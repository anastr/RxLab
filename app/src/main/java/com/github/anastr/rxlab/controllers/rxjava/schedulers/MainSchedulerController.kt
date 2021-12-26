package com.github.anastr.rxlab.controllers.rxjava.schedulers

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.RenderAction
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*

class MainSchedulerController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.just(\"A\")\n" +
                "        .observeOn(AndroidSchedulers.mainThread())\n" +
                "        .subscribe();")

        val a = BallEmit("A")

        val justOperation = FixedEmitsOperation("just", listOf(a))
        activity.surfaceView.addDrawingObject(justOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<RenderAction>()

        Observable.just(a)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val thread = Thread.currentThread().name
                actions.add(RenderAction(1000) {
                    it.checkThread(thread)
                    moveEmit(it, observerObject)
                })
            }, activity.errorHandler, {
                actions.add(RenderAction(0) { observerObject.complete() })
                activity.surfaceView.actions(actions)
            })
            .disposeOnDestroy(activity)
    }
}
