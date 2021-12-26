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
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/8/2020.
 */
class DistinctUntilChangedController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.just(1, 1, 3, 2, 2, 3, 1)\n" +
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
        activity.surfaceView.addDrawingObject(fromIterableOperation)
        val distinctOperation = TextOperation("distinctUntilChanged", "")
        activity.surfaceView.addDrawingObject(distinctOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<RenderAction>()


        Observable.fromIterable(list)
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                actions.add(RenderAction(500) { moveEmit(it, distinctOperation) })
            }
            .distinctUntilChanged { e1, e2 ->
                if (e1.value == e2.value) {
                    actions.add(RenderAction(500) { dropEmit(e2, distinctOperation) })
                    return@distinctUntilChanged true
                }
                return@distinctUntilChanged false
            }
            .subscribe({
                actions.add(RenderAction(500) { moveEmit(it, observerObject) })
            }, activity.errorHandler, {
                actions.add(RenderAction(0) { observerObject.complete() })
                activity.surfaceView.actions(actions)
            })
            .disposeOnDestroy(activity)
    }

}