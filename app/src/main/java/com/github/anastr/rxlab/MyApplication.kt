package com.github.anastr.rxlab

import android.app.Application
import com.github.anastr.rxlab.util.Utils

/**
 * Created by Anas Altair on 3/13/2020.
 */
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        Utils.prepareData(this)
    }

}