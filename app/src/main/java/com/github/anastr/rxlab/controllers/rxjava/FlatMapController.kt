package com.github.anastr.rxlab.controllers.rxjava

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.RenderAction
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/2/2020.
 */
class FlatMapController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.just(\"A,B,C\", \"D,E,F\")\n" +
                "        .flatMap(s -> Observable.fromArray(s.split(\",\")))\n" +
                "        .subscribe();")

        val abcEmit = BallEmit("A,B,C")
        val defEmit = BallEmit("D,E,F")

        val justOperation = FixedEmitsOperation("just", listOf(abcEmit, defEmit))
        activity.surfaceView.addDrawingObject(justOperation)
        val flatMapOperation = TextOperation("flatMap", "")
        activity.surfaceView.addDrawingObject(flatMapOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<RenderAction>()

        Observable.just(abcEmit, defEmit)
            .delay(1500, TimeUnit.MILLISECONDS)
            // delay change thread to computation, so we changed it back to main thread.
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                val thread = Thread.currentThread().name
                actions.add(RenderAction(0) {
                    it.checkThread(thread)
                    moveEmit(it, flatMapOperation)
                })
                Observable.fromIterable(it.value.split(','))
                    .doOnComplete { actions.add(RenderAction(0) { flatMapOperation.removeEmit(it) }) }
            }
            .map { BallEmit(it.toString()) }
            .subscribe({
                val thread = Thread.currentThread().name
                actions.add(RenderAction(1000) {
                    it.checkThread(thread)
                    addThenMove(it, flatMapOperation, observerObject)
                })
            }, activity.errorHandler, {
                actions.add(RenderAction(0) { observerObject.complete() })
                activity.surfaceView.actions(actions)
            })
            .disposeOnDestroy(activity)
    }
}