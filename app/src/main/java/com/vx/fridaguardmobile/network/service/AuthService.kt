package com.vx.fridaguardmobile.network.service

import android.content.Context
import com.vx.fridaguardmobile.appconfig.GetArrayConfigs
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class AuthService(
    context: Context
) {
    private val baseUrl by lazy { GetArrayConfigs.getArrayConfigs(2, "url-server", context = context) }

    fun login(
        username: String,
        password: String,
        deviceId: String,
        buildId: String,
        customDeviceId: String,
        customBuildId: String
    ) : JSONObject {
        val json = JSONObject().apply {
            put("username", username)
            put("password", password)
            put("deviceId", deviceId)
            put("buildId", buildId)
            put("customDeviceId", customDeviceId)
            put("customBuildId", customBuildId)
        }.toString()

        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder().url("$baseUrl/api/auth/login-user").post(requestBody).build()

        return ApiService.makeRequest(request)
    }

    fun getInitialConfig(token: String) : JSONObject {
        val request = Request.Builder().url("$baseUrl/api/mobile/get-app-id").get().addHeader("Authorization", "Bearer $token").build()

        return ApiService.makeRequest(request)
    }
}