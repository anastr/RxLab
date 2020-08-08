package com.github.anastr.rxlab.activities.schedulers

import android.os.Bundle
import com.github.anastr.rxlab.activities.OperationActivity
import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import kotlinx.android.synthetic.main.activity_operation.*

open class SchedulerOperationActivity(private val method: String, private val scheduler: Scheduler
                                      , private val note: String? = null): OperationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCode("Observable.just(\"A\")\n" +
                "        .observeOn($method)\n" +
                "        .subscribe();")

        note?.let { addNote(it) }

        val a = BallEmit("A")

        val justOperation = FixedEmitsOperation("just", listOf(a))
        surfaceView.addDrawingObject(justOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.just(a)
            .observeOn(scheduler)
            .subscribe({
                val thread = Thread.currentThread().name
                actions.add(Action(1000) {
                    it.checkThread(thread)
                    moveEmitOnRender(it, justOperation, observerObject)
                })
            }, errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            })
            .disposeOnDestroy()
    }
}
