package com.github.anastr.rxlab.activities.schedulers

import io.reactivex.rxjava3.schedulers.Schedulers

class SingleSchedulerActivity
    : SchedulerOperationActivity("Schedulers.single()", Schedulers.single())