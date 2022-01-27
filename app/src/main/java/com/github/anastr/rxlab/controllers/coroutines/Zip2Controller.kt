package com.github.anastr.rxlab.controllers.coroutines

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.emits.MergedBallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.util.ColorUtil
import com.github.anastr.rxlab.view.RenderAction
import kotlinx.android.synthetic.main.activity_operation.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*

class Zip2Controller : OperationController() {
    override suspend fun onCreate(activity: OperationActivity) {

        activity.setCode(
           "val flow = flowOf(1, 2, 3).onEach { delay(10) }\n" +
                   "val flow2 = flowOf(\"a\", \"b\", \"c\", \"d\").onEach { delay(15) }\n" +
                   "flow.zip(flow2) { i, s -> i.toString() + s }.collect {\n" +
                   "    println(it) // Will print \"1a 2b 3c\"\n" +
                   "}"
        )



        val a = BallEmit("A")
        val b = BallEmit("B")
        val c = BallEmit("C")
        val d = BallEmit("D")

        val e1 = BallEmit("1", ColorUtil.blue)
        val e2 = BallEmit("2", ColorUtil.blue)
        val e3 = BallEmit("3", ColorUtil.blue)
        val e4 = BallEmit("4", ColorUtil.blue)
        val e5 = BallEmit("5", ColorUtil.blue)


        val list = listOf(a, b, c, d)
        val list2 = listOf(e1, e2, e3, e4, e5)

        val justLettersOperation = FixedEmitsOperation("flowOf", listOf(a, b, c, d))
        activity.surfaceView.addDrawingObject(justLettersOperation)
        val justNumbersOperation = FixedEmitsOperation("flowOf", listOf(e1, e2, e3, e4, e5))
        activity.surfaceView.addDrawingObject(justNumbersOperation)
        val zipOperation = FixedEmitsOperation("zip", ArrayList())
        activity.surfaceView.addDrawingObject(zipOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<RenderAction>()





        list.asFlow().zip(list2.asFlow()) { emit1, emit2 ->
            val mergedBallEmit = MergedBallEmit(emit2.position, emit1, emit2)
            val thread = Thread.currentThread().name
            actions.add(RenderAction(1000) { moveEmit(emit1, zipOperation) })
            actions.add(RenderAction(1000) { moveEmit(emit2, zipOperation) })
            actions.add(RenderAction(500) {
                mergedBallEmit.checkThread(thread)
                zipOperation.removeEmit(emit1)
                zipOperation.removeEmit(emit2)
                addThenMove(mergedBallEmit, zipOperation, observerObject)
            })
            println("Zip operation")
        }.onCompletion {
            println("Zip onCompletion")

            activity.surfaceView.actions(actions)

        }.collect {
            println("Zip collect ${actions.size}")
        }
    }
}