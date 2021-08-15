package lead.codeoverflow.vkcupfinal.entity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import lead.codeoverflow.vkcupfinal.R

enum class Reaction(
    @StringRes val strId: Int,
    @DrawableRes val iconId: Int
) {
    FUNNY(R.string.funny, R.drawable.ic_funny),
    LIKE(R.string.like, R.drawable.ic_like),
    DISLIKE(R.string.dislike, R.drawable.ic_dislike),
    ANGRY(R.string.angry, R.drawable.ic_angry),
    SAD(R.string.sad, R.drawable.ic_sad),
    CONFUSED(R.string.confused, R.drawable.ic_confused),
    MONEY(R.string.money, R.drawable.ic_money),
    SHIT(R.string.shit, R.drawable.ic_shit),
}