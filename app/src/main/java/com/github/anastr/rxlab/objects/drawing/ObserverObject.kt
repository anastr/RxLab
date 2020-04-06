package com.github.anastr.rxlab.objects.drawing

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.github.anastr.rxlab.objects.EmitObject
import com.github.anastr.rxlab.util.Point
import com.github.anastr.rxlab.util.Utils
import com.github.anastr.rxlab.util.dpToPx

/**
 * Created by Anas Altair on 3/20/2020.
 */
class ObserverObject(name:String): DrawingObject(name) {

    private val arrowPath = Path()
    private val arrowEndX
        get () = rect.width() * .95f
    private val completePath = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val namePadding = dpToPx(4f)

    private val garbageEmits = ArrayList<Int>()
    private var isComplete = false

    init {
        paint.color = Color.DKGRAY
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = dpToPx(3f)
        paint.strokeCap = Paint.Cap.ROUND

        updatePath()
    }

    private fun updatePath() {
        arrowPath.reset()
        arrowPath.moveTo(0f, rect.centerY())
        arrowPath.lineTo(arrowEndX, rect.centerY())
        arrowPath.moveTo(arrowEndX - dpToPx(20f), rect.centerY() - dpToPx(20f))
        arrowPath.lineTo(arrowEndX, rect.centerY())
        arrowPath.lineTo(arrowEndX - dpToPx(20f), rect.centerY() + dpToPx(20f))

        completePath.reset()
        completePath.moveTo(arrowEndX - dpToPx(40f), rect.centerY() - dpToPx(7f))
        completePath.lineTo(arrowEndX - dpToPx(40f), rect.centerY() + dpToPx(7f))
    }

    override fun onSizeChanged(width: Int, height: Int) {
        super.onSizeChanged(width, height)
        updatePath()
    }

    override fun onAddEmits(emitObject: EmitObject) {
        if (isComplete)
            throw IllegalStateException("you can't add emits after onComplete!")
    }

    override fun draw(delta: Long, canvas: Canvas) {

        canvas.drawPath(arrowPath, paint)

        canvas.drawText(name, namePadding, rect.centerY() - namePadding, textPaint)

        emitObjects.forEachIndexed { index, emitObject ->
            if (!isComplete)
                emitObject.rect.offset(- delta * Utils.emitSpeed, 0f)
            emitObject.draw(canvas)
            if (emitObject.rect.right < 0f)
                garbageEmits.add(0, index)
        }
        if (isComplete)
            canvas.drawPath(completePath, paint)

        garbageEmits.forEach { removeEmitAt(it) }
        garbageEmits.clear()
    }

    override fun getInsertPoint(): Point
            = Point(arrowEndX - dpToPx(80f) - Utils.emitSize *.5f, rect.centerY() - Utils.emitSize *.5f)

    /**
     * must be called on render thread.
     */
    fun complete() {
        isComplete = true
    }
}