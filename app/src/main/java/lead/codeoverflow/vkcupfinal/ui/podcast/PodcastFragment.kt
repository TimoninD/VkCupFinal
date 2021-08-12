package lead.codeoverflow.vkcupfinal.ui.podcast

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_podcast.*
import lead.codeoverflow.vkcupfinal.R
import lead.codeoverflow.vkcupfinal.model.AudioPlayerController
import lead.codeoverflow.vkcupfinal.ui.base.BaseFragment
import lead.codeoverflow.vkcupfinal.utils.getDominantColor
import lead.codeoverflow.vkcupfinal.viewmodel.podcast.PodcastViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PodcastFragment : BaseFragment() {
    override val layoutResId: Int = R.layout.fragment_podcast

    private val viewModel by viewModel<PodcastViewModel>() {
        parametersOf("https://vk.com/podcasts-147415323_-1000000.rss")
    }

    private val audioController by inject<AudioPlayerController>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()


    }

    private fun initObserver() {
        viewModel.podcastItem.observe(viewLifecycleOwner, {
            loadPodcastIcon(it.image?.url)

            tvTitle.text = it.title
            tvAuthor.text = it.author
            audioController.addPlaylist(it.items?.map { it.enclosure?.url ?: "" } ?: listOf())
            audioController.play()
        })

        viewModel.playSpeed.observe(viewLifecycleOwner, {
            tvSpeed.text = getString(R.string.speed_foramt, it)
        })
    }

    private fun loadPodcastIcon(url: String?) {
        Glide.with(requireContext())
            .asBitmap()
            .load(url)
            .apply(
                RequestOptions.bitmapTransform(
                    RoundedCorners(
                        resources.getDimension(R.dimen.radius_8).toInt()
                    )
                )
            )
            .addListener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ) = false

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    viewBlur.setBackgroundColor(resource?.getDominantColor() ?: R.color.black)
                    return false
                }

            })
            .into(ivIcon)
    }
}