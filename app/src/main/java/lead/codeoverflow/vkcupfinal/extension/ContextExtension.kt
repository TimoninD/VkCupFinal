package lead.codeoverflow.vkcupfinal.extension

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.getColorCompat(@ColorRes colorResId: Int): Int =
    ContextCompat.getColor(this, colorResId)

fun Context.dpToPx(dp: Float) = dp * resources.displayMetrics.density