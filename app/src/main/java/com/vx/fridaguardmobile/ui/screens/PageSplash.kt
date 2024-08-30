package com.vx.fridaguardmobile.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vx.fridaguardmobile.appconfig.GetValueJson
import com.vx.fridaguardmobile.network.service.AuthService
import com.vx.fridaguardmobile.prefs.PrefsApp
import com.vx.fridaguardmobile.ui.componets.infos.SplashAnimatedTextSteps
import com.vx.fridaguardmobile.ui.componets.loading.LoadingIndicator
import com.vx.fridaguardmobile.ui.componets.utils.AnimatedText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

fun checkCustomDevice(prefs: PrefsApp): Boolean {
    val customDeviceId = prefs.getString("custom-device-id")
    val customBuildId = prefs.getString("custom-build-id")
    return !(customDeviceId.isNullOrEmpty() && customBuildId.isNullOrEmpty())
}

@Composable
fun PageSplash(navController: NavController) {
    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(false) }
    var tokenRequest by remember { mutableStateOf("") }
    val timeExperienceForUser by lazy { GetValueJson.getValueJson("timeExperienceForUsers", context = context) }

    val authService = AuthService(context = context)
    val prefs by lazy { PrefsApp(context = context) }

    LaunchedEffect(tokenRequest, isLoading) {
        if (tokenRequest.isNotEmpty() && isLoading) {
            delay(timeExperienceForUser.toLong())

            if (checkCustomDevice(prefs)) {
                isLoading = false
            } else {
                try {
                    val json = withContext(Dispatchers.IO) {
                        authService.getInitialConfig(tokenRequest)
                    }
                    val success = json.getBoolean("success")

                    if (success) {
                        val data = json.getJSONObject("data")
                        val customDeviceId = data.getString("customDeviceId")
                        val customBuildId = data.getString("customBuildId")
                        prefs.saveString("custom-device-id", customDeviceId)
                        prefs.saveString("custom-build-id", customBuildId)

                        isLoading = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            if (!isLoading) {
                navController.popBackStack()
                context.startActivity(Intent(context, LoginScreen::class.java))
            }
        }
    }

    Box(
        modifier = Modifier
            .background(color = Color(0xFF090404))
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        SplashAnimatedTextSteps(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
            onEvent = { token, loading ->
                tokenRequest = token ?: ""
                isLoading = loading
            }
        )

        if (isLoading) {
            AnimatedText(text = "Frida Guard")

            LoadingIndicator(
                isLoading = isLoading,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            )
        }
    }
}