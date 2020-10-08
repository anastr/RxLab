package com.github.anastr.rxlab.controllers

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*

/**
 * Created by Anas Altair on 4/13/2020.
 */
class TakeLastController: OperationController() {

    override fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.just(1, 2, 3, 4, 5)\n" +
                "        .takeLast(3)\n" +
                "        .subscribe();")

        activity.addNote("be aware that 'takeLast(int)' method will collect all emits in queue " +
                "and then will emit the last specified number of emits.")

        val e1 = BallEmit("1")
        val e2 = BallEmit("2")
        val e3 = BallEmit("3")
        val e4 = BallEmit("4")
        val e5 = BallEmit("5")

        val justOperation = FixedEmitsOperation("just", listOf(e1, e2, e3, e4, e5))
        activity.surfaceView.addDrawingObject(justOperation)
        val takeLastOperation = FixedEmitsOperation("takeLast", ArrayList())
        activity.surfaceView.addDrawingObject(takeLastOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.just(e1, e2, e3, e4, e5)
            .doOnNext { actions.add(Action(1000) { moveEmitOnRender(it, takeLastOperation) }) }
            .takeLast(3)
            .subscribe({
                actions.add(Action(1000) { moveEmitOnRender(it, observerObject) })
            }, activity.errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                activity.surfaceView.actions(actions)
            })
            .disposeOnDestroy(activity)
    }
}
