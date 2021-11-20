package com.github.anastr.rxlab.controllers

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.emits.MergedBallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.util.ColorUtil
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

class CombineLatestController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable o1 = Observable.interval(1500, 3000, TimeUnit.MILLISECONDS);\n" +
                "Observable o2 = Observable.interval(3000, TimeUnit.MILLISECONDS);\n" +
                "Observable.combineLatest(o1, o2, (e1, e2) -> e1 +\"-\" + e2)\n" +
                "        .subscribe();")

        val observableOperation1 = TextOperation("interval", "1500, 3000")
        activity.surfaceView.addDrawingObject(observableOperation1)
        val observableOperation2 = TextOperation("interval", "3000")
        activity.surfaceView.addDrawingObject(observableOperation2)
        val combineLatestOperation = FixedEmitsOperation("combineLatest", ArrayList())
        activity.surfaceView.addDrawingObject(combineLatestOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val observableLetters = Observable.interval(1500, 3000, TimeUnit.MILLISECONDS)
            .map { BallEmit("$it", ColorUtil.red) }
            .doOnNext {
                val thread = Thread.currentThread().name
                activity.surfaceView.action(Action(0) {
                    it.checkThread(thread)
                    if (combineLatestOperation.emitObjects.size > 1)
                        doOnRenderThread { combineLatestOperation.removeEmitAt(0) }
                    addThenMoveOnRender(it, observableOperation1, combineLatestOperation)
                })
            }
        val observableNumbers = Observable.interval(3000, TimeUnit.MILLISECONDS)
            .map { BallEmit("$it", ColorUtil.blue) }
            .doOnNext {
                val thread = Thread.currentThread().name
                activity.surfaceView.action(Action(0) {
                    it.checkThread(thread)
                    if (combineLatestOperation.emitObjects.size > 1)
                        doOnRenderThread { combineLatestOperation.removeEmitAt(0) }
                    addThenMoveOnRender(it, observableOperation2, combineLatestOperation)
                })
            }

        Observable.combineLatest(observableLetters, observableNumbers
            , BiFunction<BallEmit, BallEmit, MergedBallEmit> { emit1, emit2 ->
                val mergedBallEmit = MergedBallEmit(emit2.position, emit1, emit2)
                val thread = Thread.currentThread().name
                activity.surfaceView.action(Action(500) {
                    mergedBallEmit.checkThread(thread)
                    addThenMoveOnRender(mergedBallEmit, combineLatestOperation, observerObject)
                })
                return@BiFunction mergedBallEmit
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, activity.errorHandler)
            .disposeOnDestroy(activity)
    }
}
