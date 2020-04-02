package com.github.anastr.rxlab.objects.drawing

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import com.github.anastr.rxlab.objects.EmitObject
import com.github.anastr.rxlab.util.Point
import com.github.anastr.rxlab.util.Utils
import com.github.anastr.rxlab.util.doOnMainThread
import com.github.anastr.rxlab.util.dpToPx
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Anas Altair on 3/20/2020.
 */
abstract class DrawingObject(val name: String) {

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
    protected open fun onEmitsChanged() { }
    protected open fun onAddEmits(emitObject: EmitObject) { }

    /**
     *
     * **must be called on main thread.**
     */
    fun moveEmit(emit: EmitObject, to: Point, onEnd: () -> Any = {}) {
        val distanceX = to.x - emit.rect.left
        val distanceY = to.y - emit.rect.top
        var pre = 0f
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                emit.rect.offset(distanceX*(animation.animatedFraction - pre)
                    , distanceY*(animation.animatedFraction - pre))
                pre = animation.animatedFraction
            }
            doOnEnd { onEnd.invoke() }
        }.start()
    }

    /**
     * add emit immediately to `insertPoint`.
     *
     * **must be called on render thread.**
     */
    fun addEmit(emit: EmitObject) {
        addEmit(emit, getInsertPoint())
        onEmitsChanged()
    }

    /**
     * add emit without change its position, then move it
     * to `insertPoint` with animation.
     *
     * **must be called on render thread.**
     */
    fun addEmitWithAnimation(emit: EmitObject) {
        addEmit(emit, emit.position)
        onEmitsChanged()
        doOnMainThread {
            moveEmit(emit, getInsertPoint())
        }
    }

    /**
     * add list of emits immediately to `insertPoint`.
     *
     * **must be called on render thread.**
     */
    fun addEmits(emits: List<EmitObject>) {
        emits.forEach { addEmit(it, getInsertPoint()) }
        onEmitsChanged()
    }

    private fun addEmit(emit: EmitObject, initial: Point) {
        emit.offsetTo(initial)
        emitObjects.add(emit)
        onAddEmits(emit)
    }

    /**
     * remove emit by index.
     *
     * **must be called on render thread.**
     */
    fun removeEmitAt(index: Int) {
        emitObjects.removeAt(index)
        onEmitsChanged()
    }

    /**
     * remove emit.
     *
     * **must be called on render thread.**
     */
    fun removeEmit(emit: EmitObject) {
        emitObjects.remove(emit)
        onEmitsChanged()
    }

    override fun equals(other: Any?) = other is DrawingObject && uid == other.uid

    override fun hashCode() = uid.hashCode()
}