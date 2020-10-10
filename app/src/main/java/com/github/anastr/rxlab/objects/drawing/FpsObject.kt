package com.github.anastr.rxlab.objects.drawing

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.github.anastr.rxlab.util.ColorUtil
import com.github.anastr.rxlab.util.Point
import com.github.anastr.rxlab.util.dpToPx
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.processors.PublishProcessor
import java.util.concurrent.TimeUnit

class FpsObject: DrawingObject(dpToPx(16f)) {

    private var lastAverageFPS = 50L

    private val fpsPublisher: PublishProcessor<Long> = PublishProcessor.create<Long>()
    private val disposable: Disposable

    init {
        textPaint.color = Color.GRAY
        textPaint.textSize = dpToPx(11f)
        textPaint.textAlign = Paint.Align.CENTER

        disposable = fpsPublisher
            .buffer(Flowable.interval(1000, TimeUnit.MILLISECONDS))
            .map { it.average().toLong() }
            .subscribe {
                lastAverageFPS = it
                textPaint.color = when(lastAverageFPS) {
                    in 0..15 -> ColorUtil.red
                    in 15..25 -> ColorUtil.darkYellow
                    in 25..35 -> ColorUtil.blue
                    else -> ColorUtil.green
                }
            }
    }

    override fun getInsertPoint(): Point = Point(0f, 0f)

    override fun draw(delta: Long, canvas: Canvas) {
        fpsPublisher.onNext(1000/delta)
        canvas.drawText("$lastAverageFPS FPS", rect.centerX(), rect.bottom, textPaint)
    }

    fun dispose() {
        if(!disposable.isDisposed)
            disposable.dispose()
    }
}