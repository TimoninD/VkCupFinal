package lead.codeoverflow.vkcupfinal.utils

import android.graphics.Bitmap
import androidx.palette.graphics.Palette

fun Bitmap.getDominantColor(): Int? {
    val swatches = Palette.from(this).generate().swatches
    val swatch = swatches.maxByOrNull { it.population }
    return swatch?.rgb
}