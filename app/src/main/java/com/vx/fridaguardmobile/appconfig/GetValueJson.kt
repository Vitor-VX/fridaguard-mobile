package com.vx.fridaguardmobile.appconfig

import android.content.Context

object GetValueJson {
    fun getValueJson(name: String, context: Context) : String {
        val json = loadConfig(context) ?: return ""

        return try {
            val jsonValue = json.getString(name)

            jsonValue.ifEmpty {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}