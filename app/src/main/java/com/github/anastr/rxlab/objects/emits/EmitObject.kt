package com.github.anastr.rxlab.objects.emits

import android.graphics.*
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

    var value: String = ""
    protected var valueTextHeight: Float

    protected val emitPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    protected val valuePaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    val rect = RectF(leftTopPoint.x, leftTopPoint.y
        , leftTopPoint.x + Utils.emitSize, leftTopPoint.y + Utils.emitSize)

    val position
        get() = Point(rect.left, rect.top)

    init {
        threadPaint.color = Color.TRANSPARENT
        threadTextPaint.textSize = Utils.threadTextSize
        threadTextPaint.textAlign = Paint.Align.CENTER
        threadTextPaint.color = Color.BLACK

        valuePaint.textSize = Utils.emitTextSize
        valuePaint.typeface = Typeface.DEFAULT_BOLD
        valuePaint.color = Color.WHITE
        valuePaint.textAlign = Paint.Align.CENTER
        val valueBounds = Rect()
        valuePaint.getTextBounds("10", 0, 1, valueBounds)
        valueTextHeight = valueBounds.height().toFloat()

        checkThread(Thread.currentThread().name)
    }

    var color
        get() = emitPaint.color
        set(value) { emitPaint.color = value }

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