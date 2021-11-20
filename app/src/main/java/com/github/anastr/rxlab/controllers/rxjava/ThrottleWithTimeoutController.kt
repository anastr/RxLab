package com.github.anastr.rxlab.controllers.rxjava

import android.view.View
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.emits.EmitObject
import com.github.anastr.rxlab.objects.time.TimeObject
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/10/2020.
 */
class ThrottleWithTimeoutController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.<String>create(emitter -> {\n" +
                "    fab.setOnClickListener(v -> {\n" +
                "        if (!emitter.isDisposed())\n" +
                "            emitter.onNext(\"emit\");\n" +
                "    });\n" +
                "})\n" +
                "        // also called debounce\n" +
                "        .throttleWithTimeout(2, TimeUnit.SECONDS)\n" +
                "        .subscribe();")

        activity.addNote("add 1 emit and wait, then try to add emits rapidly.")
        activity.addNote("please be aware about emit thread!")

        activity.fab.visibility = View.VISIBLE

        val throttleObject = TextOperation("throttleWithTimeout", "2 sec")
        activity.surfaceView.addDrawingObject(throttleObject)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        Observable.create<EmitObject> { emitter ->
            activity.fab.setOnClickListener {
                if (!emitter.isDisposed)
                    emitter.onNext(BallEmit("emit"))
            }
        }
            .doOnNext {
                activity.surfaceView.action(Action(0) { doOnRenderThread {
                    if (observerObject.isTimeLocked() != true)
                        observerObject.removeLastTime()
                    observerObject.startTime(TimeObject.Lock.AFTER)
                } })
            }
            .throttleWithTimeout(2, TimeUnit.SECONDS)
            .subscribe({
                val thread = Thread.currentThread().name
                activity.surfaceView.action(Action(0) {
                    it.checkThread(thread)
                    observerObject.lockTime()
                    addThenMoveOnRender(it, throttleObject, observerObject)
                })
            }, activity.errorHandler, {
                activity.surfaceView.action(Action(0) { doOnRenderThread { observerObject.complete() } })
            })
            .disposeOnDestroy(activity)
    }
}
