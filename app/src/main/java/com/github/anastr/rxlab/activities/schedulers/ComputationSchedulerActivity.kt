package com.github.anastr.rxlab.activities.schedulers

import io.reactivex.rxjava3.schedulers.Schedulers

class ComputationSchedulerActivity
    : SchedulerOperationActivity("Schedulers.computation()", Schedulers.computation()
    , "used for calculating or long process.")
