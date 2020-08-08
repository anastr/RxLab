package com.github.anastr.rxlab.activities

import android.os.Bundle
import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.emits.ListEmit
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 8/8/2020.
 */
class BufferActivity: OperationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCode("Observable.interval(1000, TimeUnit.MILLISECONDS)\n" +
                "        .buffer(3)\n" +
                "        .subscribe();")

        val intervalOperation = TextOperation("interval", "1000 milliseconds")
        surfaceView.addDrawingObject(intervalOperation)
        val bufferOperation = FixedEmitsOperation("buffer(3)", ArrayList())
        surfaceView.addDrawingObject(bufferOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        Observable.interval(1000, TimeUnit.MILLISECONDS)
            .map { BallEmit("$it") }
            .doOnNext {
                val thread = Thread.currentThread().name
                surfaceView.action( Action(0) {
                    it.checkThread(thread)
                    addEmit(intervalOperation, it)
                    moveEmit(it, intervalOperation, bufferOperation)
                })
            }
            .buffer(3)
            .subscribe( { list ->
                val listEmit = ListEmit(list.last().position, *list.toTypedArray())
                val thread = Thread.currentThread().name
                surfaceView.action(Action(500) {
                    listEmit.checkThread(thread)
                    doOnRenderThread {
                        list.forEach { bufferOperation.removeEmit(it) }
                    }
                    addEmit(bufferOperation, listEmit)
                    moveEmit(listEmit, bufferOperation, observerObject)
                })
            }, errorHandler)
            .disposeOnDestroy()
    }
}