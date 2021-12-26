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
 * Created by Anas Altair on 4/7/2020.
 */
class SortedController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.just(3, 2, 5, 1, 4)\n" +
                "        .sorted()\n" +
                "        .subscribe();")

        activity.addNote("be aware that 'sorted()' method will collect all emits in queue " +
                "and then will emit them again, you may have OutOfMemoryException.")

        val e1 = BallEmit("1")
        val e2 = BallEmit("2")
        val e3 = BallEmit("3")
        val e4 = BallEmit("4")
        val e5 = BallEmit("5")

        val justOperation = FixedEmitsOperation("just", listOf(e3, e2, e5, e1, e4))
        activity.surfaceView.addDrawingObject(justOperation)
        val sortedOperation = FixedEmitsOperation("sorted", ArrayList())
        activity.surfaceView.addDrawingObject(sortedOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<RenderAction>()

        Observable.just(e3, e2, e5, e1, e4)
            .doOnNext { actions.add(RenderAction(1000) { moveEmit(it, sortedOperation) }) }
            .sorted { emit1, emit2 -> emit1.value.toInt().compareTo(emit2.value.toInt()) }
            .subscribe({
                actions.add(RenderAction(1000) { moveEmit(it, observerObject) })
            }, activity.errorHandler, {
                actions.add(RenderAction(0) { observerObject.complete() })
                activity.surfaceView.actions(actions)
            })
            .disposeOnDestroy(activity)
    }
}
