package com.github.anastr.rxlab.objects.drawing

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import com.github.anastr.rxlab.objects.emits.EmitObject
import com.github.anastr.rxlab.util.Point
import com.github.anastr.rxlab.util.Utils
import com.github.anastr.rxlab.util.dpToPx
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Anas Altair on 3/20/2020.
 */
abstract class DrawingObject() {

    private val uid: UUID = UUID.randomUUID()

    val rect = RectF()
    val emitObjects = ArrayList<EmitObject>()

    protected val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    init {
        textPaint.color = Color.DKGRAY
        textPaint.textSize = dpToPx(8f)
    }

    internal fun updatePosition(top: Float, right: Float) {
        rect.set(0f, top, right, top + Utils.drawingHeight)
    }

    open fun onSizeChanged(width: Int, height: Int) {
        rect.right = width.toFloat()
    }
    abstract fun draw(delta: Long, canvas: Canvas)
    abstract fun getInsertPoint(): Point
    protected open fun onRemoveEmit(emitObject: EmitObject) { }
    protected open fun onAddEmit(emitObject: EmitObject) { }

    /**
     * add list of emits immediately to `insertPoint`.
     *
     * **must be called on render thread.**
     */
    fun addEmits(emits: List<EmitObject>) {
        emits.forEach { addEmit(it, getInsertPoint()) }
    }


    /**
     * add emit immediately to `insertPoint`, or other point.
     *
     * **must be called on render thread.**
     */
    fun addEmit(emit: EmitObject, initial: Point = getInsertPoint()) {
        emit.offsetTo(initial)
        emitObjects.add(emit)
        onAddEmit(emit)
    }

    /**
     * remove emit by index.
     *
     * **must be called on render thread.**
     */
    fun removeEmitAt(index: Int) {
        onRemoveEmit(emitObjects.removeAt(index))
    }

    /**
     * remove emit.
     *
     * **must be called on render thread.**
     */
    fun removeEmit(emit: EmitObject) {
        emitObjects.remove(emit)
        onRemoveEmit(emit)
    }

    override fun equals(other: Any?) = other is DrawingObject && uid == other.uid

    override fun hashCode() = uid.hashCode()
}