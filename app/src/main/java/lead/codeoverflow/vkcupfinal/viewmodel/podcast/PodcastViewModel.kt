package lead.codeoverflow.vkcupfinal.viewmodel.podcast

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import lead.codeoverflow.vkcupfinal.entity.core.PlayData
import lead.codeoverflow.vkcupfinal.entity.core.PodcastData
import lead.codeoverflow.vkcupfinal.entity.core.toPodcastData
import lead.codeoverflow.vkcupfinal.model.AudioPlayerController
import lead.codeoverflow.vkcupfinal.model.AudioPlayerControllerImpl
import lead.codeoverflow.vkcupfinal.model.local.PodcastDao
import lead.codeoverflow.vkcupfinal.viewmodel.BaseViewModel
import tw.ktrssreader.kotlin.parser.ITunesParser
import java.net.URL

private const val NORMAL_SPEED = 1

class PodcastViewModel(
    private val rssUrl: String,
    private val jsonUrl: String,
    private val audioPlayerController: AudioPlayerController,
    private val podcastDao: PodcastDao
) : BaseViewModel(), AudioPlayerController by audioPlayerController {

    val podcastItem = MutableLiveData<PodcastData>()

    val currentPlayItem = MutableLiveData<PlayData>()

    val playSpeed = MutableLiveData(1)

    val isPlay = MutableLiveData(false)

    val playerProgress = MutableLiveData<Long>()

    val progress = MutableLiveData<Boolean>()

    init {
        audioPlayerController.init()
        parseRssUrl()
        observePlayerProgress()
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

        coroutineScope.launch {
            try {
                val podcastJsonUrl = URL(jsonUrl)
                val result = podcastJsonUrl.readText()
            } catch (e: Exception) {
                e.printStackTrace()
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

    fun seekTo(progress: Int, duration: Long) {
        val seekPosition = duration / 100 * progress
        seekTo(seekPosition)
    }

    private fun observePlayerProgress() {
        coroutineScope.launch {
            try {
                observeProgress().collect {
                    playerProgress.postValue(it)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }
}