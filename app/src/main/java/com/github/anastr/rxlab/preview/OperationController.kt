package com.github.anastr.rxlab.preview

import android.view.View
import com.github.anastr.rxlab.view.RxSurfaceView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.activity_operation.*
import java.io.Serializable

/**
 * Created by Anas Altair on 11/09/2020.
 */
abstract class OperationController: Serializable {
    lateinit var activity: OperationActivity

    abstract fun onCreate()

    protected val surfaceView: RxSurfaceView
        get() = activity.surfaceView
    protected val fab: FloatingActionButton
        get() = activity.fab

    protected val errorHandler: (Throwable) -> Unit
        get() = activity.errorHandler

    protected fun setCode(code: String) {
        activity.setCode(code)
    }

    protected fun addNote(note: String) {
        activity.addNote(note)
    }

    protected fun addExtraView(view: View) {
        activity.addExtraView(view)
    }

    fun Disposable.disposeOnDestroy() {
        activity.addDisposable(this)
    }
}