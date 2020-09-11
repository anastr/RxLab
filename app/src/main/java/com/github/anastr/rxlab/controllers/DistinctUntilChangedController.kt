package com.github.anastr.rxlab.controllers

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/8/2020.
 */
class DistinctUntilChangedController: OperationController() {

    override fun onCreate() {
        setCode("Observable.just(1, 1, 3, 2, 2, 3, 1)\n" +
                "        .distinctUntilChanged()\n" +
                "        .subscribe();")


        val list = listOf(
            BallEmit("1"),
            BallEmit("1"),
            BallEmit("3"),
            BallEmit("2")
            ,
            BallEmit("2"),
            BallEmit("3"),
            BallEmit("1")
        )

        val fromIterableOperation = FixedEmitsOperation("just", list)
        surfaceView.addDrawingObject(fromIterableOperation)
        val distinctOperation = TextOperation("distinctUntilChanged", "")
        surfaceView.addDrawingObject(distinctOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()


        Observable.fromIterable(list)
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                actions.add(Action(500) { moveEmitOnRender(it, distinctOperation) })
            }
            .distinctUntilChanged { e1, e2 ->
                if (e1.value == e2.value) {
                    actions.add(Action(500) { dropEmit(e2, distinctOperation) })
                    return@distinctUntilChanged true
                }
                return@distinctUntilChanged false
            }
            .subscribe({
                actions.add(Action(500) { moveEmitOnRender(it, observerObject) })
            }, errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            })
            .disposeOnDestroy()
    }

}