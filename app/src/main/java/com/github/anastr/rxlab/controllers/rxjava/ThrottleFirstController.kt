package com.github.anastr.rxlab.controllers.rxjava

import android.view.View
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.emits.EmitObject
import com.github.anastr.rxlab.objects.time.TimeObject
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.RenderAction
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/10/2020.
 */
class ThrottleFirstController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.<String>create(emitter -> {\n" +
                "    fab.setOnClickListener(v -> {\n" +
                "        if (!emitter.isDisposed())\n" +
                "            emitter.onNext(\"emit\");\n" +
                "    });\n" +
                "})\n" +
                "        .throttleFirst(2, TimeUnit.SECONDS)\n" +
                "        .subscribe();")

        activity.addNote("try to add emits rapidly.")

        activity.fab.visibility = View.VISIBLE

        val throttleObject = TextOperation("throttleFirst", "2 sec")
        activity.surfaceView.addDrawingObject(throttleObject)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        Observable.create<EmitObject> { emitter ->
            activity.fab.setOnClickListener {
                if (!emitter.isDisposed)
                    emitter.onNext(BallEmit("emit"))
            }
        }
            .throttleFirst(2, TimeUnit.SECONDS)
            .subscribe({
                val thread = Thread.currentThread().name
                activity.surfaceView.action(RenderAction(0) {
                    it.checkThread(thread)
                    observerObject.startTime(TimeObject.Lock.BEFORE)
                    action(RenderAction(2000) { observerObject.lockTime() })
                    addThenMove(it, throttleObject, observerObject)
                })
            }, activity.errorHandler, {
                activity.surfaceView.action(RenderAction(0) { observerObject.complete() })
            })
            .disposeOnDestroy(activity)
    }
}
