package lead.codeoverflow.vkcupfinal.viewmodel.podcast

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lead.codeoverflow.vkcupfinal.entity.ReactionPopular
import lead.codeoverflow.vkcupfinal.entity.core.*
import lead.codeoverflow.vkcupfinal.model.AudioPlayerController
import lead.codeoverflow.vkcupfinal.model.local.InfoDao
import lead.codeoverflow.vkcupfinal.model.local.PodcastDao
import lead.codeoverflow.vkcupfinal.utils.MILLS
import lead.codeoverflow.vkcupfinal.viewmodel.BaseViewModel
import tw.ktrssreader.kotlin.parser.ITunesParser
import java.net.URL

private const val NORMAL_SPEED = 1
private const val DURATION_STEP_KOEF = 6

class PodcastViewModel(
    private val rssUrl: String,
    private val jsonStr: String,
    private val audioPlayerController: AudioPlayerController,
    private val podcastDao: PodcastDao,
    private val infoDao: InfoDao
) : BaseViewModel(), AudioPlayerController by audioPlayerController {

    val podcastItem = MutableLiveData<PodcastData>()

    val currentPlayItem = MutableLiveData<PlayData>()

    val availableReactions = MutableLiveData<List<ReactionData>>()

    val currentPopularReactions = MutableLiveData<List<ReactionPopular>>()

    val playSpeed = MutableLiveData(1)

    val isPlay = MutableLiveData(false)

    val playerProgress = MutableLiveData<Long>()

    val progress = MutableLiveData<Boolean>()

    val jsonResult = MutableLiveData<JsonResult>()

    init {
        audioPlayerController.init()
        parseInfoFromJson()
        parseRssUrl()
        observePlayerProgress()
    }

    private fun parseInfoFromJson() {
        val gson = Gson()
        val result = gson.fromJson(jsonStr, JsonResult::class.java)
        jsonResult.value = result
        coroutineScope.launch {
            infoDao.saveEpisodes(result.episodes ?: listOf())
        }
    }

    private fun parseRssUrl() {
        progress.value = true
        coroutineScope.launch {
            try {
                val podcastUrl = URL(rssUrl)
                val result = podcastUrl.readText()

                val channel = ITunesParser().parse(result)
                channel.items?.firstOrNull()?.guid?.value

                val podcastData = channel.toPodcastData(rssUrl)
                podcastDao.savePodcast(podcastData)
                podcastItem.postValue(podcastData)
                currentPlayItem.postValue(podcastData.playlist.firstOrNull())
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                progress.postValue(false)
            }
        }
    }

    fun changePlayState() {
        isPlay.value = !(isPlay.value ?: true)
        if (isPlay.value == true) {
            play()
        } else {
            pause()
        }
    }

    override fun playNextEpisode() {
        audioPlayerController.playNextEpisode()
        val currentIndex = getCurrentPosition()
        val listPlayData = podcastItem.value?.playlist ?: listOf()
        if (listPlayData.size > currentIndex + 1) {
            currentPlayItem.postValue(listPlayData[currentIndex + 1])
        }
    }

    override fun playPrevEpisode() {
        audioPlayerController.playPrevEpisode()
        val currentIndex = getCurrentPosition()
        val listPlayData = podcastItem.value?.playlist ?: listOf()
        if (0 < currentIndex - 1) {
            currentPlayItem.postValue(listPlayData[currentIndex - 1])
        }
    }

    fun changeSpeed() {
        if (playSpeed.value == NORMAL_SPEED) {
            playSpeed.value = 2
        } else {
            playSpeed.value = NORMAL_SPEED
        }

        changeSpeed(playSpeed.value ?: NORMAL_SPEED)
    }

    fun seekTo(progress: Int) {
        val seekPosition = getDuration() / 100 * progress
        seekTo(seekPosition)
    }

    fun getCurrentPopularReactions(duration:Long) {
        jsonResult.value?.let { result ->
            val episode = result.episodes?.find { it.guid == currentPlayItem.value?.guid }
                ?: result.episodes?.firstOrNull()

            val durationStep = duration / DURATION_STEP_KOEF
            var maxFrom = 0L
            var maxTo = durationStep
            val tempList = mutableListOf<Statistic>()
            val popularReactions = mutableListOf<ReactionPopular>()
            episode?.statistics?.forEach {
                if (it.time in maxFrom..maxTo) {
                    tempList.add(it)
                } else {
                    maxFrom += durationStep
                    maxTo += durationStep
                    val group = tempList.groupBy { it.reactionId }
                    var popularList = listOf<Statistic>()
                    group.forEach {
                        if (popularList.size < it.value.size) {
                            popularList = it.value
                        }
                    }
                    if (popularList.isNotEmpty()) {
                        popularReactions.add(
                            ReactionPopular(
                                jsonResult.value?.reactions?.find { it.id == popularList.first().reactionId }?.emoji
                                    ?: "",
                                popularList.first().time,
                                popularList.last().time
                            )
                        )
                    }
                    tempList.clear()
                }
            }
            currentPopularReactions.postValue(popularReactions)

        }
    }

    private fun observePlayerProgress() {
        coroutineScope.launch {
            try {
                observeProgress().collect { progress ->
                    playerProgress.postValue(progress)
                    withContext(Dispatchers.Default) {
                        val jsonInfo = jsonResult.value
                        jsonInfo?.let {
                            val episode =
                                it.episodes?.find { it.guid == currentPlayItem.value?.guid }
                                    ?: it.episodes?.firstOrNull()
                            val timedReaction = episode?.timedReactions?.find {
                                val to = it.to.toLong() * MILLS
                                val from = it.from.toLong() * MILLS
                                progress in from..to
                            }
                            val availableReactionList =
                                timedReaction?.availableReactions ?: listOf()
                            val reactions = mutableListOf<ReactionData>()
                            it.reactions?.forEach {
                                if (availableReactionList.contains(it.id)) {
                                    reactions.add(it)
                                }
                            }
                            availableReactions.postValue(reactions)
                        }
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }
}