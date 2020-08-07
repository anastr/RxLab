package com.github.anastr.rxlab.activities.schedulers

import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executors

class OtherSchedulerActivity
    : SchedulerOperationActivity("Schedulers.from(Executors.newFixedThreadPool(3)"
    , Schedulers.from(Executors.newFixedThreadPool(3)), "custom thread, fixed number of threads..")