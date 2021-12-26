package com.github.anastr.rxlab.controllers.rxjava

import android.view.View
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.emits.EmitObject
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.RenderAction
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*

/**
 * Created by Anas Altair on 4/10/2020.
 */
class CreateController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.<String>create(emitter -> {\n" +
                "    fab.setOnClickListener(v -> {\n" +
                "        if (!emitter.isDisposed())\n" +
                "            emitter.onNext(\"emit\");\n" +
                "    });\n" +
                "}).subscribe();")

        activity.fab.visibility = View.VISIBLE

        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        Observable.create<EmitObject> { emitter ->
            activity.fab.setOnClickListener {
                if (!emitter.isDisposed)
                    emitter.onNext(
                        BallEmit(
                            "emit"
                        )
                    )
            }
        }
            .subscribe({
                activity.surfaceView.action(RenderAction(0) { observerObject.addEmit(it) })
            }, activity.errorHandler, {
                activity.surfaceView.action(RenderAction(0) { observerObject.complete() })
            })
            .disposeOnDestroy(activity)
    }
}
