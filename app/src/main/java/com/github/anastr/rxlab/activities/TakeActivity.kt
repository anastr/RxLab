package com.github.anastr.rxlab.activities

import android.os.Bundle
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/6/2020.
 */
class TakeActivity: OperationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCode("Observable.interval(1000, TimeUnit.MILLISECONDS)\n" +
                "        .take(3)\n" +
                "        .subscribe();")

        val intervalOperation = TextOperation("interval", "1000 milliseconds (take 3)")
        surfaceView.addDrawingObject(intervalOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.interval(1, TimeUnit.MILLISECONDS)
            .take(3)
            .subscribe({
                val thread = Thread.currentThread().name
                actions.add(Action(1000) {
                    val emit =
                        BallEmit("${(it + 1)} sec")
                    emit.checkThread(thread)
                    addThenMoveOnRender(emit, intervalOperation, observerObject)
                })
            }, errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            })
            .disposeOnDestroy()
    }
}