package com.github.anastr.rxlab.util

import android.content.Context
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Created by Anas Altair on 3/13/2020.
 */
class Utils {
    companion object {

        internal var density = 0f
        internal var drawingPadding = 0f
        internal var defaultDrawingHeight = 0f
        internal var threadSize = 0f
        internal var threadTextSize = 0f
        internal var emitSize = 0f
        internal var emitSpeed = 0f
        internal var emitTextSize = 0f

        fun prepareData(context: Context) {
            density = context.resources.displayMetrics.density
            drawingPadding = dpToPx(8f)
            defaultDrawingHeight = dpToPx(60f)
            threadSize = dpToPx(10f)
            threadTextSize =  dpToPx(7f)
            emitSize = dpToPx(40f)
            emitSpeed = dpToPx(40f) / 1000f
            emitTextSize = dpToPx(9f)
        }
    }
}

fun dpToPx(dp: Float) = dp * Utils.density
//fun pxToDp(px: Float) = px / Utils.density

/**
 * sleep for some time.
 */
fun <T>T.takeTime(time: Long): T {
    try {
        Thread.sleep(time)
    }
    catch (ignore: Exception) {}
    return this
}

///**
// * sleep for random time.
// */
//fun <T>T.takeRandomTime(time: Long): T = takeTime(Random(time).nextLong())

fun <T>Observable<T>.delayEach(interval: Long): Observable<T>
        = this.subscribeOn(Schedulers.computation()).map { it.takeTime(interval) }

fun String.isNumber(): Boolean = toIntOrNull() != null
