package com.github.anastr.rxlab.adapter

import android.app.Activity
import com.github.anastr.rxlab.preview.OperationActivity
import com.github.anastr.rxlab.preview.OperationController

/**
 * Created by Anas Altair on 4/1/2020.
 */
data class OperationData(val name: String
                         , val clazz: Class<out Activity> = OperationActivity::class.java
                         , val operationController: OperationController? = null)