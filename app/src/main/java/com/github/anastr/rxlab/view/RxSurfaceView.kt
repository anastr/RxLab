package com.github.anastr.rxlab.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.SurfaceView
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.view.doOnAttach
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.github.anastr.rxlab.objects.drawing.DrawingObject
import com.github.anastr.rxlab.objects.drawing.FpsObject
import com.github.anastr.rxlab.objects.drawing.ObserverObject
import com.github.anastr.rxlab.objects.emits.BallEmit
import com.github.anastr.rxlab.objects.emits.EmitObject
import com.github.anastr.rxlab.objects.time.TimeObject
import com.github.anastr.rxlab.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import java.util.concurrent.Executors
import kotlin.math.max
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

/**
 * Created by Anas Altair on 3/6/2020.
 */
@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
class RxSurfaceView : SurfaceView {

    companion object {
        /** frames per second */
        const val FBS = 50L
    }

    private var lastFrameTime = System.currentTimeMillis()

    private val renderDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val drawingObjects = ArrayList<DrawingObject>()

    private var isRunning = false
    private val looperStateFlow = MutableStateFlow(false)
    private val fpsObject = FpsObject()

    var onError: ((Throwable) -> Unit)? = null

    private val coroutineScope: CoroutineScope?
        get() = findViewTreeLifecycleOwner()?.lifecycle?.coroutineScope

    constructor(context: Context?): this(context, null)

    constructor(context: Context?, attrs: AttributeSet?): this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    init {
        doOnAttach {
            coroutineScope?.launch {
                withContext(renderDispatcher) {
                    looperStateFlow.flatMapLatest { startRender ->
                        if (startRender)
                            tickerFlow(Duration.milliseconds(1000 / FBS))
                        else
                            emptyFlow()
                    }
                        .collect {
                            val now = System.currentTimeMillis()
                            update(now - lastFrameTime)
                            lastFrameTime = now
                        }
                }
            }
        }
        addDrawingObject(fpsObject)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val exactHeight = drawingObjects.sumOf { it.height.toDouble() } + Utils.drawingPadding * drawingObjects.size
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
        doOnRenderThread {
            actions.forEach { action ->
                delay(action.delay)
                action.action(this@RxSurfaceView)
            }
        }
    }


    /**
     * add action to invoke it immediately with its delay.
     *
     * **when you need to use this, its better to not add delay.**
     */
    fun action(action: Action) {
        actions(listOf(action))
    }

    fun addDrawingObject(drawingObject: DrawingObject) {
        var top = drawingObjects.sumOf { it.height.toDouble() }.toFloat()
        if (drawingObjects.size != 0)
            top += drawingObjects.size * Utils.drawingPadding
        drawingObject.updatePosition(top, width.toFloat())
        drawingObjects.add(drawingObject)
        requestLayout()
    }

    fun addEmitOnRender(
        drawingObject: DrawingObject,
        emit: EmitObject = BallEmit("", ColorUtil.randomColor()),
    ) {
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
    fun moveEmitOnRender(emit: EmitObject, to: DrawingObject) {
        doOnRenderThread {
            moveEmit(emit, to)
        }
    }

    /**
     * remove emit from first object and add it to the other,
     * then start animation to make smooth movement between operations.
     *
     * you mustn't move more than 1 emit to the same [to] Object,
     * wait 500 ms at least between them.
     *
     * **must be called on render thread.**
     */
    fun moveEmit(emit: EmitObject, to: DrawingObject) {
        emit.parentObject?.removeEmit(emit)
        // get insertPoint before add emit.
        val toPoint = to.getInsertPoint()
        to.addEmit(emit, emit.position)
        // call move must still here to make sure
        // that the emit has take its place.
        doOnMainThread { moveEmit(emit, toPoint) }
    }

    /**
     * add [emit] to [addTo], then move it to [moveTo] on render thread
     * using [moveEmit].
     */
    fun addThenMoveOnRender(emit: EmitObject, addTo: DrawingObject, moveTo: DrawingObject) {
        doOnRenderThread {
            addTo.addEmit(emit)
            moveEmit(emit, moveTo)
        }
    }

    fun dropEmit(emit: EmitObject, from: DrawingObject) {
        doOnMainThread {
            moveEmit(emit, Point(width.toFloat(), emit.rect.top)) { doOnRenderThread { from.removeEmit(emit) } }
        }
    }

    fun startTimeOnRender(observerObject: ObserverObject, lock: TimeObject.Lock) {
        doOnRenderThread {
            observerObject.startTime(lock)
        }
    }

    /**
     *
     * **must be called on main thread.**
     */
    private fun moveEmit(emit: EmitObject, to: Point, onEnd: () -> Unit = {}) {
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

    fun doOnRenderThread(action: suspend () -> Unit) {
        coroutineScope?.launch(renderDispatcher) {
            action()
        }
    }

    private inline fun doOnMainThread(crossinline action: suspend () -> Unit) {
        coroutineScope?.launch(Dispatchers.Main) {
            action()
        }
    }

    fun pause() {
        isRunning = false
        looperStateFlow.tryEmit(isRunning)
    }

    fun resume() {
        isRunning = true
        looperStateFlow.tryEmit(isRunning)
    }

    fun dispose() {
        fpsObject.dispose()
    }
}
