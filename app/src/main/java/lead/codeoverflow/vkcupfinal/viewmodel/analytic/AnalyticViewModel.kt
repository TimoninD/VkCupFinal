package lead.codeoverflow.vkcupfinal.viewmodel.analytic

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import lead.codeoverflow.vkcupfinal.entity.core.Episode
import lead.codeoverflow.vkcupfinal.model.local.InfoDao
import lead.codeoverflow.vkcupfinal.viewmodel.BaseViewModel

class AnalyticViewModel(private val episodeId: String, private val infoDao: InfoDao) :
    BaseViewModel() {

    val episode = MutableLiveData<Episode>()

    val progress = MutableLiveData<Boolean>()

    val error = MutableLiveData<String>()

    init {
        getEpisode()
    }

    private fun getEpisode() {
        progress.value = true
        coroutineScope.launch {
            try {
                val episodeData = infoDao.getEpisode("podcast-147415323_456239773"/*episodeId*/)
                if (episodeData != null) {
                    episode.postValue(episodeData)
                }
            } catch (t: Throwable) {
                error.postValue(t.message.orEmpty())
            } finally {
                progress.postValue(false)
            }
        }
    }

}