package lead.codeoverflow.vkcupfinal.ui.podcast

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_podcast.*
import lead.codeoverflow.vkcupfinal.R
import lead.codeoverflow.vkcupfinal.ui.base.BaseFragment
import lead.codeoverflow.vkcupfinal.viewmodel.podcast.PodcastViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PodcastFragment : BaseFragment() {
    override val layoutResId: Int = R.layout.fragment_podcast

    private val viewModel by viewModel<PodcastViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView.loadUrl("https://vk.com/podcasts-147415323_-1000000.rss")
        viewModel.parseRssUrl("https://vk.com/podcasts-147415323_-1000000.rss")
    }
}