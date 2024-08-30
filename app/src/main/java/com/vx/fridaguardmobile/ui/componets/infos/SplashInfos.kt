package com.vx.fridaguardmobile.ui.componets.infos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vx.fridaguardmobile.R
import com.vx.fridaguardmobile.appconfig.GetArrayConfigs
import com.vx.fridaguardmobile.appconfig.GetValueJson
import com.vx.fridaguardmobile.appconfig.loadConfig
import kotlinx.coroutines.delay
import org.json.JSONObject

@Composable
fun SplashAnimatedTextSteps(onEvent: (String?, Boolean) -> Unit, modifier: Modifier = Modifier) {
    var currentStep by remember { mutableIntStateOf(0) }
    var isVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val splashTextSequence by lazy { GetValueJson.getValueJson("splashTextSequence", context = context).toBoolean() }
    val token by lazy { GetArrayConfigs.getArrayConfigs(0, "public-token", context = context) }

    if (splashTextSequence) {
        HandleStepsAnimation(currentStep, isVisible, modifier, token, onEvent) { newStep, newVisibility ->
            currentStep = newStep
            isVisible = newVisibility
        }
    } else {
        onEvent(token, true)
    }
}

private fun shouldShowSplashTextSequence(config: JSONObject): Boolean {
    return config.getBoolean("splashTextSequence")
}

private fun getTokenPublic(config: JSONObject): String? {
    val configsArray = config.getJSONArray("configs")

    return if (configsArray.length() > 0) {
        configsArray.getJSONObject(0).optString("public-token", null)
    } else {
        null
    }
}

@Composable
private fun HandleStepsAnimation(
    currentStep: Int,
    isVisible: Boolean,
    modifier: Modifier,
    token: String?,
    onEvent: (String?, Boolean) -> Unit,
    updateState: (Int, Boolean) -> Unit
) {
    val steps = getSplashTextSteps()

    LaunchedEffect(currentStep) {
        if (currentStep < steps.size) {
            updateState(currentStep, true)
            delay(1000)
            updateState(currentStep, false)
            delay(2000)
            updateState(currentStep + 1, false)
        }
    }

    if (currentStep in steps.indices) {
        RenderAnimatedTextStep(isVisible, steps[currentStep], modifier)
    } else if (currentStep >= steps.size) {
        onEvent(token, true)
    }
}

@Composable
private fun RenderAnimatedTextStep(
    isVisible: Boolean,
    step: Pair<String, Int?>,
    modifier: Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = fadeOut(),
        modifier = modifier
    ) {
        val (text, icon) = step

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            icon?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(25.dp)
                )
            }

            Text(
                text = text,
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 16.sp
                ),
            )
        }
    }
}

private fun getSplashTextSteps(): List<Pair<String, Int?>> {
    return listOf(
        "Welcome to Frida Guard!" to null,
        "Licensed by Vitor VX" to R.drawable.ic_github,
        "Thanks to the EngModMobile community" to R.drawable.ic_discord
    )
}