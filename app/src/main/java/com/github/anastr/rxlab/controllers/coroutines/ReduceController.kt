package com.github.anastr.rxlab.controllers.coroutines

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.RenderAction
import kotlinx.android.synthetic.main.activity_operation.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class ReduceController : OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {

        activity.setCode("val total = flowOf(1,2,3,4,5).reduce { " +
                "\n val1, val2 -> val1 + val2 }")

        activity.addNote("first, 'reduce' operation will receive two emits, then " +
                "it will take the result of them with next emit...")


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


        coroutineScope {
            val lastBall = list.asFlow().onEach {
                if (it.value == "1")
                    actions.add(RenderAction(100) { moveEmit(it, reduceOperation) })
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

            val thread = Thread.currentThread().name
            actions.add(RenderAction(0) {
                lastBall.checkThread(thread)
                addThenMove(lastBall, reduceOperation, observerObject)
            })
            actions.add(RenderAction(0) { observerObject.complete() })
            activity.surfaceView.actions(actions)
        }






    }
}