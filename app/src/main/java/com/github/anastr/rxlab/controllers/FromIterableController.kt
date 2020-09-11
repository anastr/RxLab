package com.github.anastr.rxlab.controllers

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable

/**
 * Created by Anas Altair on 4/1/2020.
 */
class FromIterableController: OperationController() {

    override fun onCreate() {
        setCode("List list = Arrays.asList(\"L\", \"i\", \"s\", \"t\");\n" +
                "Observable.fromIterable(list)\n" +
                "        .subscribe();")


        val list = listOf(
            BallEmit("L"),
            BallEmit("i"),
            BallEmit("s"),
            BallEmit("t")
        )

        val fromIterableOperation = FixedEmitsOperation("fromIterable", list)
        surfaceView.addDrawingObject(fromIterableOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.fromIterable(list)
            .subscribe({
                actions.add(Action(1000) { moveEmitOnRender(it, observerObject) })
            }, errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            })
            .disposeOnDestroy()
    }

}