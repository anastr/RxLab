package com.github.anastr.rxlab.controllers.rxjava

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.RenderAction
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*

/**
 * Created by Anas Altair on 4/1/2020.
 */
class FromIterableController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("List list = Arrays.asList(\"L\", \"i\", \"s\", \"t\");\n" +
                "Observable.fromIterable(list)\n" +
                "        .subscribe();")


        val list = listOf(
            BallEmit("L"),
            BallEmit("i"),
            BallEmit("s"),
            BallEmit("t")
        )

        val fromIterableOperation = FixedEmitsOperation("fromIterable", list)
        activity.surfaceView.addDrawingObject(fromIterableOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<RenderAction>()

        Observable.fromIterable(list)
            .subscribe({
                actions.add(RenderAction(1000) { moveEmit(it, observerObject) })
            }, activity.errorHandler, {
                actions.add(RenderAction(0) { observerObject.complete() })
                activity.surfaceView.actions(actions)
            })
            .disposeOnDestroy(activity)
    }

}