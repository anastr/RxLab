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
 * Created by Anas Altair on 4/8/2020.
 */
class ElementAtActivity: OperationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCode("Observable.just(1, 2, 3, 4, 5)\n" +
                "        .elementAt(2)\n" +
                "        .subscribe();")

        addNote("look carefully at the source, this operation will continue emitting and dropping " +
                "until the desired index is reached, then will call onSuccess and ignoring the rest of emits.")

        val e1 = BallEmit("1")
        val e2 = BallEmit("2")
        val e3 = BallEmit("3")
        val e4 = BallEmit("4")
        val e5 = BallEmit("5")

        val justOperation = FixedEmitsOperation("just", listOf(e1, e2, e3, e4, e5))
        surfaceView.addDrawingObject(justOperation)
        val elementAtOperation = TextOperation("elementAt", "2")
        surfaceView.addDrawingObject(elementAtOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.just(e1, e2, e3, e4, e5)
            .doOnNext {
                actions.add(Action(1000) { moveEmitOnRender(it, elementAtOperation) })
                if (it.value != "3")
                    actions.add(Action(1000) { dropEmit(it, elementAtOperation) })
            }
            .elementAt(2)
            .subscribe({
                actions.add(Action(1000) { moveEmitOnRender(it, observerObject) })
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            }, errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            })
            .disposeOnDestroy()
    }
}
