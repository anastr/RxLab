package com.github.anastr.rxlab.controllers

import android.os.Handler
import androidx.core.os.postDelayed
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.time.TimeObject
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/1/2020.
 */
class IntervalController: OperationController() {

    override fun onCreate() {
        setCode("Observable.interval(2000, TimeUnit.MILLISECONDS)\n" +
                "        .subscribe();")

        val intervalOperation = TextOperation("interval", "2000 milliseconds")
        surfaceView.addDrawingObject(intervalOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        Handler().postDelayed(500) {
            surfaceView.startTimeOnRender(observerObject, TimeObject.Lock.AFTER)
            Observable.interval(2000, TimeUnit.MILLISECONDS)
                .subscribe {
                    val thread = Thread.currentThread().name
                    surfaceView.action( Action(0) {
                        val emit = BallEmit("${(it + 1) * 2} sec")
                        emit.checkThread(thread)
                        doOnRenderThread {
                            intervalOperation.addEmit(emit)
                            observerObject.startTime(TimeObject.Lock.AFTER)
                            moveEmit(emit, observerObject)
                        }
                    })
                }
                .disposeOnDestroy()
        }
    }
}