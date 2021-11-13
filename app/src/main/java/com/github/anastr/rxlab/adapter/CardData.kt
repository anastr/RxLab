package com.github.anastr.rxlab.adapter

import android.app.Activity
import com.github.anastr.rxlab.preview.OperationActivity

data class CardData(
    val title: String,
    val clazz: Class<out Activity> = OperationActivity::class.java,
)
