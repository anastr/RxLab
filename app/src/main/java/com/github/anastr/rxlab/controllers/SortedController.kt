package com.github.anastr.rxlab.controllers

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable

/**
 * Created by Anas Altair on 4/7/2020.
 */
class SortedController: OperationController() {

    override fun onCreate() {
        setCode("Observable.just(3, 2, 5, 1, 4)\n" +
                "        .sorted()\n" +
                "        .subscribe();")

        addNote("be aware that 'sorted()' method will collect all emits in queue " +
                "and then will emit them again, you may have OutOfMemoryException.")

        val e1 = BallEmit("1")
        val e2 = BallEmit("2")
        val e3 = BallEmit("3")
        val e4 = BallEmit("4")
        val e5 = BallEmit("5")

        val justOperation = FixedEmitsOperation("just", listOf(e3, e2, e5, e1, e4))
        surfaceView.addDrawingObject(justOperation)
        val sortedOperation = FixedEmitsOperation("sorted", ArrayList())
        surfaceView.addDrawingObject(sortedOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.just(e3, e2, e5, e1, e4)
            .doOnNext { actions.add(Action(1000) { moveEmitOnRender(it, sortedOperation) }) }
            .sorted { emit1, emit2 -> emit1.value.toInt().compareTo(emit2.value.toInt()) }
            .subscribe({
                actions.add(Action(1000) { moveEmitOnRender(it, observerObject) })
            }, errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            })
            .disposeOnDestroy()
    }
}
