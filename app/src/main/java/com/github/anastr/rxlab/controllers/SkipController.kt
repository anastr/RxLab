package com.github.anastr.rxlab.controllers

import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable

/**
 * Created by Anas Altair on 4/8/2020.
 */
class SkipController: OperationController() {

    override fun onCreate() {
        setCode("// emits from 1 to 5\n" +
                "Observable.range(1, 5)\n" +
                "        .skip(3)\n" +
                "        .subscribe();")

        val rangeOperation = TextOperation("range", "1, 5")
        surfaceView.addDrawingObject(rangeOperation)
        val skipOperation = TextOperation("skip", "3")
        surfaceView.addDrawingObject(skipOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.range(1, 5)
            .map { BallEmit("$it") }
            .doOnNext {
                actions.add(Action(1000) {
                    addThenMoveOnRender(it, rangeOperation, skipOperation)
                })
                if (it.value == "1" || it.value == "2" || it.value == "3") {
                    actions.add(Action(500) {
                        dropEmit(it, skipOperation)
                    })
                }
            }
            .skip(3)
            .subscribe({
                val thread = Thread.currentThread().name
                actions.add(Action(1000) {
                    it.checkThread(thread)
                    moveEmitOnRender(it, observerObject)
                })
            }, errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            })
            .disposeOnDestroy()
    }
}