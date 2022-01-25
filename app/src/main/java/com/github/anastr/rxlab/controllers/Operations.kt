package com.github.anastr.rxlab.controllers

@Suppress("EnumEntryName")
enum class OperationName(val text: String) {

    // RxJava
    buffer("buffer"),
    combineLatest("combineLatest"),
    concatMap("concatMap"),
    contains("contains"),
    create("create"),
    distinct("distinct"),
    distinctUntilChanged("distinctUntilChanged"),
    elementAt("elementAt"),
    error("error"),
    filter("filter"),
    flatMap("flatMap"),
    fromArray("fromArray"),
    fromIterable("fromIterable"),
    interval("interval"),
    just("just"),
    map("map"),
    merge("merge"),
    range("range"),
    reduce("reduce"),
    scan("scan"),
    scan_2("scan with initialValue"),
    skip("skip"),
    sorted("sorted"),
    switchMap("switchMap"),
    take("take"),
    takeLast("takeLast"),
    throttleFirst("throttleFirst"),
    throttleLast("throttleLast"),
    throttleWithTimeout("throttleWithTimeout"),
    timer("timer"),
    toList("toList"),
    zip_2_observables("zip 2 observables"),
    zip_3_observables("zip 3 observables"),
    // Schedules
    mainThread("mainThread"),
    computation("computation"),
    io("io"),
    single("single"),
    other("other"),

    // Kotlin Coroutines
    kt_flowOf("flowOf"),
    kt_reduce("reduce"),
    kt_debounce("debounce"),

    // Compare
    compare_flatMap_concatMap("flatMap and concatMap"),

    // Hot sources
    hot_create("create"),
}
