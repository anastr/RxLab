package com.github.anastr.rxlab.objects.time

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.RectF
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.util.ColorUtil
import com.github.anastr.rxlab.util.Point
import com.github.anastr.rxlab.util.Utils
import com.github.anastr.rxlab.util.dpToPx
import java.util.*

/**
 * add time block only on [ObserverObject].
 *
 * Created by Anas Altair on 4/10/2020.
 */
class TimeObject(leftTopPoint: Point, lock: Lock) {

    /** if false, time object width will keep increased by time. */
    var locked = false
        private set

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var rounded = 0f

    val rect = RectF(leftTopPoint.x + lock.initOffset, leftTopPoint.y
        , leftTopPoint.x + lock.initOffset, leftTopPoint.y + Utils.emitSize)

    init {
        rect.inset(0f, - dpToPx(5f))

        rounded = dpToPx(10f)
        paint.color = ColorUtil.yellow
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = dpToPx(2.5f)
        paint.pathEffect = DashPathEffect(floatArrayOf(dpToPx(5f), dpToPx(5f)), dpToPx(3f))
    }

    fun draw(canvas: Canvas) {
        canvas.drawRoundRect(rect, rounded, rounded, paint)
    }

    /**
     * stop growing.
     *
     * **safe thread.**
     */
    fun lock() {
        locked = true
    }

    private val uid: UUID = UUID.randomUUID()

    override fun equals(other: Any?) = other is TimeObject && uid == other.uid

    override fun hashCode() = uid.hashCode()

    enum class Lock(val initOffset: Float) {
        /** after the emit. */
        AFTER(Utils.emitSize),
        /** before the emit. */
        BEFORE(0f)
    }
}