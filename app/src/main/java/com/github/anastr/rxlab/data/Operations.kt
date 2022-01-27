package com.github.anastr.rxlab.data

import com.github.anastr.rxlab.controllers.OperationName
import com.github.anastr.rxlab.controllers.coroutines.FlowOfController
import com.github.anastr.rxlab.controllers.coroutines.KtDebounceController
import com.github.anastr.rxlab.controllers.coroutines.Zip2Controller
import com.github.anastr.rxlab.controllers.rxjava.*
import com.github.anastr.rxlab.objects.Operation
import com.github.anastr.rxlab.objects.OperationType
import com.github.anastr.rxlab.preview.OperationController

fun allOperations() = listOf(
    Operation(OperationName.buffer, BufferController()),
    Operation(OperationName.combineLatest, CombineLatestController()),
    Operation(OperationName.concatMap, ConcatMapController()),
    Operation(OperationName.contains, ContainsController()),
    Operation(OperationName.create, CreateController()),
    Operation(OperationName.distinct, DistinctController()),
    Operation(OperationName.distinctUntilChanged, DistinctUntilChangedController()),
    Operation(OperationName.elementAt, ElementAtController()),
    Operation(OperationName.error, ErrorObservableController()),
    Operation(OperationName.filter, FilterController()),
    Operation(OperationName.flatMap, FlatMapController()),
    Operation(OperationName.fromArray, FromArrayController()),
    Operation(OperationName.fromIterable, FromIterableController()),
    Operation(OperationName.interval, IntervalController()),
    Operation(OperationName.just, JustController()),
    Operation(OperationName.map, MapController()),
    Operation(OperationName.merge, MergeController()),
    Operation(OperationName.range, RangeController()),
    Operation(OperationName.reduce, ReduceController()),
    Operation(OperationName.scan, ScanController()),
    Operation(OperationName.scan_2, Scan2Controller()),
    Operation(OperationName.skip, SkipController()),
    Operation(OperationName.sorted, SortedController()),
    Operation(OperationName.switchMap, SwitchMapController()),
    Operation(OperationName.take, TakeController()),
    Operation(OperationName.takeLast, TakeLastController()),
    Operation(OperationName.throttleFirst, ThrottleFirstController()),
    Operation(OperationName.throttleLast, ThrottleLastController()),
    Operation(OperationName.throttleWithTimeout, ThrottleWithTimeoutController()),
    Operation(OperationName.timer, TimerController()),
    Operation(OperationName.toList, ToListController()),
    Operation(OperationName.zip_2_observables, Zip2Controller()),
    Operation(OperationName.zip_3_observables, Zip3Controller()),

    // Kotlin coroutines
    kotlinOperation(OperationName.kt_flowOf, FlowOfController()),
    kotlinOperation(OperationName.kt_reduce, com.github.anastr.rxlab.controllers.coroutines.ReduceController()),
    kotlinOperation(OperationName.kt_debounce, KtDebounceController()),
    kotlinOperation(OperationName.zip,
        Zip2Controller()
    ),
)

private fun kotlinOperation(
    operationName: OperationName,
    controller: OperationController,
) = Operation(operationName, controller, OperationType.Coroutine)
