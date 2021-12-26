package com.github.anastr.rxlab.controllers.rxjava

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.RenderAction
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*

/**
 * Created by Anas Altair on 4/6/2020.
 */
class MapController : OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.just(\"Dr.\", \"Anas\", \"Altair\")\n" +
                "        .map(s -> s.length())\n" +
                "        .subscribe();")


        val a = BallEmit("Dr.")
        val b = BallEmit("Anas")
        val c = BallEmit("Altair")

        val justOperation = FixedEmitsOperation("just", listOf(a, b, c))
        activity.surfaceView.addDrawingObject(justOperation)
        val mapOperation = TextOperation("map", "by length")
        activity.surfaceView.addDrawingObject(mapOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<RenderAction>()

        Observable.just(a, b, c)
            .doOnNext { actions.add(RenderAction(1000) { moveEmit(it, mapOperation) }) }
            .subscribe({
                actions.add(RenderAction(1000) {
                    it.value = it.value.length.toString()
                    moveEmit(it, observerObject)
                })
            }, activity.errorHandler, {
                actions.add(RenderAction(0) { observerObject.complete() })
                activity.surfaceView.actions(actions)
            })
            .disposeOnDestroy(activity)
    }
}
