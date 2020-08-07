package com.github.anastr.rxlab.objects.emits

import android.graphics.Canvas
import com.github.anastr.rxlab.util.Point

/**
 * Created by Anas Altair on 4/13/2020.
 */
class MergedBallEmit(leftTopPoint: Point, vararg ballEmits: BallEmit): BallEmit(leftTopPoint) {

    private val colors = ballEmits.map { it.color }

    init {
        super.value = ballEmits[0].value
        ballEmits.drop(1).forEach { value += "-${it.value}" }
    }

    override fun drawContent(canvas: Canvas) {
        var startAngle = 0f
        val sweep = 360f / colors.size
        colors.forEach {
            super.color = it
            canvas.drawArc(rect, startAngle, sweep, true, emitPaint)
            startAngle += sweep
        }
        canvas.drawText(value, rect.centerX(), rect.centerY() + valueTextHeight*.5f, valuePaint)
    }
}