package lead.codeoverflow.vkcupfinal.viewmodel.podcast

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lead.codeoverflow.vkcupfinal.entity.core.PodcastData
import lead.codeoverflow.vkcupfinal.entity.core.toPodcastData
import lead.codeoverflow.vkcupfinal.model.AudioPlayerController
import lead.codeoverflow.vkcupfinal.model.local.PodcastDao
import lead.codeoverflow.vkcupfinal.viewmodel.BaseViewModel
import tw.ktrssreader.kotlin.model.channel.ITunesChannelData
import tw.ktrssreader.kotlin.parser.ITunesParser
import java.net.URL

class PodcastViewModel(
    private val rssUrl: String,
    private val jsonUrl: String,
    private val audioPlayerController: AudioPlayerController,
    private val podcastDao: PodcastDao
) : BaseViewModel() {

    val podcastItem = MutableLiveData<PodcastData>()

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
    }

    private fun observePlayerProgress() {
        coroutineScope.launch {
            try {
                audioPlayerController.observeProgress().collect {
                    playerProgress.postValue(it)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }
}