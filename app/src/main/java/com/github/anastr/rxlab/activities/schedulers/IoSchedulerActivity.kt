package com.github.anastr.rxlab.activities.schedulers

import io.reactivex.rxjava3.schedulers.Schedulers

class IoSchedulerActivity
    : SchedulerOperationActivity("Schedulers.io()", Schedulers.io()
    , "used for any process needs to wait, like Api call, read and write on files.")