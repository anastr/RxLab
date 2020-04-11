package com.github.anastr.rxlab.objects.emits

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import com.github.anastr.rxlab.util.ColorUtil
import com.github.anastr.rxlab.util.Point
import com.github.anastr.rxlab.util.Utils
import java.util.*

/**
 * Created by Anas Altair on 3/7/2020.
 */
abstract class EmitObject(leftTopPoint: Point) {

    private val uid: UUID = UUID.randomUUID()

    private var threadNumber: String = ""
    private val threadPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val threadTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    val rect = RectF(leftTopPoint.x, leftTopPoint.y
        , leftTopPoint.x + Utils.emitSize, leftTopPoint.y + Utils.emitSize)

    val position
        get() = Point(rect.left, rect.top)

    init {
        threadPaint.color = Color.TRANSPARENT
        threadTextPaint.textSize = Utils.threadTextSize
        threadTextPaint.textAlign = Paint.Align.CENTER
        threadTextPaint.color = Color.BLACK

        checkThread(Thread.currentThread().name)
    }

    abstract fun drawContent(canvas: Canvas)

    fun draw(canvas: Canvas) {
        drawContent(canvas)
        canvas.drawCircle(rect.left + Utils.threadSize * .5f, rect.top + Utils.threadSize * .5f
            , Utils.threadSize * .5f, threadPaint)
        canvas.drawText(threadNumber, rect.left + Utils.threadSize * .5f, rect.top + Utils.threadSize * .8f, threadTextPaint)
    }

    fun offsetTo(leftTopPoint: Point) = rect.offsetTo(leftTopPoint.x, leftTopPoint.y)

    fun checkThread(name: String) {
        val thread = name.split('-')
        threadPaint.color = when (thread[0]) {
            "main" -> ColorUtil.mainThread
            "RxComputationThreadPool" -> ColorUtil.computationThread
            "RxCachedThreadScheduler" -> ColorUtil.ioThread
            "RxSingleScheduler" -> ColorUtil.singleThread
            else -> ColorUtil.otherThread
        }
        threadNumber = if (thread.size > 1) thread[1] else ""
    }

    override fun equals(other: Any?) = other is EmitObject && uid == other.uid

    override fun hashCode() = uid.hashCode()
}