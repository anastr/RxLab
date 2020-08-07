package com.github.anastr.rxlab.objects.emits

import android.graphics.Canvas
import com.github.anastr.rxlab.util.Point

/**
 * Created by Anas Altair on 4/13/2020.
 */
class MergedBallEmit(leftTopPoint: Point, vararg emits: EmitObject): EmitObject(leftTopPoint) {

    private val colors = emits.map { it.color }

    init {
        super.value = emits.map { it.value }.reduce { acc, emitValue -> "$acc-$emitValue" }
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