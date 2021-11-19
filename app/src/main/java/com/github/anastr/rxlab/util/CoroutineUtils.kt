package com.github.anastr.rxlab.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
fun tickerFlow(period: Duration, initialDelay: Duration = Duration.ZERO) = flow {
    delay(initialDelay)
    while (true) {
        val timeTaken = measureTime { emit(Unit) }
        val correctedPeriod = maxOf(Duration.ZERO, period - timeTaken)
        delay(correctedPeriod)
    }
}
