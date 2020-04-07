package com.github.anastr.rxlab.view

/**
 * Created by Anas Altair on 4/5/2020.
 */
data class Action(val delay: Long = 1000, val action: RxSurfaceView.() -> Any)