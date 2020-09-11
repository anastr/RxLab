package com.github.anastr.rxlab.controllers

import android.view.View
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.emits.EmitObject
import com.github.anastr.rxlab.objects.time.TimeObject
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/10/2020.
 */
class ThrottleWithTimeoutController: OperationController() {

    override fun onCreate() {
        setCode("Observable.<String>create(emitter -> {\n" +
                "    fab.setOnClickListener(v -> {\n" +
                "        if (!emitter.isDisposed())\n" +
                "            emitter.onNext(\"emit\");\n" +
                "    });\n" +
                "})\n" +
                "        // also called debounce\n" +
                "        .throttleWithTimeout(2, TimeUnit.SECONDS)\n" +
                "        .subscribe();")

        addNote("add 1 emit and wait, then try to add emits rapidly.")
        addNote("please be aware about emit thread!")

        fab.visibility = View.VISIBLE

        val throttleObject = TextOperation("throttleWithTimeout", "2 sec")
        surfaceView.addDrawingObject(throttleObject)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        Observable.create<EmitObject> { emitter ->
            fab.setOnClickListener {
                if (!emitter.isDisposed)
                    emitter.onNext(BallEmit("emit"))
            }
        }
            .doOnNext {
                surfaceView.action(Action(0) { doOnRenderThread {
                    if (observerObject.isTimeLocked() != true)
                        observerObject.removeLastTime()
                    observerObject.startTime(TimeObject.Lock.AFTER)
                } })
            }
            .throttleWithTimeout(2, TimeUnit.SECONDS)
            .subscribe({
                val thread = Thread.currentThread().name
                surfaceView.action(Action(0) {
                    it.checkThread(thread)
                    observerObject.lockTime()
                    addThenMoveOnRender(it, throttleObject, observerObject)
                })
            }, errorHandler, {
                surfaceView.action(Action(0) { doOnRenderThread { observerObject.complete() } })
            })
            .disposeOnDestroy()
    }
}
