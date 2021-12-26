package com.github.anastr.rxlab.controllers.coroutines

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.RenderAction
import kotlinx.android.synthetic.main.activity_operation.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class FlowOfController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("""
            flowOf(1, 2, 3).collect {
                // it..
            }
        """.trimIndent())

        val list = listOf(
            BallEmit("1"),
            BallEmit("2"),
            BallEmit("3"),
        )

        val flowOfOperation = FixedEmitsOperation("flowOf", list)
        activity.surfaceView.addDrawingObject(flowOfOperation)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<RenderAction>()

        list.asFlow()
            .onEach {
                actions.add(RenderAction(1000) { moveEmit(it, observerObject) })
            }
            .onCompletion {
                actions.add(RenderAction(0) { observerObject.complete() })
                activity.surfaceView.actions(actions)
            }
            .collect()
    }
}
