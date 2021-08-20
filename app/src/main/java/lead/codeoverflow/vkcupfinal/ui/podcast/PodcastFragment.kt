package lead.codeoverflow.vkcupfinal.ui.podcast

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_analytic.*
import kotlinx.android.synthetic.main.fragment_podcast.*
import kotlinx.android.synthetic.main.fragment_podcast.ivBack
import kotlinx.android.synthetic.main.fragment_podcast.progress
import kotlinx.android.synthetic.main.fragment_podcast.tvTitle
import lead.codeoverflow.vkcupfinal.R
import lead.codeoverflow.vkcupfinal.entity.Reaction
import lead.codeoverflow.vkcupfinal.entity.core.ReactionData
import lead.codeoverflow.vkcupfinal.extension.addProgressListener
import lead.codeoverflow.vkcupfinal.model.AudioPlayerControllerImpl
import lead.codeoverflow.vkcupfinal.model.Prefs
import lead.codeoverflow.vkcupfinal.ui.base.BaseFragment
import lead.codeoverflow.vkcupfinal.utils.MILLS
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
        parametersOf(navArgs.rssUrl, navArgs.jsonStr)
    }

    private val reactionAdapter by lazy {
        ReactionAdapter {

        }
    }

    private val popularReactionAdapter by lazy {
        ListDelegationAdapter(
            reactionPopularAdapterDelegate()
        )
    }

    private var bottomSheetDrawable: Drawable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvReaction.adapter = reactionAdapter
        //rvPopularReaction.adapter = popularReactionAdapter

        initObserver()
        initListeners()
    }

    private fun initListeners() {
        ivPause.setOnClickListener {
            viewModel.changePlayState()
        }

        ivAnalytic.setOnClickListener {
            viewModel.currentPlayItem.value?.let {
                findNavController().navigate(
                    PodcastFragmentDirections.actionPodcastFragmentToAnalyticFragment(
                        it.guid, viewModel.getDuration()
                    )
                )
            }
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
            viewModel.seekTo(it)
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

            ivAnalytic.isVisible = viewModel.jsonResult.value != null
        })

        viewModel.currentPlayItem.observe(viewLifecycleOwner, { playData ->
            tvDuration.text = playData.duration
            viewModel.getCurrentPopularReactions(playData.duration.parseDuration())
        })

        viewModel.playSpeed.observe(viewLifecycleOwner, {
            tvSpeed.text = getString(R.string.speed_foramt, it)
        })

        viewModel.currentPopularReactions.observe(viewLifecycleOwner, {
            viewPopularReaction.postDelayed({
                viewPopularReaction.init(viewModel.currentPlayItem.value?.duration.toString().parseDuration(), it)
            }, 0)

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
            val progress = (it.toFloat() / viewModel.getDuration().toFloat() * 100).toInt()
            seekBarPosition.progress = progress

        })

        viewModel.availableReactions.observe(viewLifecycleOwner, {
            reactionAdapter.items = it
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

    override fun onStop() {
        viewModel.isPlay.value = false
        super.onStop()
    }

    override fun onDestroy() {
        viewModel.close()
        super.onDestroy()
    }
}