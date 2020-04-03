package com.github.anastr.rxlab.objects.drawing

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.github.anastr.rxlab.util.dpToPx

/**
 * Created by Anas Altair on 3/23/2020.
 */
abstract class OperationObject(name:String): DrawingObject(name) {

    protected var textHeight: Float = 0f

    init {
        textPaint.textSize = dpToPx(15f)
        val bounds = Rect()
        textPaint.getTextBounds("Test", 0, 4, bounds)
        textHeight = bounds.height().toFloat()
    }

    abstract var contentWidth: Float

    override fun draw(delta: Long, canvas: Canvas) {
        textPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText("$name( ", rect.centerX() - contentWidth * .5f, rect.centerY() + textHeight*.5f, textPaint)
        textPaint.textAlign = Paint.Align.LEFT
        canvas.drawText(" )", rect.centerX() + contentWidth * .5f, rect.centerY() + textHeight*.5f, textPaint)
    }
}