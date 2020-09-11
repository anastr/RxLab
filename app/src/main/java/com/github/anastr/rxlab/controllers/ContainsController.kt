package com.github.anastr.rxlab.controllers

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable

/**
 * Created by Anas Altair on 25/7/2020.
 */
class ContainsController : OperationController() {

    override fun onCreate() {
        setCode("Observable.just(\"A\", \"B\", \"C\", \"D\")\n" +
                "        .contains(\"C\")\n" +
                "        .subscribe();")


        val a = BallEmit("A")
        val b = BallEmit("B")
        val c = BallEmit("C")
        val d = BallEmit("D")

        val justOperation = FixedEmitsOperation("just", listOf(a, b, c, d))
        surfaceView.addDrawingObject(justOperation)
        val containsOperation = TextOperation("contains", "C")
        surfaceView.addDrawingObject(containsOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.just(a, b, c, d)
            .doOnNext { actions.add(Action(1000) { moveEmitOnRender(it, containsOperation) }) }
            .doOnNext {
                if (it != c)
                    actions.add(Action(1000) { dropEmit(it, containsOperation) })
            }
            .contains(c)
            .subscribe( {
                actions.add(Action(1000) {
                    c.value = it.toString()
                    moveEmitOnRender(c, observerObject)
                })
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            }, errorHandler)
            .disposeOnDestroy()
    }
}
