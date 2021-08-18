package lead.codeoverflow.vkcupfinal.ui.podcast

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_podcast.*
import kotlinx.android.synthetic.main.fragment_podcast.tvTitle
import lead.codeoverflow.vkcupfinal.R
import lead.codeoverflow.vkcupfinal.entity.Reaction
import lead.codeoverflow.vkcupfinal.extension.addProgressListener
import lead.codeoverflow.vkcupfinal.model.AudioPlayerControllerImpl
import lead.codeoverflow.vkcupfinal.ui.base.BaseFragment
import lead.codeoverflow.vkcupfinal.utils.extractTime
import lead.codeoverflow.vkcupfinal.utils.getDominantColor
import lead.codeoverflow.vkcupfinal.utils.parseDuration
import lead.codeoverflow.vkcupfinal.viewmodel.podcast.PodcastViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PodcastFragment : BaseFragment() {
    override val layoutResId: Int = R.layout.fragment_podcast

    private val navArgs: PodcastFragmentArgs by navArgs()

    private val viewModel by viewModel<PodcastViewModel>() {
        parametersOf(navArgs.rssUrl, navArgs.jsonUrl)
    }

    private val reactionAdapter by lazy {
        ListDelegationAdapter(
            reactionAdapterDelegate {

            }
        ).apply {
            items = Reaction.values().toList()
            notifyDataSetChanged()
        }
    }

    private var bottomSheetDrawable: Drawable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvReaction.adapter = reactionAdapter
        initObserver()
        initListeners()
    }

    private fun initListeners() {
        ivPause.setOnClickListener {
            viewModel.changePlayState()
        }

        tvSpeed.setOnClickListener {
            viewModel.changeSpeed()
        }

        ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        ivNext.setOnClickListener {
            viewModel.playNextEpisode()
        }

        ivPrev.setOnClickListener {
            viewModel.playPrevEpisode()
        }

        seekBarPosition.addProgressListener({}, {
            viewModel.seekTo(it, tvDuration.text.toString().parseDuration())
        })

        seekBarVolume.addProgressListener({
            viewModel.changeVolume(it)
        }, {

        })
    }

    private fun initObserver() {
        viewModel.podcastItem.observe(viewLifecycleOwner, {
            loadPodcastIcon(it.image)

            tvTitle.text = it.title
            tvAuthor.text = it.owner
            viewModel.addPlaylist(it.playlist.map { it.url })
            tvCurrentTime.text = getString(R.string.default_time)
        })

        viewModel.currentPlayItem.observe(viewLifecycleOwner, {
            tvDuration.text = it.duration
        })

        viewModel.playSpeed.observe(viewLifecycleOwner, {
            tvSpeed.text = getString(R.string.speed_foramt, it)
        })

        viewModel.isPlay.observe(viewLifecycleOwner, {
            if (it) {
                ivPause.setImageResource(R.drawable.ic_pause)
                viewModel.play()
            } else {
                ivPause.setImageResource(R.drawable.ic_play)
                viewModel.pause()
            }
        })

        viewModel.progress.observe(viewLifecycleOwner, {
            content.isVisible = !it
            progress.isVisible = it
        })

        viewModel.playerProgress.observe(viewLifecycleOwner, {
            tvCurrentTime.text = it.extractTime()
            seekBarPosition.progress =
                (it / tvDuration.text.toString().parseDuration() * 100).toInt()
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
                    val dominantColor = resource?.getDominantColor() ?: R.color.black
                    val topGradientDrawable = GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(
                            dominantColor,
                            ContextCompat.getColor(requireContext(), R.color.black)
                        )
                    )
                    topGradientDrawable.gradientRadius = 1000f
                    topGradientDrawable.innerRadius = 1000

                    bottomSheetDrawable = GradientDrawable(
                        GradientDrawable.Orientation.BOTTOM_TOP,
                        intArrayOf(
                            dominantColor,
                            ContextCompat.getColor(requireContext(), R.color.black)
                        )
                    )
                    viewBlur.background = topGradientDrawable
                    reactionContainer.background = bottomSheetDrawable
                    return false
                }

            })
            .into(ivIcon)
    }

    override fun onDestroy() {
        viewModel.close()
        super.onDestroy()
    }
}