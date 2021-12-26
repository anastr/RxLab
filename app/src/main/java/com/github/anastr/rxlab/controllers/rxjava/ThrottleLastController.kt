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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/10/2020.
 */
class ThrottleLastController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.<String>create(emitter -> {\n" +
                "    fab.setOnClickListener(v -> {\n" +
                "        if (!emitter.isDisposed())\n" +
                "            emitter.onNext(\"emit\");\n" +
                "    });\n" +
                "})\n" +
                "        // also called sample\n" +
                "        .throttleLast(2, TimeUnit.SECONDS)\n" +
                "        .subscribe();")

        activity.addNote("try to add emits rapidly.")

        activity.fab.visibility = View.VISIBLE

        val throttleObject = TextOperation("throttleLast", "2 sec")
        activity.surfaceView.addDrawingObject(throttleObject)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        // delay to make sure that surfaceView has created.
        Observable.just("")
            .delay(200, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { start(activity, throttleObject, observerObject) }
            .disposeOnDestroy(activity)
    }

    private fun start(activity: OperationActivity, throttleObject: TextOperation, observerObject: ObserverObject) {

        Observable.interval(0, 2000, TimeUnit.MILLISECONDS)
            .subscribe {
                activity.surfaceView.action(RenderAction(0) {
                    observerObject.lockTime()
                    observerObject.startTime(TimeObject.Lock.AFTER)
                })
            }
            .disposeOnDestroy(activity)

        Observable.create<EmitObject> { emitter ->
            activity.fab.setOnClickListener {
                if (!emitter.isDisposed)
                    emitter.onNext(
                        BallEmit(
                            "emit"
                        )
                    )
            }
        }
            .throttleLast(2000, TimeUnit.MILLISECONDS)
            .subscribe({
                val thread = Thread.currentThread().name
                activity.surfaceView.action(RenderAction(0) {
                    it.checkThread(thread)
                    addThenMove(it, throttleObject, observerObject)
                })
            }, activity.errorHandler, {
                activity.surfaceView.action(RenderAction(0) { observerObject.complete() })
            })
            .disposeOnDestroy(activity)
    }
}
