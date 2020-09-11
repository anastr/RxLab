package com.github.anastr.rxlab.controllers

import android.view.View
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.emits.EmitObject
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable

/**
 * Created by Anas Altair on 4/10/2020.
 */
class CreateController: OperationController() {

    override fun onCreate() {
        setCode("Observable.<String>create(emitter -> {\n" +
                "    fab.setOnClickListener(v -> {\n" +
                "        if (!emitter.isDisposed())\n" +
                "            emitter.onNext(\"emit\");\n" +
                "    });\n" +
                "}).subscribe();")

        fab.visibility = View.VISIBLE

        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

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
            .subscribe({
                surfaceView.action(Action(0) { addEmitOnRender(observerObject, it) })
            }, errorHandler, {
                surfaceView.action(Action(0) { doOnRenderThread { observerObject.complete() } })
            })
            .disposeOnDestroy()
    }
}
