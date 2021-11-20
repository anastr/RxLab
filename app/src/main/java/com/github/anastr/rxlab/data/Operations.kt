package com.github.anastr.rxlab.data

import com.github.anastr.rxlab.controllers.coroutines.FlowOfController
import com.github.anastr.rxlab.controllers.rxjava.*
import com.github.anastr.rxlab.objects.Operation
import com.github.anastr.rxlab.objects.OperationType

fun allOperations() = listOf(
    Operation("buffer", BufferController()),
    Operation("combineLatest", CombineLatestController()),
    Operation("concatMap", ConcatMapController()),
    Operation("contains", ContainsController()),
    Operation("create", CreateController()),
    Operation("distinct", DistinctController()),
    Operation("distinctUntilChanged", DistinctUntilChangedController()),
    Operation("elementAt", ElementAtController()),
    Operation("error", ErrorObservableController()),
    Operation("filter", FilterController()),
    Operation("flatMap", FlatMapController()),
    Operation("fromArray", FromArrayController()),
    Operation("fromIterable", FromIterableController()),
    Operation("interval", IntervalController()),
    Operation("just", JustController()),
    Operation("map", MapController()),
    Operation("merge", MergeController()),
    Operation("range", RangeController()),
    Operation("reduce", ReduceController()),
    Operation("scan", ScanController()),
    Operation("scan2", Scan2Controller()),
    Operation("skip", SkipController()),
    Operation("sorted", SortedController()),
    Operation("switchMap", SwitchMapController()),
    Operation("take", TakeController()),
    Operation("takeLast", TakeLastController()),
    Operation("throttleFirst", ThrottleFirstController()),
    Operation("throttleLast", ThrottleLastController()),
    Operation("throttleWithTimeout", ThrottleWithTimeoutController()),
    Operation("timer", TimerController()),
    Operation("toList", ToListController()),
    Operation("zip 2 observables", Zip2Controller()),
    Operation("zip 3 observables", Zip3Controller()),

    // Kotlin coroutines
    Operation("flowOf", FlowOfController(), OperationType.Coroutine),
)
