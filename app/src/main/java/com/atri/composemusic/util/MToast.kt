package com.atri.composemusic.util

import android.app.Application
import android.widget.Toast

object MToast {

    private var toast: Toast? = null
    private var context: Application? = null

    fun init(app: Application) {
        this.context = app
    }

    fun show(message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

}