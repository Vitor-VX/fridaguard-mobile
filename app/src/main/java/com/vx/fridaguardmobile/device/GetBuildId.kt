package com.vx.fridaguardmobile.device

import android.os.Build

object GetBuildId {
    fun getBuildId() : String {
        return Build.ID
    }
}