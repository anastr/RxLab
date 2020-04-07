package com.github.anastr.rxlab.objects.drawing

import android.graphics.Canvas
import android.graphics.Paint
import com.github.anastr.rxlab.objects.EmitObject
import com.github.anastr.rxlab.util.Point
import com.github.anastr.rxlab.util.Utils
import com.github.anastr.rxlab.util.dpToPx

/**
 * this is a drawing object to draw fixed number of emits.
 *
 * Created by Anas Altair on 3/23/2020.
 */
class FixedEmitsOperation(name: String, emits: List<EmitObject>): OperationObject(name) {

    private val padding = dpToPx(4f)

    override var contentWidth: Float = 0f

    init {
        addEmits(emits)
    }

    override fun draw(delta: Long, canvas: Canvas) {
        super.draw(delta, canvas)
        textPaint.textAlign = Paint.Align.LEFT
        emitObjects.forEachIndexed { index, emitObject ->
            emitObject.draw(canvas)
            if (index != emitObjects.size - 1)
                canvas.drawText(",", emitObject.rect.left + Utils.emitSize, rect.centerY() + Utils.emitSize*.5f, textPaint)
        }
    }

    override fun onSizeChanged(width: Int, height: Int) {
        super.onSizeChanged(width, height)
        updateContent(true)
    }

    override fun onAddEmit(emitObject: EmitObject) {
        updateContent(false)
    }

    override fun onRemoveEmit(emitObject: EmitObject) {
        updateContent(true)
    }

    private fun updateContent(manageLastEmit: Boolean) {
        contentWidth = Utils.emitSize * emitObjects.size + padding * emitObjects.size
        var startContentX = rect.centerX() - contentWidth *.5f
        emitObjects.forEachIndexed { index, emitObject ->
            // it's inserting responsibility to manage last emit position.
            if (!manageLastEmit && index == emitObjects.size -1)
                return@forEachIndexed
            val emitLeft = startContentX + index * Utils.emitSize
            emitObject.rect.offsetTo(emitLeft, rect.centerY() - Utils.emitSize*.5f)
            startContentX += padding
        }
    }

    override fun getInsertPoint(): Point =
        Point(rect.centerX() + contentWidth *.5f - Utils.emitSize *.5f
            , rect.centerY() - Utils.emitSize * .5f)
}