package lead.codeoverflow.vkcupfinal.ui.analytic

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.android.synthetic.main.fragment_analytic.*
import lead.codeoverflow.vkcupfinal.R
import lead.codeoverflow.vkcupfinal.entity.core.Episode
import lead.codeoverflow.vkcupfinal.extension.getColorCompat
import lead.codeoverflow.vkcupfinal.ui.base.BaseFragment
import lead.codeoverflow.vkcupfinal.viewmodel.analytic.AnalyticViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val BAR_COUNT = 24

class AnalyticFragment : BaseFragment() {
    override val layoutResId: Int = R.layout.fragment_analytic

    private val args: AnalyticFragmentArgs by navArgs()

    private val viewModel: AnalyticViewModel by viewModel() {
        parametersOf(args.episodeId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        viewModel.error.observe(viewLifecycleOwner, {
            showToast(it)
        })

        viewModel.progress.observe(viewLifecycleOwner, {
            progress.isVisible = it
        })

        viewModel.episode.observe(viewLifecycleOwner, {
            drawReactionsChart(it)

        })

    }

    private fun drawReactionsChart(episode: Episode) {
        val barEntries = mutableListOf<BarEntry>()

        val durationStep = args.duration / BAR_COUNT

        var maxFrom = 0L

        var maxTo = durationStep

        var count = 0

        var currentX = 1
        episode.statistics.forEach {
            if (it.time in maxFrom..maxTo) {
                count++
            } else {
                barEntries.add(BarEntry(currentX.toFloat(), count.toFloat()))
                maxFrom += durationStep
                maxTo += durationStep
                count = 0
                currentX++
            }
        }
        val barDataSet = BarDataSet(barEntries, "")
        barDataSet.setDrawValues(false)
        barDataSet.color = requireContext().getColorCompat(R.color.blue_32)

        val barData = BarData(barDataSet)
        barData.barWidth = 0.5f

        reactionChart.xAxis.setDrawGridLines(false)
        reactionChart.xAxis.setDrawAxisLine(false)
        reactionChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        reactionChart.xAxis.textColor = requireContext().getColorCompat(R.color.primary)
        reactionChart.xAxis.textSize = 12f
        reactionChart.xAxis.typeface = ResourcesCompat.getFont(requireContext(), R.font.vk_regular)


        reactionChart.axisLeft.setDrawGridLines(false)
        reactionChart.axisLeft.setDrawAxisLine(false)
        reactionChart.axisLeft.setDrawLabels(false)

        reactionChart.axisRight.setDrawGridLines(false)
        reactionChart.axisRight.setDrawAxisLine(false)
        reactionChart.axisRight.setDrawLabels(false)

        reactionChart.extraBottomOffset = 5f
        reactionChart.description.isEnabled = false
        reactionChart.legend.isEnabled = false

        reactionChart.data = barData
        reactionChart.invalidate()
    }
}