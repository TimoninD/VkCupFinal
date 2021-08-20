package lead.codeoverflow.vkcupfinal.di

import lead.codeoverflow.vkcupfinal.viewmodel.analytic.AnalyticViewModel
import lead.codeoverflow.vkcupfinal.viewmodel.podcast.PodcastViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModelModule {
    val module = module {
        viewModel { (rssUrl: String, jsonUrl: String) ->
            PodcastViewModel(
                rssUrl,
                jsonUrl,
                get(),
                get(),
                get()
            )
        }

        viewModel { (episodeId: String) ->
            AnalyticViewModel(episodeId, get())
        }
    }
}