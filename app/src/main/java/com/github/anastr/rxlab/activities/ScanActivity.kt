package com.github.anastr.rxlab.activities

import android.os.Bundle
import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*

/**
 * Created by Anas Altair on 4/12/2020.
 */
class ScanActivity: OperationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCode("Observable.just(1, 2, 3, 4)\n" +
                "        .scan((accumulator, emit) -> accumulator + emit)\n" +
                "        .subscribe();")

        addNote("look carefully to the first emit, it doesn't pass to scan.")

        val a = BallEmit("1")
        val b = BallEmit("2")
        val c = BallEmit("3")
        val d = BallEmit("4")

        val justOperation = FixedEmitsOperation("just", listOf(a, b, c, d))
        surfaceView.addDrawingObject(justOperation)
        val scanOperation = TextOperation("scan", "")
        surfaceView.addDrawingObject(scanOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.just(a, b, c, d)
            .scan { accumulator: BallEmit, emit: BallEmit ->
                actions.add(Action(800) {
                    moveEmitOnRender(emit, scanOperation)
                    val text = (emit.value.toInt()+accumulator.value.toInt()).toString()
                    emit.value = text
                    scanOperation.setText(text)
                })
                emit
            }
            .subscribe({
                val thread = Thread.currentThread().name
                actions.add(Action(800) {
                    it.checkThread(thread)
                    moveEmitOnRender(it, observerObject)
                })
            }, errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            })
            .disposeOnDestroy()
    }
}
