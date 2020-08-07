package com.github.anastr.rxlab.activities.schedulers

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

class MainSchedulerActivity
    : SchedulerOperationActivity("AndroidSchedulers.mainThread()", AndroidSchedulers.mainThread())