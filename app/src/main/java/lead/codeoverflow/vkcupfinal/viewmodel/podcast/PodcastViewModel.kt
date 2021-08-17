package lead.codeoverflow.vkcupfinal.viewmodel.podcast

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import lead.codeoverflow.vkcupfinal.entity.core.PodcastData
import lead.codeoverflow.vkcupfinal.entity.core.toPodcastData
import lead.codeoverflow.vkcupfinal.model.AudioPlayerController
import lead.codeoverflow.vkcupfinal.viewmodel.BaseViewModel
import tw.ktrssreader.kotlin.model.channel.ITunesChannelData
import tw.ktrssreader.kotlin.parser.ITunesParser
import java.net.URL

class PodcastViewModel(
    private val rssUrl: String,
    private val jsonUrl: String,
    private val audioPlayerController: AudioPlayerController
) : BaseViewModel() {

    val podcastItem = MutableLiveData<PodcastData>()

    val playSpeed = MutableLiveData(1)

    val playerProgress = MutableLiveData<Long>()

    init {
        parseRssUrl()
        observePlayerProgress()
    }

    private fun parseRssUrl() {
        coroutineScope.launch {
            try {
                val podcastUrl = URL(rssUrl)
                val result = podcastUrl.readText()
                val channel = ITunesParser().parse(result)
                channel.items?.firstOrNull()?.guid?.value
                podcastItem.postValue(channel.toPodcastData())
            } catch (e: Exception) {
                e.printStackTrace()
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