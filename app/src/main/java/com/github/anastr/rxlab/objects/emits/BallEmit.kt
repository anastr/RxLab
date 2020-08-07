package com.github.anastr.rxlab.objects.emits

import android.graphics.*
import android.text.TextPaint
import com.github.anastr.rxlab.util.Point
import com.github.anastr.rxlab.util.Utils

/**
 * Created by Anas Altair on 3/7/2020.
 */
open class BallEmit(leftTopPoint: Point = Point(0f, 0f)): EmitObject(leftTopPoint) {

    constructor(value: String, color: Int = Color.RED, leftTopPoint: Point = Point(0f, 0f)) : this() {
        this.value = value
        super.color = color
        rect.offsetTo(leftTopPoint.x, leftTopPoint.y)
    }

    var value: String = ""

    protected var valueTextHeight: Float

    protected val valuePaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    init {
        valuePaint.textSize = Utils.emitTextSize
        valuePaint.typeface = Typeface.DEFAULT_BOLD
        valuePaint.color = Color.WHITE
        valuePaint.textAlign = Paint.Align.CENTER
        val valueBounds = Rect()
        valuePaint.getTextBounds("10", 0, 1, valueBounds)
        valueTextHeight = valueBounds.height().toFloat()
    }

    override fun drawContent(canvas: Canvas) {
        canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width()*.5f, emitPaint)
        canvas.drawText(value, rect.centerX(), rect.centerY() + valueTextHeight*.5f, valuePaint)
    }

}