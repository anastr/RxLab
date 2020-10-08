package com.github.anastr.rxlab.controllers

import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*

/**
 * Created by Anas Altair on 4/1/2020.
 */
class RangeController: OperationController() {

    override fun onCreate(activity: OperationActivity) {
        activity.setCode("// start from 3\n" +
                "// count 5\n" +
                "Observable.range(3, 5)\n" +
                "        .subscribe();")

        val rangeOperation = TextOperation("range", "3, 5")
        activity.surfaceView.addDrawingObject(rangeOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.range(3, 5)
            .subscribe({
                val thread = Thread.currentThread().name
                actions.add(Action(1000) {
                    val emit = BallEmit("$it")
                    emit.checkThread(thread)
                    addThenMoveOnRender(emit, rangeOperation, observerObject)
                })
            }, activity.errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                activity.surfaceView.actions(actions)
            })
            .disposeOnDestroy(activity)
    }
}