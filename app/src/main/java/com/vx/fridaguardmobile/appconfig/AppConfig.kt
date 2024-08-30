package com.vx.fridaguardmobile.appconfig

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Optional

fun readJsonFile(context: Context, filename: String) : String? {
    return try {
        val inputStream = context.assets.open(filename)
        val reader = InputStreamReader(inputStream)
        val bufferedReader = BufferedReader(reader)

        val stringBuilder = StringBuilder()
        var line: String? = bufferedReader.readLine()

        while(line != null) {
            stringBuilder.append(line)
            line = bufferedReader.readLine()
        }

        bufferedReader.close()
        stringBuilder.toString()
    } catch (e: RuntimeException){
        e.printStackTrace()
        null
    }
}

fun loadConfig(context: Context) : JSONObject? {
    return try {
        val jsonText = readJsonFile(context = context, filename = "config.json")
        val json = JSONObject(jsonText!!)

        json
    } catch (e: RuntimeException) {
        e.printStackTrace()
        null
    }
}