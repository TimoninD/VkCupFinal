package lead.codeoverflow.vkcupfinal.ui.podcast

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegate
import kotlinx.android.synthetic.main.view_popular_reaction.view.*
import kotlinx.android.synthetic.main.view_reaction.view.*
import lead.codeoverflow.vkcupfinal.R
import lead.codeoverflow.vkcupfinal.entity.Reaction
import lead.codeoverflow.vkcupfinal.entity.core.ReactionData
import lead.codeoverflow.vkcupfinal.entity.core.ReactionDataDiff

fun reactionAdapterDelegate(onClick: (ReactionData) -> Unit) =
    adapterDelegate<ReactionData, ReactionData>(R.layout.view_reaction) {

        bind {
            with(itemView) {
                tvReaction.text = item.emoji
                setOnClickListener {
                    onClick.invoke(item)
                }
            }
        }
    }

fun reactionPopularAdapterDelegate() =
    adapterDelegate<Reaction, Reaction>(R.layout.view_popular_reaction) {
        bind {
            with(itemView) {
                tvEmodji.text = item.emodji
            }
        }
    }

class ReactionAdapter(onClick: (ReactionData) -> Unit) :
    AsyncListDifferDelegationAdapter<ReactionData>(ReactionDataDiff()) {
    init {
        delegatesManager.addDelegate(reactionAdapterDelegate(onClick))
    }
}