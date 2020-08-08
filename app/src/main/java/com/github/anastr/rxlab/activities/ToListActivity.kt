package com.github.anastr.rxlab.activities

import android.os.Bundle
import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.emits.ListEmit
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*

/**
 * Created by Anas Altair on 7/8/2020.
 */
class ToListActivity: OperationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCode("Observable.just(1, 2, 3)\n" +
                "        .toList()\n" +
                "        .subscribe();")


        val a = BallEmit("1")
        val b = BallEmit("2")
        val c = BallEmit("3")

        val justOperation = FixedEmitsOperation("just", listOf(a, b, c))
        surfaceView.addDrawingObject(justOperation)
        val toListOperation = FixedEmitsOperation("toList", ArrayList())
        surfaceView.addDrawingObject(toListOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        Observable.just(a, b, c)
            .doOnNext {
                actions.add(Action(1000) { moveEmitOnRender(it, justOperation, toListOperation) })
            }
            .toList()
            .subscribe( { list ->
                val listEmit = ListEmit(list.last().position, *list.toTypedArray())
                val thread = Thread.currentThread().name
                actions.add(Action(500) {
                    listEmit.checkThread(thread)
                    doOnRenderThread {
                        list.forEach { toListOperation.removeEmit(it) }
                    }
                    addThenMoveOnRender(listEmit, toListOperation, observerObject)
                })

                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            }, errorHandler)
            .disposeOnDestroy()
    }
}