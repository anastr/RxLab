package com.github.anastr.rxlab.controllers.coroutines

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.RenderAction
import kotlinx.android.synthetic.main.activity_operation.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class ReduceController : OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {


        val a = BallEmit("1")
        val b = BallEmit("2")
        val c = BallEmit("3")
        val d = BallEmit("4")
        val e = BallEmit("5")

        val list = listOf(a, b, c, d, e)

        val onEachOperation = FixedEmitsOperation("reduce", listOf(a, b, c, d, e))
        activity.surfaceView.addDrawingObject(onEachOperation)
        val reduceOperation = TextOperation("reduce", "")
        activity.surfaceView.addDrawingObject(reduceOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<RenderAction>()

        list.asFlow().onEach {
            println("onEach Coroutines")
            delay(500)
            if (it.value == "1")
                actions.add(RenderAction(0) { moveEmit(it, reduceOperation) })
        }.onCompletion {
            println("onCompletion Coroutines")
            actions.add(RenderAction(0) { observerObject.complete() })
            activity.surfaceView.actions(actions)
        }.reduce { emit1: BallEmit, emit2: BallEmit ->
            println("Reduce Coroutines")
            val text = (emit1.value.toInt() + emit2.value.toInt()).toString()
            actions.add(RenderAction(0) { moveEmit(emit2, reduceOperation) })
            actions.add(RenderAction(1000) {
                reduceOperation.setText(text)
                if (emit1.value == "1")
                    dropEmit(emit1, reduceOperation)
                dropEmit(emit2, reduceOperation)
            })
            BallEmit(text)
        }


    }
}