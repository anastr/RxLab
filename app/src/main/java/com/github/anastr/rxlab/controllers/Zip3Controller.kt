package com.github.anastr.rxlab.controllers

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.emits.MergedBallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.util.ColorUtil
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function3
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/17/2020.
 */
class Zip3Controller: OperationController() {

    override fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable o1 = Observable.just(\"A\", \"B\", \"C\", \"D\");\n" +
                "Observable o2 = Observable.just(\"a\", \"b\", \"c\", \"d\");\n" +
                "Observable o3 = Observable.just(1, 2, 3, 4, 5);\n" +
                "Observable.zip(o1, o2, o3, (L, l, n) -> L +\"-\" l +\"-\" + n)\n" +
                "        .subscribe();")

        val lA = BallEmit("A")
        val lB = BallEmit("B")
        val lC = BallEmit("C")
        val lD = BallEmit("D")

        val a = BallEmit("a", ColorUtil.green)
        val b = BallEmit("b", ColorUtil.green)
        val c = BallEmit("c", ColorUtil.green)
        val d = BallEmit("d", ColorUtil.green)

        val e1 = BallEmit("1", ColorUtil.blue)
        val e2 = BallEmit("2", ColorUtil.blue)
        val e3 = BallEmit("3", ColorUtil.blue)
        val e4 = BallEmit("4", ColorUtil.blue)
        val e5 = BallEmit("5", ColorUtil.blue)

        val justCapLettersOperation = FixedEmitsOperation("just", listOf(lA, lB, lC, lD))
        activity.surfaceView.addDrawingObject(justCapLettersOperation)
        val justSmallLettersOperation = FixedEmitsOperation("just", listOf(a, b, c, d))
        activity.surfaceView.addDrawingObject(justSmallLettersOperation)
        val justNumbersOperation = FixedEmitsOperation("just", listOf(e1, e2, e3, e4, e5))
        activity.surfaceView.addDrawingObject(justNumbersOperation)
        val zipOperation = FixedEmitsOperation("zip", ArrayList())
        activity.surfaceView.addDrawingObject(zipOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        val observableCapLetters = Observable.just(lA, lB, lC, lD)
        val observableSmallLetters = Observable.just(a, b, c, d)
        val observableNumbers = Observable.just(e1, e2, e3, e4, e5)

        Observable.zip(observableCapLetters, observableSmallLetters, observableNumbers
            , Function3<BallEmit, BallEmit, BallEmit, MergedBallEmit> { emit1, emit2, emit3 ->
                val mergedBallEmit = MergedBallEmit(emit2.position, emit1, emit2, emit3)
                val thread = Thread.currentThread().name
                actions.add(Action(0) { moveEmitOnRender(emit1, zipOperation) })
                actions.add(Action(1000) { moveEmitOnRender(emit2, zipOperation) })
                actions.add(Action(1000) { moveEmitOnRender(emit3, zipOperation) })
                actions.add(Action(500) {
                    mergedBallEmit.checkThread(thread)
                    doOnRenderThread {
                        zipOperation.removeEmit(emit1)
                        zipOperation.removeEmit(emit2)
                        zipOperation.removeEmit(emit3)
                    }
                    addThenMoveOnRender(mergedBallEmit, zipOperation, observerObject)
                })
                return@Function3 mergedBallEmit
            })
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, activity.errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                activity.surfaceView.actions(actions)
            })
            .disposeOnDestroy(activity)
    }
}
