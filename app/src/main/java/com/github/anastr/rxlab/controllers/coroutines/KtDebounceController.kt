package com.github.anastr.rxlab.controllers.coroutines

import android.view.View
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.time.TimeObject
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.RenderAction
import kotlinx.android.synthetic.main.activity_operation.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class KtDebounceController: OperationController() {

    @OptIn(ExperimentalTime::class)
    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("""
            callbackFlow {
                fab.setOnClickListener {
                    trySend("emit")
                }
                awaitClose()
            }
                .debounce(Duration.seconds(2))
                .collect { emit ->
                    // ..
                }
        """.trimIndent())

        activity.addNote("add 1 emit and wait, then try to add emits rapidly.")

        activity.fab.visibility = View.VISIBLE

        val debounceObject = TextOperation("debounce", "2 sec")
        activity.surfaceView.addDrawingObject(debounceObject)
        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        callbackFlow {
            activity.fab.setOnClickListener {
                trySend(BallEmit("emit"))
            }
            awaitClose()
        }.onEach {
            activity.surfaceView.action(RenderAction(0) {
                if (observerObject.isTimeLocked() != true)
                    observerObject.removeLastTime()
                observerObject.startTime(TimeObject.Lock.AFTER)
            })
        }.debounce(Duration.seconds(2))
            .catch {
                activity.surfaceView.action(RenderAction(0) { observerObject.complete() })
            }
            .collect {
                val thread = Thread.currentThread().name
                activity.surfaceView.action(RenderAction(0) {
                    it.checkThread(thread)
                    observerObject.lockTime()
                    addThenMove(it, debounceObject, observerObject)
                })
            }
    }
}
