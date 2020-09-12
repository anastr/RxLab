package com.github.anastr.rxlab.controllers

import android.widget.Toast
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable

class ErrorObservableController: OperationController() {
    override fun onCreate() {
        setCode("Observable.error(() -> new RuntimeException(\"just an error\"))\n" +
                "        .subscribe();")

        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        Observable.error<Any> { RuntimeException("just an error") }
            .subscribe({ }, {
                Toast.makeText(activity, "just an error", Toast.LENGTH_LONG).show()
                surfaceView.action(Action(0) { doOnRenderThread { observerObject.completeWithError() } })
            })
    }
}