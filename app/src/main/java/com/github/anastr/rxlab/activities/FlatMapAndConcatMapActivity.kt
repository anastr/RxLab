package com.github.anastr.rxlab.activities

import android.os.Bundle
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.drawing.FixedEmitsOperation
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.drawing.TextOperation
import com.github.anastr.rxlab.util.delayEach
import com.github.anastr.rxlab.view.Action
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import kotlinx.android.synthetic.main.activity_operation.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * Created by Anas Altair on 4/5/2020.
 */
class FlatMapAndConcatMapActivity: OperationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCode("Observable.just(\"A,B,C\", \"D,E,F\")\n" +
                "    .flatMap(s -> Observable.fromArray(s.split(\",\"))\n" +
                "            .subscribeOn(Schedulers.computation())\n" +
                "            .map(c -> takeTime(c)))\n" +
                "    .subscribe();\n\n" +
                "Observable.just(\"A,B,C\", \"D,E,F\")\n" +
                "    .concatMap(s -> Observable.fromArray(s.split(\",\"))\n" +
                "            .subscribeOn(Schedulers.computation())\n" +
                "            .map(c -> takeTime(c)))\n" +
                "    .subscribe();")

        addNote("please note that 'flatMap' operation doesn't respect the order," +
                " you will get a different result on ech replay, and concatMap will not receive" +
                " the second emit until the first one is complete.")

        val abcEmit1 = BallEmit("A,B,C")
        val defEmit1 = BallEmit("D,E,F")

        val justOperation1 = FixedEmitsOperation("just", listOf(abcEmit1, defEmit1))
        surfaceView.addDrawingObject(justOperation1)
        val flatMapOperation = TextOperation("flatMap", "")
        surfaceView.addDrawingObject(flatMapOperation)
        val observerObject1 = ObserverObject("Observer 1")
        surfaceView.addDrawingObject(observerObject1)

        val actions1 = ArrayList<Action>()

        Observable.just(abcEmit1, defEmit1)
            .delay(1500, TimeUnit.MILLISECONDS)
            // delay change thread to computation, so we changed it back to main thread.
            .observeOn(AndroidSchedulers.mainThread())
            .flatMap {
                val thread = Thread.currentThread().name
                actions1.add(Action(0) {
                    it.checkThread(thread)
                    moveEmit(it, justOperation1, flatMapOperation)
                })
                Observable.fromIterable(it.value.split(','))
                    .delayEach(Random.nextLong(50))
                    .doOnComplete { actions1.add(Action(0) { doOnRenderThread { flatMapOperation.removeEmit(it) } }) }
            }
            .map { BallEmit(it.toString()) }
            .subscribe ({
                val thread = Thread.currentThread().name
                actions1.add(Action(1000) {
                    it.checkThread(thread)
                    addEmit(flatMapOperation, it)
                    moveEmit(it, flatMapOperation, observerObject1)
                })
            }, errorHandler, {
                actions1.add(Action(0) { doOnRenderThread { observerObject1.complete() } })
                surfaceView.actions(actions1)
            })
            .disposeOnDestroy()


        val abcEmit2 = BallEmit("A,B,C")
        val defEmit2 = BallEmit("D,E,F")

        val justOperation2 = FixedEmitsOperation("just", listOf(abcEmit2, defEmit2))
        surfaceView.addDrawingObject(justOperation2)
        val concatMapOperation = TextOperation("concatMap", "")
        surfaceView.addDrawingObject(concatMapOperation)
        val observerObject2 = ObserverObject("Observer 2")
        surfaceView.addDrawingObject(observerObject2)

        val actions2 = ArrayList<Action>()

        Observable.just(abcEmit2, defEmit2)
            .delay(1500, TimeUnit.MILLISECONDS)
            // delay change thread to computation, so we changed it back to main thread.
            .observeOn(AndroidSchedulers.mainThread())
            .concatMap {
                val thread = Thread.currentThread().name
                actions2.add(Action(0) {
                    it.checkThread(thread)
                    moveEmit(it, justOperation2, concatMapOperation)
                })
                Observable.fromIterable(it.value.split(','))
                    .delayEach(Random.nextLong(50))
                    .doOnComplete { actions2.add(Action(0) { doOnRenderThread { concatMapOperation.removeEmit(it) } }) }
            }
            .map { BallEmit(it.toString()) }
            .subscribe ({
                val thread = Thread.currentThread().name
                actions2.add(Action(1000) {
                    it.checkThread(thread)
                    addEmit(concatMapOperation, it)
                    moveEmit(it, concatMapOperation, observerObject2)
                })
            }, errorHandler, {
                actions2.add(Action(0) { doOnRenderThread { observerObject2.complete() } })
                surfaceView.actions(actions2)
            })
            .disposeOnDestroy()
    }
}