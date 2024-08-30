package com.vx.fridaguardmobile.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("OkHttp", "Sending request ${request.url} with headers ${request.headers}")
        return chain.proceed(request)
    }
}