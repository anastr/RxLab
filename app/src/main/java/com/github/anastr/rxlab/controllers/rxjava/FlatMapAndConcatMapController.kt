package com.github.anastr.rxlab.controllers.rxjava

import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController
import com.github.anastr.rxlab.util.ColorUtil
import com.github.anastr.rxlab.util.delayEach
import com.github.anastr.rxlab.view.RenderAction
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * Created by Anas Altair on 4/5/2020.
 */
class FlatMapAndConcatMapController: OperationController() {

    override suspend fun onCreate(activity: OperationActivity) {
        activity.setCode("Observable.just(\"A,B,C\", \"D,E,F\")\n" +
                "    .flatMap(s -> Observable.fromArray(s.split(\",\"))\n" +
                "            .subscribeOn(Schedulers.computation())\n" +
                "            .map(c -> takeTime(c)))\n" +
                "    .subscribe();\n\n" +
                "Observable.just(\"A,B,C\", \"D,E,F\")\n" +
                "    .concatMap(s -> Observable.fromArray(s.split(\",\"))\n" +
                "            .subscribeOn(Schedulers.computation())\n" +
                "            .map(c -> takeTime(c)))\n" +
                "    .subscribe();")

        activity.addNote("please note that 'flatMap' operation doesn't respect the order," +
                " you will get a different result on ech replay, and concatMap will not receive" +
                " the second emit until the first one is complete.")

        val abcEmit1 = BallEmit("A,B,C")
        val defEmit1 = BallEmit("D,E,F")

        val justOperation1 = FixedEmitsOperation("just", listOf(abcEmit1, defEmit1))
        activity.surfaceView.addDrawingObject(justOperation1)
        val flatMapOperation = TextOperation("flatMap", "")
        activity.surfaceView.addDrawingObject(flatMapOperation)
        val observerObject1 = ObserverObject("Observer 1")
        activity.surfaceView.addDrawingObject(observerObject1)

        val actions1 = ArrayList<RenderAction>()

        Observable.just(abcEmit1, defEmit1)
            .delay(1500, TimeUnit.MILLISECONDS)
            // delay change thread to computation, so we changed it back to main thread.
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                val thread = Thread.currentThread().name
                actions1.add(RenderAction(0) {
                    it.checkThread(thread)
                    moveEmit(it, flatMapOperation)
                })
                Observable.fromIterable(it.value.split(','))
                    .delayEach(Random.nextLong(50))
                    .doOnComplete { actions1.add(RenderAction(0) { flatMapOperation.removeEmit(it) }) }
            }
            .map { BallEmit(it.toString()) }
            .subscribe ({
                val thread = Thread.currentThread().name
                actions1.add(RenderAction(1000) {
                    it.checkThread(thread)
                    addThenMove(it, flatMapOperation, observerObject1)
                })
            }, activity.errorHandler, {
                actions1.add(RenderAction(0) { observerObject1.complete() })
                activity.surfaceView.actions(actions1)
            })
            .disposeOnDestroy(activity)


        val abcEmit2 = BallEmit("A,B,C", ColorUtil.blue)
        val defEmit2 = BallEmit("D,E,F", ColorUtil.blue)

        val justOperation2 = FixedEmitsOperation("just", listOf(abcEmit2, defEmit2))
        activity.surfaceView.addDrawingObject(justOperation2)
        val concatMapOperation = TextOperation("concatMap", "")
        activity.surfaceView.addDrawingObject(concatMapOperation)
        val observerObject2 = ObserverObject("Observer 2")
        activity.surfaceView.addDrawingObject(observerObject2)

        val actions2 = ArrayList<RenderAction>()

        Observable.just(abcEmit2, defEmit2)
            .delay(1500, TimeUnit.MILLISECONDS)
            // delay change thread to computation, so we changed it back to main thread.
            .observeOn(AndroidSchedulers.mainThread())
            .concatMap {
                val thread = Thread.currentThread().name
                actions2.add(RenderAction(0) {
                    it.checkThread(thread)
                    moveEmit(it, concatMapOperation)
                })
                Observable.fromIterable(it.value.split(','))
                    .delayEach(Random.nextLong(50))
                    .doOnComplete { actions2.add(RenderAction(0) { concatMapOperation.removeEmit(it) }) }
            }
            .map { BallEmit(it.toString(), ColorUtil.blue) }
            .subscribe ({
                val thread = Thread.currentThread().name
                actions2.add(RenderAction(1000) {
                    it.checkThread(thread)
                    addThenMove(it, concatMapOperation, observerObject2)
                })
            }, activity.errorHandler, {
                actions2.add(RenderAction(0) { observerObject2.complete() })
                activity.surfaceView.actions(actions2)
            })
            .disposeOnDestroy(activity)
    }
}