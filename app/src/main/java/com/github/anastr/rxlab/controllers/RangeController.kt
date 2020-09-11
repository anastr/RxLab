package com.github.anastr.rxlab.controllers

import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable

/**
 * Created by Anas Altair on 4/1/2020.
 */
class RangeController: OperationController() {

    override fun onCreate() {
        setCode("// start from 3\n" +
                "// count 5\n" +
                "Observable.range(3, 5)\n" +
                "        .subscribe();")

        val rangeOperation = TextOperation("range", "3, 5")
        surfaceView.addDrawingObject(rangeOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.range(3, 5)
            .subscribe({
                val thread = Thread.currentThread().name
                actions.add(Action(1000) {
                    val emit = BallEmit("$it")
                    emit.checkThread(thread)
                    addThenMoveOnRender(emit, rangeOperation, observerObject)
                })
            }, errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            })
            .disposeOnDestroy()
    }
}