package lead.codeoverflow.vkcupfinal.ui.podcast

import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import kotlinx.android.synthetic.main.view_reaction.view.*
import lead.codeoverflow.vkcupfinal.R
import lead.codeoverflow.vkcupfinal.entity.Reaction

var isExpanded = false
fun reactionAdapterDelegate(onClick: (Reaction) -> Unit) =
    adapterDelegate<Reaction, Reaction>(R.layout.view_reaction) {

        bind {
            with(itemView) {
                ivIcon.setImageResource(item.iconId)
                tvText.text = getString(item.strId)
                tvText.isVisible = isExpanded
                setOnClickListener {
                    onClick.invoke(item)
                }
            }
        }
    }