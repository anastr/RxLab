package com.github.anastr.rxlab.objects.emits

import android.graphics.Canvas
import android.graphics.Color
import com.github.anastr.rxlab.util.Point

/**
 * Created by Anas Altair on 3/7/2020.
 */
open class BallEmit(leftTopPoint: Point = Point(0f, 0f)): EmitObject(leftTopPoint) {

    constructor(value: String, color: Int = Color.RED, leftTopPoint: Point = Point(0f, 0f)) : this() {
        super.value = value
        super.color = color
        rect.offsetTo(leftTopPoint.x, leftTopPoint.y)
    }

    override fun drawContent(canvas: Canvas) {
        canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width()*.5f, emitPaint)
        canvas.drawText(value, rect.centerX(), rect.centerY() + valueTextHeight*.5f, valuePaint)
    }

}