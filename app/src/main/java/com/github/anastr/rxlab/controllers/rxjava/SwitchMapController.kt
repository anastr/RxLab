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
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/11/2020.
 */
class SwitchMapController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.just(\"A,B,C\", \"D,E,F\", \"G,H,I\")\n" +
                "    .switchMap(s -> Observable.fromArray(s.split(\",\"))\n" +
                "            .subscribeOn(Schedulers.computation())\n" +
                "            .map(c -> tackTime(c)))\n" +
                "    .subscribe();")

        activity.addNote("if you haven't changed the thread you will get " +
                "the same result when using 'concatMap'.")

        val abcEmit = BallEmit("A,B,C")
        val defEmit = BallEmit("D,E,F")
        val ghiEmit = BallEmit("G,H,I")

        val justOperation = FixedEmitsOperation("just", listOf(abcEmit, defEmit, ghiEmit))
        activity.surfaceView.addDrawingObject(justOperation)
        val switchMapOperation = TextOperation("switchMap", "")
        activity.surfaceView.addDrawingObject(switchMapOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<RenderAction>()

        Observable.just(abcEmit, defEmit, ghiEmit)
            .delay(1500, TimeUnit.MILLISECONDS)
            // delay change thread to computation, so we changed it back to main thread.
            .observeOn(AndroidSchedulers.mainThread())
            .switchMap {
                val thread = Thread.currentThread().name
                actions.add(RenderAction(0) {
                    it.checkThread(thread)
                    moveEmit(it, switchMapOperation)
                })
                Observable.fromIterable(it.value.split(','))
                    .subscribeOn(Schedulers.computation())
                    .delay(10, TimeUnit.MILLISECONDS)
                    .doOnDispose { actions.add(RenderAction(1000) { dropEmit(it, switchMapOperation) }) }
                    .doOnComplete { actions.add(RenderAction(0) { switchMapOperation.removeEmit(it) }) }
            }
            .map { BallEmit(it.toString()) }
            .subscribe({
                val thread = Thread.currentThread().name
                actions.add(RenderAction(1000) {
                    it.checkThread(thread)
                    addThenMove(it, switchMapOperation, observerObject)
                })
            }, activity.errorHandler, {
                actions.add(RenderAction(0) { observerObject.complete() })
                activity.surfaceView.actions(actions)
            })
            .disposeOnDestroy(activity)
    }
}