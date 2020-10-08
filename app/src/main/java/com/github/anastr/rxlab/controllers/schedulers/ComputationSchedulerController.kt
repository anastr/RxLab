package com.github.anastr.rxlab.controllers.schedulers

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_operation.*

class ComputationSchedulerController: OperationController() {

    override fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.just(\"A\")\n" +
                "        .observeOn(Schedulers.computation())\n" +
                "        .subscribe();")

        activity.addNote("used for calculating or long process.")

        val a = BallEmit("A")

        val justOperation = FixedEmitsOperation("just", listOf(a))
        activity.surfaceView.addDrawingObject(justOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.just(a)
            .observeOn(Schedulers.computation())
            .subscribe({
                val thread = Thread.currentThread().name
                actions.add(Action(1000) {
                    it.checkThread(thread)
                    moveEmitOnRender(it, observerObject)
                })
            }, activity.errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                activity.surfaceView.actions(actions)
            })
            .disposeOnDestroy(activity)
    }
}

