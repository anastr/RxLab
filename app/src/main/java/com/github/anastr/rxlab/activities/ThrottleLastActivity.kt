package com.github.anastr.rxlab.activities

import android.os.Bundle
import android.view.View
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.emits.EmitObject
import com.github.anastr.rxlab.objects.time.TimeObject
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/10/2020.
 */
class ThrottleLastActivity: OperationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCode("Observable.<String>create(emitter -> {\n" +
                "    fab.setOnClickListener(v -> {\n" +
                "        if (!emitter.isDisposed())\n" +
                "            emitter.onNext(\"emit\");\n" +
                "    });\n" +
                "})\n" +
                "        // also called sample\n" +
                "        .throttleLast(2, TimeUnit.SECONDS)\n" +
                "        .subscribe();")

        addNote("try to add emits rapidly.")

        fab.visibility = View.VISIBLE

        val throttleObject = TextOperation("throttleLast", "2 sec")
        surfaceView.addDrawingObject(throttleObject)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        // delay to make sure that surfaceView has created.
        Observable.just("")
            .delay(200, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { start(throttleObject, observerObject) }
            .disposeOnDestroy()
    }

    private fun start(throttleObject: TextOperation, observerObject: ObserverObject) {

        Observable.interval(0, 2000, TimeUnit.MILLISECONDS)
            .subscribe {
                surfaceView.action(Action(0) { doOnRenderThread {
                    observerObject.lockTime()
                    observerObject.startTime(TimeObject.Lock.AFTER)
                } })
            }
            .disposeOnDestroy()

        Observable.create<EmitObject> { emitter ->
            fab.setOnClickListener {
                if (!emitter.isDisposed)
                    emitter.onNext(
                        BallEmit(
                            "emit"
                        )
                    )
            }
        }
            .throttleLast(2000, TimeUnit.MILLISECONDS)
            .subscribe({
                val thread = Thread.currentThread().name
                surfaceView.action(Action(0) {
                    it.checkThread(thread)
                    addThenMoveOnRender(it, throttleObject, observerObject)
                })
            }, errorHandler, {
                surfaceView.action(Action(0) { doOnRenderThread { observerObject.complete() } })
            })
            .disposeOnDestroy()
    }
}
