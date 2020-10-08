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
 * Created by Anas Altair on 4/2/2020.
 */
class ConcatMapController: OperationController() {

    override fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.just(\"A,B,C\", \"D,E,F\")\n" +
                "        .concatMap(s -> Observable.fromArray(s.split(\",\")))\n" +
                "        .subscribe();")

        val abcEmit = BallEmit("A,B,C")
        val defEmit = BallEmit("D,E,F")

        val justOperation = FixedEmitsOperation("just", listOf(abcEmit, defEmit))
        activity.surfaceView.addDrawingObject(justOperation)
        val concatMapOperation = TextOperation("concatMap", "")
        activity.surfaceView.addDrawingObject(concatMapOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.just(abcEmit, defEmit)
            .delay(1500, TimeUnit.MILLISECONDS)
            // delay change thread to computation, so we changed it back to main thread.
            .observeOn(AndroidSchedulers.mainThread())
            .concatMap {
                val thread = Thread.currentThread().name
                actions.add(Action(0) {
                    it.checkThread(thread)
                    moveEmitOnRender(it, concatMapOperation)
                })
                Observable.fromIterable(it.value.split(','))
                    .doOnComplete { actions.add(Action(0) { doOnRenderThread { concatMapOperation.removeEmit(it) } }) }
            }
            .map { BallEmit(it.toString()) }
            .subscribe({
                val thread = Thread.currentThread().name
                actions.add(Action(1000) {
                    it.checkThread(thread)
                    addThenMoveOnRender(it, concatMapOperation, observerObject)
                })
            }, activity.errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                activity.surfaceView.actions(actions)
            })
            .disposeOnDestroy(activity)
    }
}