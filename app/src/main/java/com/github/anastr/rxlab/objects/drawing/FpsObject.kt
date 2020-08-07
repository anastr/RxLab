package com.github.anastr.rxlab.objects.drawing

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.github.anastr.rxlab.util.Point
import com.github.anastr.rxlab.util.dpToPx
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.PublishProcessor
import java.util.concurrent.TimeUnit

class FpsObject: DrawingObject("FPS") {

    private var lastFPS = 40L

    private val fpsPublisher: PublishProcessor<Long> = PublishProcessor.create<Long>()

    init {
        textPaint.color = Color.GRAY
        textPaint.textSize = dpToPx(11f)
        textPaint.textAlign = Paint.Align.CENTER

        fpsPublisher
            .buffer(Flowable.interval(1000, TimeUnit.MILLISECONDS))
            .map { it.average().toLong() }
            .subscribe { lastFPS = it }
    }

    override fun getInsertPoint(): Point = Point(0f, 0f)

    override fun draw(delta: Long, canvas: Canvas) {
        fpsPublisher.onNext(1000/delta)
        canvas.drawText("$lastFPS FPS", rect.centerX(), rect.centerY(), textPaint)
    }
}