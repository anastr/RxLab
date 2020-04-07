package com.github.anastr.rxlab.objects.drawing

import android.graphics.Canvas
import android.graphics.Paint
import com.github.anastr.rxlab.util.Point
import com.github.anastr.rxlab.util.Utils
import com.github.anastr.rxlab.util.dpToPx

/**
 * Created by Anas Altair on 4/1/2020.
 */
class TextOperation(name: String, private val text: String): OperationObject(name) {

    override var contentWidth: Float = textPaint.measureText(text)

    override fun draw(delta: Long, canvas: Canvas) {
        super.draw(delta, canvas)
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText(text, rect.centerX(), rect.centerY() + textHeight*.5f, textPaint)
        emitObjects.forEach { it.draw(canvas) }
    }

    override fun getInsertPoint(): Point =
        Point(rect.centerX() + contentWidth * .5f + dpToPx(10f)
            , rect.centerY() - Utils.emitSize * .5f)
}