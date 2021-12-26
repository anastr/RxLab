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
 * Created by Anas Altair on 25/7/2020.
 */
class ContainsController : OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.just(\"A\", \"B\", \"C\", \"D\")\n" +
                "        .contains(\"C\")\n" +
                "        .subscribe();")


        val a = BallEmit("A")
        val b = BallEmit("B")
        val c = BallEmit("C")
        val d = BallEmit("D")

        val justOperation = FixedEmitsOperation("just", listOf(a, b, c, d))
        activity.surfaceView.addDrawingObject(justOperation)
        val containsOperation = TextOperation("contains", "C")
        activity.surfaceView.addDrawingObject(containsOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<RenderAction>()

        Observable.just(a, b, c, d)
            .doOnNext { actions.add(RenderAction(1000) { moveEmit(it, containsOperation) }) }
            .doOnNext {
                if (it != c)
                    actions.add(RenderAction(1000) { dropEmit(it, containsOperation) })
            }
            .contains(c)
            .subscribe( {
                actions.add(RenderAction(1000) {
                    c.value = it.toString()
                    moveEmit(c, observerObject)
                })
                actions.add(RenderAction(0) {  observerObject.complete() })
                activity.surfaceView.actions(actions)
            }, activity.errorHandler)
            .disposeOnDestroy(activity)
    }
}
