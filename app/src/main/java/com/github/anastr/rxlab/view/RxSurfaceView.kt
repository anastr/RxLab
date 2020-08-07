package com.github.anastr.rxlab.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import com.github.anastr.rxlab.objects.drawing.DrawingObject
import com.github.anastr.rxlab.objects.drawing.FpsObject
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.emits.EmitObject
import com.github.anastr.rxlab.objects.time.TimeObject
import com.github.anastr.rxlab.util.*
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.processors.PublishProcessor
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.max

/**
 * Created by Anas Altair on 3/6/2020.
 */
class RxSurfaceView : SurfaceView {

    companion object {
        /** frames per second */
        const val FBS = 50L
    }

    private var lastFrameTime = System.currentTimeMillis()

    private val renderThread = Executors.newSingleThreadExecutor()
    private val actionsThread = Executors.newFixedThreadPool(3)
    private val drawingObjects = ArrayList<DrawingObject>()

    private var isRunning = false
    private var isActionRunning = false
    private val looper: PublishProcessor<Boolean> = PublishProcessor.create<Boolean>()
    private val renderPublisher: PublishProcessor<() -> Unit> = PublishProcessor.create<() -> Unit>()
    /** hot observable to deal with actions (add, remove or move emits - complete observer ...) */
    private val actionSubject: Subject<List<Action>> = PublishSubject.create<List<Action>>().toSerialized()
    private val compositeDisposable = CompositeDisposable()

    constructor(context: Context?): this(context, null)

    constructor(context: Context?, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        looper.switchMap { startRender ->
            if (startRender)
                Flowable.interval(1000 / FBS, TimeUnit.MILLISECONDS)
                    .onBackpressureLatest()
                    .concatMap { Flowable.just(it).skipWhile { isActionRunning } }
                    .observeOn(Schedulers.from(renderThread))
                    .doOnNext {
                        val now = System.currentTimeMillis()
                        update(now - lastFrameTime)
                        lastFrameTime = now
                    }
            else
                Flowable.empty()
        }
            .subscribe()
            .addToDispose()

        renderPublisher
            .doOnNext { isActionRunning = true }
            .observeOn(Schedulers.from(renderThread))
            .subscribe {
                it.invoke()
                isActionRunning = false
            }
            .addToDispose()

        actionSubject
            .flatMap { list ->
                Observable.fromIterable(list)
                    .observeOn(Schedulers.from(actionsThread))
                    .map { it.takeTime(it.delay) }
            }
//            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it.action.invoke(this) }
            .addToDispose()

        addDrawingObject(FpsObject())
    }

    private fun Disposable.addToDispose() {
        compositeDisposable.add(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val exactHeight = Utils.drawingHeight * drawingObjects.size + Utils.drawingPadding * drawingObjects.size
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), max(MeasureSpec.getSize(heightMeasureSpec), exactHeight.toInt()))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        drawingObjects.forEach { it.onSizeChanged(w, h) }
    }

    private fun update(delta: Long) {
        if (isRunning && holder.surface.isValid) {
            val canvas = holder.lockCanvas()
            canvas.drawColor(Color.WHITE)

            drawingObjects.forEach {
                it.draw(delta, canvas)
            }

            holder.unlockCanvasAndPost(canvas)
        }
    }

    /**
     * run actions sequentially depending on their delay.
     */
    fun actions(actions: List<Action>) {
        actionSubject.onNext(actions)
    }


    /**
     * add action to invoke it immediately with its delay.
     *
     * **when you need to use this, its better to not add delay.**
     */
    fun action(action: Action) {
        actionSubject.onNext(listOf(action))
    }

    fun addDrawingObject(drawingObject: DrawingObject) {
        var top = drawingObjects.size * Utils.drawingHeight
        if (drawingObjects.size != 0)
            top += drawingObjects.size * Utils.drawingPadding
        drawingObject.updatePosition(top, width.toFloat())
        drawingObjects.add(drawingObject)
        requestLayout()
    }

    fun addEmit(drawingObject: DrawingObject, emit: EmitObject = BallEmit("", ColorUtil.randomColor())) {
        doOnRenderThread {
            drawingObject.addEmit(emit)
        }
    }

    /**
     * remove emit from first object and add it to the other,
     * then start animation to make smooth movement between operations.
     *
     * you mustn't move more than 1 emit to the same [to] Object,
     * wait 500 ms at least between them.
     */
    fun moveEmit(emit: EmitObject, from: DrawingObject, to: DrawingObject) {
        doOnRenderThread {
            from.removeEmit(emit)
            // get insertPoint before add emit.
            val toPoint = to.getInsertPoint()
            to.addEmit(emit, emit.position)
            // call move must still here to make sure
            // that the emit has take its place.
            doOnMainThread { moveEmit(emit, toPoint) }
        }
    }

    fun dropEmit(emit: EmitObject, from: DrawingObject) {
        doOnMainThread {
            moveEmit(emit, Point(width.toFloat(), emit.rect.top)) { doOnRenderThread { from.removeEmit(emit) } }
        }
    }

    fun startTime(observerObject: ObserverObject, lock: TimeObject.Lock) {
        doOnRenderThread {
            observerObject.startTime(lock)
        }
    }

    /**
     *
     * **must be called on main thread.**
     */
    private fun moveEmit(emit: EmitObject, to: Point, onEnd: () -> Any = {}) {
        val distanceX = to.x - emit.rect.left
        val distanceY = to.y - emit.rect.top
        var pre = 0f
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 400
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                emit.rect.offset(distanceX*(animation.animatedFraction - pre)
                    , distanceY*(animation.animatedFraction - pre))
                pre = animation.animatedFraction
            }
            doOnEnd { onEnd.invoke() }
        }.start()
    }

    fun doOnRenderThread(action: () -> Unit) {
        renderPublisher.onNext(action)
    }

    fun pause() {
        isRunning = false
        looper.onNext(false)
    }

    fun resume() {
        isRunning = true
        looper.onNext(true)
    }

    fun dispose() {
        compositeDisposable.dispose()
    }
}
