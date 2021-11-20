package com.github.anastr.rxlab.controllers

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/13/2020.
 */
class ReduceController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.just(1, 2, 3, 4, 5)\n" +
                "        .reduce((total, emit) -> total + emit)\n" +
                "        .subscribe();")

        activity.addNote("first, 'reduce' operation will receive two emits, then " +
                "it will take the result of them with next emit...")

        val a = BallEmit("1")
        val b = BallEmit("2")
        val c = BallEmit("3")
        val d = BallEmit("4")
        val e = BallEmit("5")

        val justOperation = FixedEmitsOperation("just", listOf(a, b, c, d, e))
        activity.surfaceView.addDrawingObject(justOperation)
        val reduceOperation = TextOperation("reduce", "")
        activity.surfaceView.addDrawingObject(reduceOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.just(a, b, c, d, e)
            .delay(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                if (it.value == "1")
                    actions.add(Action(0) { moveEmitOnRender(it, reduceOperation) })
            }
            .reduce { emit1: BallEmit, emit2: BallEmit ->
                val text = (emit1.value.toInt() + emit2.value.toInt()).toString()
                actions.add(Action(0) { moveEmitOnRender(emit2, reduceOperation) })
                actions.add(Action(1000) {
                    reduceOperation.setText(text)
                    if (emit1.value == "1")
                        dropEmit(emit1, reduceOperation)
                    dropEmit(emit2, reduceOperation)
                })
                BallEmit(text)
            }
            .subscribe({
                val thread = Thread.currentThread().name
                actions.add(Action(0) {
                    it.checkThread(thread)
                    addThenMoveOnRender(it, reduceOperation, observerObject)
                })
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                activity.surfaceView.actions(actions)
            }, activity.errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                activity.surfaceView.actions(actions)
            })
            .disposeOnDestroy(activity)
    }
}
