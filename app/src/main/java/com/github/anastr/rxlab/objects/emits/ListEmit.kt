package com.github.anastr.rxlab.objects.emits

import android.graphics.Canvas
import android.graphics.Paint
import com.github.anastr.rxlab.util.Point
import com.github.anastr.rxlab.util.dpToPx

class ListEmit(leftTopPoint: Point, vararg ballEmits: BallEmit): BallEmit(leftTopPoint) {

    private val listPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        value = "{${ballEmits.map { it.value }.reduce { acc, emitValue -> "$acc, $emitValue" }}}"
        ballEmits.firstOrNull()?.let { listPaint.color = it.color }
    }

    override fun drawContent(canvas: Canvas) {
        canvas.drawRoundRect(rect, dpToPx(5f), dpToPx(5f), listPaint)
        canvas.drawText(value, rect.centerX(), rect.centerY() + valueTextHeight*.5f, valuePaint)
    }

}