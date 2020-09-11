package com.github.anastr.rxlab.controllers

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.util.ColorUtil
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable

/**
 * Created by Anas Altair on 4/15/2020.
 */
class MergeController: OperationController() {

    override fun onCreate() {
        setCode("Observable o1 = Observable.just(\"A\", \"B\", \"C\", \"D\");\n" +
                "Observable o2 = Observable.just(\"a\", \"b\", \"c\", \"d\");\n" +
                "Observable.merge(o1, o2)\n" +
                "        .subscribe();")

        val lA = BallEmit("A")
        val lB = BallEmit("B")
        val lC = BallEmit("C")
        val lD = BallEmit("D")

        val a = BallEmit("a", ColorUtil.green)
        val b = BallEmit("b", ColorUtil.green)
        val c = BallEmit("c", ColorUtil.green)
        val d = BallEmit("d", ColorUtil.green)

        val justCapLettersOperation = FixedEmitsOperation("just", listOf(lA, lB, lC, lD))
        surfaceView.addDrawingObject(justCapLettersOperation)
        val justSmallLettersOperation = FixedEmitsOperation("just", listOf(a, b, c, d))
        surfaceView.addDrawingObject(justSmallLettersOperation)
        val mergeOperation = FixedEmitsOperation("merge", ArrayList())
        surfaceView.addDrawingObject(mergeOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        val observableLetters = Observable.just(lA, lB, lC, lD)
            .doOnNext {
                actions.add(Action(500) { moveEmitOnRender(it, mergeOperation) })
            }
        val observableNumbers = Observable.just(a, b, c, d)
            .doOnNext {
                actions.add(Action(500) { moveEmitOnRender(it, mergeOperation) })
            }

        Observable.merge(observableLetters, observableNumbers)
            .subscribe({
                val thread = Thread.currentThread().name
                actions.add(Action(500) {
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
