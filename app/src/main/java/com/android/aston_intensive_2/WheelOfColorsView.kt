package com.android.aston_intensive_2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class WheelOfColorsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr)  {

    private val colorsForWheel = arrayOf(
        Paint().apply { color = Color.parseColor("#FF9800") },
        Paint().apply { color = Color.parseColor("#673AB7") },
        Paint().apply { color = Color.GREEN },
        Paint().apply { color = Color.YELLOW },
        Paint().apply { color = Color.BLUE },
        Paint().apply { color = Color.CYAN },
        Paint().apply { color = Color.RED }
    )
    private val sectionCount = colorsForWheel.size
    var currentAngle: Float = 0f
    private val sweepAngle = 360f / sectionCount

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawWheelOfColors(canvas)
    }

    private fun drawWheelOfColors(canvas: Canvas) {
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = width.coerceAtMost(height) / 2f

        canvas.translate(centerX,centerY)

        for (iteration in colorsForWheel.indices){
            canvas.drawArc(RectF(-radius,-radius,radius,radius),
                currentAngle + iteration * sweepAngle,
                sweepAngle,
                true,
                colorsForWheel[iteration])
        }
    }

//    fun getColorIndex(): Int{
//        return ((currentAngle + sweepAngle / 2) / sweepAngle).toInt() % sectionCount
//    }

    fun settingCurrentAngle(angle: Float){
        currentAngle = angle
        invalidate()
    }

    fun rotation(){
        currentAngle += 20f
        if (currentAngle >= 360) currentAngle -= 360
        invalidate()
    }


}