package com.vx.fridaguardmobile.appconfig

import android.content.Context

object GetArrayConfigs {
    fun getArrayConfigs(index: Int, name: String, context: Context) : String {
        val json = loadConfig(context) ?: return ""

        return try {
            val configsArray = json.getJSONArray("configs")
            if (configsArray.length() > index) {
                configsArray.getJSONObject(index).getString(name)
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}