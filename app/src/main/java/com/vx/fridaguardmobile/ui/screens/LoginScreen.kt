package com.vx.fridaguardmobile.ui.screens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vx.fridaguardmobile.alert.ToastApp
import com.vx.fridaguardmobile.appconfig.GetArrayConfigs
import com.vx.fridaguardmobile.ui.screens.ui.PageLogin
import com.vx.fridaguardmobile.ui.theme.FridaGuardMobileTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.OutputStreamWriter
import java.net.Socket

class LoginScreen : ComponentActivity() {
    private fun startApp() {
        val packageAppEnter = GetArrayConfigs.getArrayConfigs(1, "package-app", context = applicationContext)
        Log.v("AppFrida", packageAppEnter)
        val launchIntent = applicationContext.packageManager.getLaunchIntentForPackage(
            packageAppEnter.ifEmpty { throw Exception("App not found") })

        if (launchIntent != null) {
            applicationContext.startActivity(launchIntent)
        }
    }

    private fun sendDataToApp(token: String) {
        Log.v("AppFrida", "Event send");

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val socket = Socket("127.0.0.1", 6070)

                if (socket.isConnected) {
                    val outputStreamWriter = OutputStreamWriter(socket.getOutputStream())
                    outputStreamWriter.write("token=$token")
                    outputStreamWriter.flush()
                    outputStreamWriter.close()

                    socket.close()

                    Log.v("AppFrida", "Value send to TCP")
                    return@launch
                }

                Log.v("AppFrida", "Error connection localHost")
            } catch(e: Exception) {
                Log.v("AppFrida", "Error sendDataTCP: $e")
            }
        }
    }

    private fun init(token: String) {
        try {
            startApp()
        } catch (e: Exception) {
            if (e.message == "App not found") {
                ToastApp.toast("App not found.", applicationContext)

                return
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            sendDataToApp(token = token)
        }, 3000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FridaGuardMobileTheme {
                PageLogin(
                    event = {
                        init(it)
                    }
                )
            }
        }
    }
}