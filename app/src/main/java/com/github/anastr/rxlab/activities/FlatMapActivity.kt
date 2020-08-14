package com.github.anastr.rxlab.activities

import android.os.Bundle
import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/2/2020.
 */
class FlatMapActivity: OperationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCode("Observable.just(\"A,B,C\", \"D,E,F\")\n" +
                "        .flatMap(s -> Observable.fromArray(s.split(\",\")))\n" +
                "        .subscribe();")

        val abcEmit = BallEmit("A,B,C")
        val defEmit = BallEmit("D,E,F")

        val justOperation = FixedEmitsOperation("just", listOf(abcEmit, defEmit))
        surfaceView.addDrawingObject(justOperation)
        val flatMapOperation = TextOperation("flatMap", "")
        surfaceView.addDrawingObject(flatMapOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.just(abcEmit, defEmit)
            .delay(1500, TimeUnit.MILLISECONDS)
            // delay change thread to computation, so we changed it back to main thread.
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                val thread = Thread.currentThread().name
                actions.add(Action(0) {
                    it.checkThread(thread)
                    moveEmitOnRender(it, flatMapOperation)
                })
                Observable.fromIterable(it.value.split(','))
                    .doOnComplete { actions.add(Action(0) { doOnRenderThread { flatMapOperation.removeEmit(it) } }) }
            }
            .map { BallEmit(it.toString()) }
            .subscribe({
                val thread = Thread.currentThread().name
                actions.add(Action(1000) {
                    it.checkThread(thread)
                    addThenMoveOnRender(it, flatMapOperation, observerObject)
                })
            }, errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            })
            .disposeOnDestroy()
    }
}