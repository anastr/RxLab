package com.github.anastr.rxlab.objects

import android.graphics.Canvas
import android.graphics.RectF
import com.github.anastr.rxlab.util.Point
import com.github.anastr.rxlab.util.Utils
import java.util.*

/**
 * Created by Anas Altair on 3/7/2020.
 */
abstract class EmitObject(leftTopPoint: Point) {

    private val uid: UUID = UUID.randomUUID()

    val rect = RectF(leftTopPoint.x, leftTopPoint.y
        , leftTopPoint.x + Utils.emitSize, leftTopPoint.y + Utils.emitSize)

    val position
        get() = Point(rect.left, rect.top)

    abstract fun draw(canvas: Canvas)

    fun offsetTo(leftTopPoint: Point) = rect.offsetTo(leftTopPoint.x, leftTopPoint.y)

    override fun equals(other: Any?) = other is EmitObject && uid == other.uid

    override fun hashCode() = uid.hashCode()
}