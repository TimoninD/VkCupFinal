package lead.codeoverflow.vkcupfinal.ui.choosepodcast

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_choose_podcast.*
import lead.codeoverflow.vkcupfinal.R
import lead.codeoverflow.vkcupfinal.ui.base.BaseFragment

class ChoosePodcastFragment : BaseFragment() {
    override val layoutResId: Int = R.layout.fragment_choose_podcast

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSave.setOnClickListener {
            if (etRssLink.text.isNotBlank()) {
                findNavController().navigate(
                    ChoosePodcastFragmentDirections.actionChoosePodcastFragmentToPodcastFragment(
                        etRssLink.text.toString(),
                        etJsonLink.text.toString()
                    )
                )
            }
        }
    }

}