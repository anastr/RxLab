package com.github.anastr.rxlab.controllers

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable

/**
 * Created by Anas Altair on 4/6/2020.
 */
class MapController : OperationController() {

    override fun onCreate() {
        setCode("Observable.just(\"Dr.\", \"Anas\", \"Altair\")\n" +
                "        .map(s -> s.length())\n" +
                "        .subscribe();")


        val a = BallEmit("Dr.")
        val b = BallEmit("Anas")
        val c = BallEmit("Altair")

        val justOperation = FixedEmitsOperation("just", listOf(a, b, c))
        surfaceView.addDrawingObject(justOperation)
        val mapOperation = TextOperation("map", "by length")
        surfaceView.addDrawingObject(mapOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.just(a, b, c)
            .doOnNext { actions.add(Action(1000) { moveEmitOnRender(it, mapOperation) }) }
            .subscribe({
                actions.add(Action(1000) {
                    it.value = it.value.length.toString()
                    moveEmitOnRender(it, observerObject)
                })
            }, errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            })
            .disposeOnDestroy()
    }
}
