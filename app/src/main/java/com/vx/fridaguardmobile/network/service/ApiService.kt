package com.vx.fridaguardmobile.network.service

import android.content.Context
import com.vx.fridaguardmobile.alert.ToastApp
import com.vx.fridaguardmobile.network.interceptor.LoggingInterceptor
import com.vx.fridaguardmobile.network.response.ResponseApi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

object ApiService : ResponseApi {
    private val client = OkHttpClient.Builder()
        .addInterceptor(LoggingInterceptor())
        .retryOnConnectionFailure(true)
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    fun makeRequest(request: Request): JSONObject {
        return try {
            val requestApp = request.newBuilder().build()
            val response = client.newCall(requestApp).execute()

            if (response.isSuccessful) {
                getResponseSuccess(response)
            } else {
                getResponseFailed(response)
            }
        } catch (e: SocketTimeoutException) {
            JSONObject().apply { put("error", "Request timed out") }
        } catch (e: Exception) {
            JSONObject().apply { put("error", "Request failed: ${e.message}") }
        }
    }


    override fun getResponseSuccess(response: Response): JSONObject {
        return response.body?.string()?.let { it -> JSONObject(it) } ?: JSONObject()
    }

    override fun getResponseFailed(response: Response): JSONObject {
        return response.body?.string()?.let { it -> JSONObject(it) } ?: JSONObject()
    }
}
