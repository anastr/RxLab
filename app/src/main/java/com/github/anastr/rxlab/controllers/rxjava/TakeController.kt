package com.github.anastr.rxlab.controllers.rxjava

import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/6/2020.
 */
class TakeController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.interval(1000, TimeUnit.MILLISECONDS)\n" +
                "        .take(3)\n" +
                "        .subscribe();")

        val intervalOperation = TextOperation("interval", "1000 milliseconds (take 3)")
        activity.surfaceView.addDrawingObject(intervalOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.interval(1, TimeUnit.MILLISECONDS)
            .take(3)
            .subscribe({
                val thread = Thread.currentThread().name
                actions.add(Action(1000) {
                    val emit =
                        BallEmit("${(it + 1)} sec")
                    emit.checkThread(thread)
                    addThenMoveOnRender(emit, intervalOperation, observerObject)
                })
            }, activity.errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                activity.surfaceView.actions(actions)
            })
            .disposeOnDestroy(activity)
    }
}