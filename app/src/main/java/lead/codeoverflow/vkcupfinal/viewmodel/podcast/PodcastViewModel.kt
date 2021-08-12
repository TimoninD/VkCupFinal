package lead.codeoverflow.vkcupfinal.viewmodel.podcast

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lead.codeoverflow.vkcupfinal.viewmodel.BaseViewModel
import tw.ktrssreader.kotlin.parser.ITunesParser
import java.net.URL

class PodcastViewModel : BaseViewModel() {

    fun parseRssUrl(url: String) {
        coroutineScope.launch {
            try {
                val podcastUrl = URL(url)
                val result = podcastUrl.readText()
                val channel = ITunesParser().parse(result)
                withContext(Dispatchers.Main) {
                    Log.e("Channel", channel.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}