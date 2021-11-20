package com.github.anastr.rxlab.preview

import io.reactivex.rxjava3.disposables.Disposable
import java.io.Serializable

/**
 * Created by Anas Altair on 11/09/2020.
 */
abstract class OperationController: Serializable {

    abstract suspend fun onCreate(activity: OperationActivity)

    fun Disposable.disposeOnDestroy(activity: OperationActivity) {
        activity.addDisposable(this)
    }
}