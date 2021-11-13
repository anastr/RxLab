package com.github.anastr.rxlab.adapter

import com.github.anastr.rxlab.preview.OperationController

/**
 * Created by Anas Altair on 4/1/2020.
 */
data class OperationData(
    val name: String,
    val operationController: OperationController? = null,
)
