package com.github.anastr.rxlab.controllers

import android.widget.Toast
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*

class ErrorObservableController: OperationController() {
    override fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.error(() -> new RuntimeException(\"just an error\"))\n" +
                "        .subscribe();")

        val observerObject = ObserverObject("Observer")
        activity.surfaceView.addDrawingObject(observerObject)

        Observable.error<Any> { RuntimeException("just an error") }
            .subscribe({ }, {
                Toast.makeText(activity, "just an error", Toast.LENGTH_LONG).show()
                activity.surfaceView.action(Action(0) { doOnRenderThread { observerObject.completeWithError() } })
            })
    }
}