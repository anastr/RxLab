package com.github.anastr.rxlab.util

import kotlin.random.Random

/**
 * Created by Anas Altair on 3/27/2020.
 */
@Suppress("MemberVisibilityCanBePrivate")
class ColorUtil {
    companion object {
        //// emit colors:
        const val red = 0xFFF44336.toInt()
        const val pink = 0xFFFF4081.toInt()
        const val purple = 0xFFAB47BC.toInt()
        const val litePurple = 0xFFE040FB.toInt()
        const val deepPurple = 0xFF7E57C2.toInt()
        const val blue = 0xFF448AFF.toInt()
        const val liteBlue = 0xFF00B0FF.toInt()
        const val blueGray = 0xFF607D8B.toInt()
        const val green = 0xFF43A047.toInt()
        const val orange = 0xFFEF6C00.toInt()
        const val deepOrange = 0xFFF4511E.toInt()
        const val brown = 0xFF795548.toInt()
        const val gray = 0xFF757575.toInt()

        //// thread colors:
        const val mainThread = 0xFFFFAB91.toInt()
        const val computationThread = 0xFF18FFFF.toInt()
        const val ioThread = 0xFFFFFF00.toInt()
        const val singleThread = 0xFF76FF03.toInt()
        const val otherThread = 0xFFF5F5F5.toInt()

        private val colors = arrayOf(red, pink, purple, litePurple, deepPurple, blue, liteBlue
            , blueGray, green, orange, deepOrange, brown, gray)

        fun randomColor(): Int = colors[Random.nextInt(colors.size)]
    }
}