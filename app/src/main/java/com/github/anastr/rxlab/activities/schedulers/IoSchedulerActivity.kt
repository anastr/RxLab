package com.github.anastr.rxlab.activities.schedulers

import io.reactivex.rxjava3.schedulers.Schedulers

class IoSchedulerActivity
    : SchedulerOperationActivity("Schedulers.io()", Schedulers.io())