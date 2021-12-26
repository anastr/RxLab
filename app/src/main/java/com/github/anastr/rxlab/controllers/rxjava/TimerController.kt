package com.github.anastr.rxlab.controllers.rxjava

import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.time.TimeObject
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.RenderAction
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 10/10/2020.
 */
class TimerController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.timer(3, TimeUnit.SECONDS)\n" +
                "        .subscribe();")

        val timerOperation = TextOperation("timer", "3 seconds")
        activity.surfaceView.addDrawingObject(timerOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        delay(500)
        activity.surfaceView.startTimeOnRender(observerObject, TimeObject.Lock.AFTER)
        Observable.timer(3, TimeUnit.SECONDS)
            .subscribe({
                val thread = Thread.currentThread().name
                activity.surfaceView.action(RenderAction(0) {
                    val emit = BallEmit("$it")
                    emit.checkThread(thread)
                    addThenMove(emit, timerOperation, observerObject)
                })
            }, activity.errorHandler, {
                activity.surfaceView.action(RenderAction(0) { observerObject.complete() })
            })
            .disposeOnDestroy(activity)
    }
}
