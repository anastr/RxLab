package com.github.anastr.rxlab.controllers

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.emits.MergedBallEmit
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.util.ColorUtil
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/14/2020.
 */
class Zip2Controller: OperationController() {

    override fun onCreate() {
        setCode("Observable o1 = Observable.just(\"A\", \"B\", \"C\", \"D\");\n" +
                "Observable o2 = Observable.just(1, 2, 3, 4, 5);\n" +
                "Observable.zip(o1, o2, (l, n) -> l +\"-\" + n)\n" +
                "        .subscribe();")

        val a = BallEmit("A")
        val b = BallEmit("B")
        val c = BallEmit("C")
        val d = BallEmit("D")

        val e1 = BallEmit("1", ColorUtil.blue)
        val e2 = BallEmit("2", ColorUtil.blue)
        val e3 = BallEmit("3", ColorUtil.blue)
        val e4 = BallEmit("4", ColorUtil.blue)
        val e5 = BallEmit("5", ColorUtil.blue)

        val justLettersOperation = FixedEmitsOperation("just", listOf(a, b, c, d))
        surfaceView.addDrawingObject(justLettersOperation)
        val justNumbersOperation = FixedEmitsOperation("just", listOf(e1, e2, e3, e4, e5))
        surfaceView.addDrawingObject(justNumbersOperation)
        val zipOperation = FixedEmitsOperation("zip", ArrayList())
        surfaceView.addDrawingObject(zipOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        val observableLetters = Observable.just(a, b, c, d)
        val observableNumbers = Observable.just(e1, e2, e3, e4, e5)

        Observable.zip(observableLetters, observableNumbers
            , BiFunction<BallEmit, BallEmit, MergedBallEmit> { emit1, emit2 ->
                val mergedBallEmit = MergedBallEmit(emit2.position, emit1, emit2)
                val thread = Thread.currentThread().name
                actions.add(Action(0) { moveEmitOnRender(emit1, zipOperation) })
                actions.add(Action(1000) { moveEmitOnRender(emit2, zipOperation) })
                actions.add(Action(500) {
                    mergedBallEmit.checkThread(thread)
                    doOnRenderThread {
                        zipOperation.removeEmit(emit1)
                        zipOperation.removeEmit(emit2)
                    }
                    addThenMoveOnRender(mergedBallEmit, zipOperation, observerObject)
                })
                return@BiFunction mergedBallEmit
            })
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            })
            .disposeOnDestroy()
    }
}
