package com.github.anastr.rxlab.objects

import com.github.anastr.rxlab.controllers.OperationName
import com.github.anastr.rxlab.preview.OperationController

/**
 * Created by Anas Altair on 4/1/2020.
 */
data class Operation(
    val operationName: OperationName,
    val controller: OperationController,
    val type: OperationType = OperationType.RxJava,
)

sealed class OperationType {
    object RxJava: OperationType()
    object Coroutine: OperationType()
}
