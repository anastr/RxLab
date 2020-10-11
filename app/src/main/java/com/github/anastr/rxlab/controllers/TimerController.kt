package com.github.anastr.rxlab.controllers

import android.os.Handler
import androidx.core.os.postDelayed
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.time.TimeObject
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 10/10/2020.
 */
class TimerController: OperationController() {

    override fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.timer(3, TimeUnit.SECONDS)\n" +
                "        .subscribe();")

        val timerOperation = TextOperation("timer", "3 seconds")
        activity.surfaceView.addDrawingObject(timerOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        Handler().postDelayed(500) {
            activity.surfaceView.startTimeOnRender(observerObject, TimeObject.Lock.AFTER)
            Observable.timer(3, TimeUnit.SECONDS)
                .subscribe({
                    val thread = Thread.currentThread().name
                    activity.surfaceView.action(Action(0) {
                        val emit = BallEmit("$it")
                        emit.checkThread(thread)
                        addThenMoveOnRender(emit, timerOperation, observerObject)
                    })
                }, activity.errorHandler, {
                    activity.surfaceView.action(Action(0) { doOnRenderThread { observerObject.complete() } })
                })
                .disposeOnDestroy(activity)
        }
    }
}
