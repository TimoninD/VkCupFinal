package lead.codeoverflow.vkcupfinal.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.widget.FrameLayout
import lead.codeoverflow.vkcupfinal.R
import lead.codeoverflow.vkcupfinal.extension.getColorCompat
import kotlin.experimental.and
import kotlin.experimental.xor
import kotlin.math.max
import kotlin.math.min

class AudioVisualizer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
) :
    FrameLayout(
        context,
        attrs,
        defStyleAttr
    ) {
    private var maxColumns = 15
    private val activeColor = context.getColorCompat(R.color.accent)
    private var canvas: Canvas? = null
    private var canvasBitmap: Bitmap? = null
    private val rect: Rect = Rect()
    private val paint: Paint = Paint()
    private val columnWidth = 3f.toPx()
    private val columnSpace = 2f.toPx()
    private val radius = 3f.toPx()

    private val barCount by lazy {
        width / (columnWidth + columnSpace)
    }

    var audioWavePosition = 0

    private val volumeList: MutableList<Int> = mutableListOf()

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        maxColumns = width / (columnWidth + columnSpace).toInt()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
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
            canvas.drawBitmap(it, Matrix(), null)
        }
    }

    fun init(volumesByte: List<Byte>) {
        val volumes = getVolumesFromBytes(volumesByte)
        volumeList.addAll(volumes)
        drawSticks()
        invalidate()
    }

    private fun getVolumesFromBytes(bytes: List<Byte>): List<Int> {
        val volumesList = mutableListOf<Int>()
        var i = 0
        while (i + 1 < bytes.size) {
            val volume = ((bytes[i + 1] and 0xff.toByte()).toInt()
                .shl(8)) xor (bytes[i] and 0xff.toByte()).toInt()
            volumesList.add(volume)
            i += bytes.size / barCount.toInt()
        }
        return volumesList
    }

    private fun drawSticks() {
        val stickCount = min(volumeList.size, maxColumns)
        var left = 0f
        var right = columnWidth
        for (i in 0 until stickCount) {
            val currentPosition = max(0, volumeList.size - stickCount + i)
            val height = getStickHeight(volumeList[currentPosition])
            val rect = createRectF(left, right, height)
            paint.alpha = if (i <= audioWavePosition) 255 else 33
            canvas?.drawRoundRect(
                rect,
                radius,
                radius,
                paint
            )
            left += columnWidth + columnSpace
            right = left + columnWidth
        }
    }

    private fun getStickHeight(volume: Int): Float {
        return max(height / 72f.toPx() * volume.toFloat(), 3f.toPx())
    }

    private fun createRectF(left: Float, right: Float, height: Float): RectF {
        return RectF(left, this.height.toFloat() - height, right, this.height.toFloat())
    }


    fun Float.toPx() = this * resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT

    init {
        paint.color = activeColor
    }
}