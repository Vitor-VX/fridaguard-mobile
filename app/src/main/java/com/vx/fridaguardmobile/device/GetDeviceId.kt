package com.vx.fridaguardmobile.device

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

object GetDeviceId {
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context) : String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}