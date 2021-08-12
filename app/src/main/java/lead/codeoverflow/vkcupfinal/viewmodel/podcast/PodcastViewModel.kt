package lead.codeoverflow.vkcupfinal.viewmodel.podcast

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import lead.codeoverflow.vkcupfinal.viewmodel.BaseViewModel
import tw.ktrssreader.kotlin.model.channel.ITunesChannelData
import tw.ktrssreader.kotlin.parser.ITunesParser
import java.net.URL

class PodcastViewModel(private val url: String) : BaseViewModel() {

    val podcastItem = MutableLiveData<ITunesChannelData>()

    val playSpeed = MutableLiveData(1)

    init {
        parseRssUrl()
    }

    private fun parseRssUrl() {
        coroutineScope.launch {
            try {
                val podcastUrl = URL(url)
                val result = podcastUrl.readText()
                val channel = ITunesParser().parse(result)
                podcastItem.postValue(channel)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}