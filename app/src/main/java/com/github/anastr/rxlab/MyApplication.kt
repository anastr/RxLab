package com.github.anastr.rxlab

import android.app.Application
import com.github.anastr.rxlab.util.Utils
import io.github.kbiakov.codeview.classifier.CodeProcessor

/**
 * Created by Anas Altair on 3/13/2020.
 */
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        CodeProcessor.init(this)
        Utils.prepareData(this)
    }

}