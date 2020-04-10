package com.github.anastr.rxlab.activities

import android.os.Bundle
import com.github.anastr.rxlab.objects.BallEmit
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/1/2020.
 */
class IntervalActivity: OperationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCode("Observable.interval(2000, TimeUnit.MILLISECONDS)\n" +
                "        .subscribe();")

        val intervalOperation = TextOperation("interval", "2000 milliseconds")
        surfaceView.addDrawingObject(intervalOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        Observable.interval(2000, TimeUnit.MILLISECONDS)
            .subscribe {
                val thread = Thread.currentThread().name
                surfaceView.action( Action(0) {
                    val emit = BallEmit("${(it+1)*2} sec")
                    emit.checkThread(thread)
                    addEmit(intervalOperation, emit)
                    moveEmit(emit, intervalOperation, observerObject)
                })
            }
            .disposeOnDestroy()
    }
}