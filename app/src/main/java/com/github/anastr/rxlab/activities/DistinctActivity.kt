package com.github.anastr.rxlab.activities

import android.os.Bundle
import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit

/**
 * Created by Anas Altair on 4/8/2020.
 */
class DistinctActivity: OperationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCode("Observable.just(1, 1, 3, 2, 2, 3, 1)\n" +
                "        .distinct()\n" +
                "        .subscribe();")


        val list = listOf(
            BallEmit("1"),
            BallEmit("1"),
            BallEmit("3"),
            BallEmit("2")
            ,
            BallEmit("2"),
            BallEmit("3"),
            BallEmit("1")
        )

        val fromIterableOperation = FixedEmitsOperation("just", list)
        surfaceView.addDrawingObject(fromIterableOperation)
        val distinctOperation = TextOperation("distinct", "")
        surfaceView.addDrawingObject(distinctOperation)
        val observerObject = ObserverObject("Observer")
        surfaceView.addDrawingObject(observerObject)

        val actions = ArrayList<Action>()

        val keys = ArrayList<String>()

        Observable.fromIterable(list)
            .delay(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                actions.add(Action(500) { moveEmitOnRender(it, distinctOperation) })
                if (keys.contains(it.value))
                    actions.add(Action(500) { dropEmit(it, distinctOperation) })
                else
                    actions.add(Action(500) { moveEmitOnRender(it, observerObject) })
            }
            .distinct({ it.value }, { keys })
            .subscribe({}, errorHandler, {
                actions.add(Action(0) { doOnRenderThread { observerObject.complete() } })
                surfaceView.actions(actions)
            })
            .disposeOnDestroy()
    }

}