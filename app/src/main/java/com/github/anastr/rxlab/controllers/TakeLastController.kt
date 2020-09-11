package com.github.anastr.rxlab.controllers

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable

/**
 * Created by Anas Altair on 4/13/2020.
 */
class TakeLastController: OperationController() {

    override fun onCreate() {
        setCode("Observable.just(1, 2, 3, 4, 5)\n" +
                "        .takeLast(3)\n" +
                "        .subscribe();")

        addNote("be aware that 'takeLast(int)' method will collect all emits in queue " +
                "and then will emit the last specified number of emits.")

        val e1 = BallEmit("1")
        val e2 = BallEmit("2")
        val e3 = BallEmit("3")
        val e4 = BallEmit("4")
        val e5 = BallEmit("5")

        val justOperation = FixedEmitsOperation("just", listOf(e1, e2, e3, e4, e5))
        surfaceView.addDrawingObject(justOperation)
        val takeLastOperation = FixedEmitsOperation("takeLast", ArrayList())
        surfaceView.addDrawingObject(takeLastOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.just(e1, e2, e3, e4, e5)
            .doOnNext { actions.add(Action(1000) { moveEmitOnRender(it, takeLastOperation) }) }
            .takeLast(3)
            .subscribe({
                actions.add(Action(1000) { moveEmitOnRender(it, observerObject) })
            }, errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            })
            .disposeOnDestroy()
    }
}
