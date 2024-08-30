package com.vx.fridaguardmobile.ui.screens.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vx.fridaguardmobile.prefs.PrefsApp
import com.vx.fridaguardmobile.ui.componets.developer.LicenseDeveloper
import com.vx.fridaguardmobile.ui.componets.loading.LoadingIndicator
import com.vx.fridaguardmobile.R
import com.vx.fridaguardmobile.alert.PopupAlert
import com.vx.fridaguardmobile.alert.ToastApp
import com.vx.fridaguardmobile.appconfig.loadConfig
import com.vx.fridaguardmobile.device.GetBuildId
import com.vx.fridaguardmobile.device.GetDeviceId
import com.vx.fridaguardmobile.network.service.AuthService
import com.vx.fridaguardmobile.ui.screens.ui.logincomponents.BtnLogin
import com.vx.fridaguardmobile.ui.screens.ui.logincomponents.CustomCheckBoxWithText
import com.vx.fridaguardmobile.ui.screens.ui.logincomponents.CustomTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

val fontTitle = FontFamily(
    Font(resId = R.font.poppins_semibold)
)

@Composable
fun TextTitleLogin(text: String) {
    Text(
        text = text,
        color = Color(0xFF8E2DE2),
        fontFamily = fontTitle,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun TextInfoLogin(text: String) {
    Text(
        text = text,
        color = Color(0xFF8E2DE2),
        fontFamily = fontTitle,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun LoginForm(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isRemember: Boolean,
    onRememberChange: (Boolean) -> Unit,
    title: String,
    titleInfo: String,
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        TextTitleLogin(text = title)

        Spacer(modifier = Modifier.height(8.dp))
        TextInfoLogin(text = titleInfo)

        Spacer(modifier = Modifier.height(24.dp))

        CustomTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = "Username",
            Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Password",
            Icons.Default.Lock,
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomCheckBoxWithText(isRemember, modifier = Modifier.align(Alignment.Start)) {
            onRememberChange(!isRemember)
        }

        Spacer(modifier = Modifier.height(24.dp))

        BtnLogin(
            onClick = onLoginClick,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Composable
fun FeedbackAlerts(
    isRequestSuccess: String?,
    isRequestFailed: String?,
    onSuccessDismiss: () -> Unit,
    onFailureDismiss: () -> Unit,
    event: (String) -> Unit?
) {
    isRequestSuccess?.let { json ->
        val result = JSONObject(json)

        val success = if (result.has("success")) result.getBoolean("success") else false
        val message = if (result.has("success")) result.getString("message") else "Unrecognized error"

        if (!success) {
            PopupAlert(
                title = "Oops, something went wrong.",
                message = message
            ) {
                onSuccessDismiss()
            }
            return
        }

        PopupAlert(
            title = "Success!",
            message = message
        ) {
            val data = result.getJSONArray("data").getJSONObject(0)
            val token = data.getString("token")
            event(token)
            onSuccessDismiss()
        }
    }

    isRequestFailed?.let {
        PopupAlert(
            title = "Failed to Server",
            message = "Internal server error.",
            onDismiss = onFailureDismiss
        )
    }
}

fun login(
    authService: AuthService,
    username: String,
    password: String,
    deviceId: String,
    buildId: String,
    customDeviceId: String,
    customBuildId: String,
    onSuccess: (String) -> Unit,
    onFailure: (String) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val json = authService.login(
                username = username,
                password = password,
                deviceId = deviceId,
                buildId = buildId,
                customDeviceId = customDeviceId,
                customBuildId = customBuildId
            ).toString()

            withContext(Dispatchers.Main) {
                onSuccess(json)
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                onFailure("Login Failed")
            }
        }
    }
}

@Composable
fun Config() {
    val systemUiController = rememberSystemUiController()
    systemUiController.statusBarDarkContentEnabled = false
    systemUiController.navigationBarDarkContentEnabled = false
}

@Composable
fun PageLogin(event: (String) -> Unit?) {
    Config()
    val context = LocalContext.current
    val prefs by lazy { PrefsApp(context = context) }

    var username by remember { mutableStateOf(prefs.getString("username") ?: "") }
    var password by remember { mutableStateOf(prefs.getString("password") ?: "") }
    var isRemember by remember { mutableStateOf(prefs.getBoolean("isRemember")) }

    val customDeviceId by remember { mutableStateOf(prefs.getString("custom-device-id") ?: "") }
    val customBuildId by remember { mutableStateOf(prefs.getString("custom-build-id") ?: "") }
    val deviceId = GetDeviceId.getDeviceId(context = context)
    val buildId = GetBuildId.getBuildId()
    var isLoading by remember { mutableStateOf(false) }
    val authService = AuthService(context = context)
    var isRequestSuccess by remember { mutableStateOf<String?>(null) }
    var isRequestFailed by remember { mutableStateOf<String?>(null) }

    // init config json
    val (title, titleInfo) = loadConfig(context)?.let { config ->
        val jsonLoginConfig = config.getJSONArray("loginApp").getJSONObject(0)
        jsonLoginConfig.getString("title") to jsonLoginConfig.getString("titleInfo")
    } ?: ("Login" to "Welcome user!")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0C0517))
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginForm(
                username = username,
                onUsernameChange = { newUsername ->
                    username = newUsername
                    if (isRemember) {
                        prefs.saveString("username", newUsername)
                    }
                },
                password = password,
                onPasswordChange = { newPassword ->
                    password = newPassword
                    if (isRemember) {
                        prefs.saveString("password", newPassword)
                    }
                },
                isRemember = isRemember,
                onRememberChange = {
                    isRemember = !isRemember
                    prefs.saveBoolean("isRemember", isRemember)

                    if (isRemember) {
                        prefs.saveString("username", username)
                        prefs.saveString("password", password)
                    } else {
                        prefs.saveString("username", "")
                        prefs.saveString("password", "")
                    }
                },
                title = title,
                titleInfo = titleInfo,
                onLoginClick = {
                    isLoading = true

                    if (username.isEmpty() || password.isEmpty()) {
                        ToastApp.toast(
                            text = "Username or Password not defined",
                            context = context
                        )
                        isLoading = false
                        return@LoginForm
                    } else if (customDeviceId.isEmpty() || customBuildId.isEmpty()) {
                        ToastApp.toast(
                            text = "Request device not defined, restart app",
                            context = context
                        )
                        isLoading = false
                        return@LoginForm
                    }

                    login(
                        authService = authService,
                        username = username,
                        password = password,
                        deviceId = deviceId,
                        buildId = buildId,
                        customDeviceId = customDeviceId,
                        customBuildId = customBuildId,
                        onSuccess = {
                            isLoading = false
                            isRequestSuccess = it
                        },
                        onFailure = {
                            isLoading = false
                            isRequestFailed = it
                        }
                    )
                }
            )
        }

        LoadingIndicator(
            isLoading = isLoading,
            isBackground = true,
            modifier = Modifier.align(Alignment.Center)
        )

        LicenseDeveloper(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )
    }

    FeedbackAlerts(
        isRequestSuccess = isRequestSuccess,
        isRequestFailed = isRequestFailed,
        onSuccessDismiss = { isRequestSuccess = null },
        onFailureDismiss = { isRequestFailed = null },
        event = event
    )
}
