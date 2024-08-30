package com.vx.fridaguardmobile.alert

import android.content.Context
import android.widget.Toast

object ToastApp {
    fun toast(text: String, context: Context) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show()
    }
}