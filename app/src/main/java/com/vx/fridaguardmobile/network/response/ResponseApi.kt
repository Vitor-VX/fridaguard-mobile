package com.vx.fridaguardmobile.network.response

import okhttp3.Response
import org.json.JSONObject

interface ResponseApi {
    fun getResponseSuccess(response: Response): JSONObject
    fun getResponseFailed(response: Response): JSONObject
}