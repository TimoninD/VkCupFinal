package lead.codeoverflow.vkcupfinal.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import lead.codeoverflow.vkcupfinal.R
import lead.codeoverflow.vkcupfinal.entity.ReactionPopular
import lead.codeoverflow.vkcupfinal.extension.getColorCompat

class PopularReactionsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
) :
    View(
        context,
        attrs,
        defStyleAttr
    ) {

    private val rect: Rect = Rect()
    private val paint: Paint = Paint()
    private val radius = 3f.toPx()
    private val textSize = 16f.toPx()
    private val margin = 8f.toPx()

    init {
        paint.color = context.getColorCompat(R.color.white_10)
        paint.textSize = textSize
    }

    var currentDuration = 0L
    var currentReactions = listOf<ReactionPopular>()
    private var canvas: Canvas? = null
    private var canvasBitmap: Bitmap? = null


    override fun onDraw(canvas: Canvas?) {
        rect.set(0, 0, width, height)
        if (canvasBitmap == null) {
            canvasBitmap = Bitmap.createBitmap(
                width, height, Bitmap.Config.ARGB_8888
            )
        }

        if (this.canvas == null) {
            canvasBitmap?.let {
                this.canvas = Canvas(it)
            }
        }
        canvasBitmap?.let {
            canvas?.drawBitmap(it, Matrix(), null)
        }
    }

    fun init(duration: Long, reactions: List<ReactionPopular>) {
        canvas?.drawColor(Color.TRANSPARENT, BlendMode.CLEAR)
        currentDuration = duration
        currentReactions = reactions
        drawReactions()
        invalidate()
    }

    private fun drawReactions() {
        val pxToTime = currentDuration / width
        currentReactions.forEach {
            val left = it.from / pxToTime.toFloat()
            val right = it.to / pxToTime.toFloat()

            val textPosition = right - ((right - left) / 2) - textSize / 2
            paint.color = context.getColorCompat(R.color.white)
            canvas?.drawText(it.emodji, textPosition, textSize, paint)
            val rect = createRectF(left, right)
            paint.color = context.getColorCompat(R.color.white_10)
            canvas?.drawRoundRect(
                rect,
                radius,
                radius,
                paint
            )
        }
    }

    private fun createRectF(left: Float, right: Float): RectF {
        return RectF(left, 0f + margin + textSize, right, this.height.toFloat())
    }

    private fun Float.toPx() =
        this * resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
}