package com.vx.fridaguardmobile.ui.componets.utils

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vx.fridaguardmobile.R
import kotlinx.coroutines.delay

@Composable
fun AnimatedText(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            for ((index, char) in text.withIndex()) {
                AnimatedTextChar(char = char, delay = index * 100L)
            }
        }
    }
}

@Composable
fun AnimatedTextChar(char: Char, delay: Long) {
    var startAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(delay)
        startAnimation = true
    }

    val transition = updateTransition(targetState = startAnimation, label = "CharAnimation")

    val offsetY by transition.animateDp(
        transitionSpec = {
            if (false isTransitioningTo true) {
                tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            } else {
                spring(stiffness = Spring.StiffnessLow)
            }
        }, label = "offsetY"
    ) { animated ->
        if (animated) 0.dp else 100.dp
    }

    val alpha by transition.animateFloat(
        transitionSpec = {
            if (false isTransitioningTo true) {
                tween(durationMillis = 1500)
            } else {
                spring(stiffness = Spring.StiffnessLow)
            }
        }, label = "alpha"
    ) { animated ->
        if (animated) 1f else 0f
    }

    Box(
        modifier = Modifier
            .offset(y = offsetY)
            .alpha(alpha)
    ) {
        Text(
            text = char.toString(),
            color = Color(0xFF9C27B0), // Roxo
            fontFamily = FontFamily(Font(resId = R.font.poppins_semibold)),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(horizontal = 1.dp)
        )
    }
}
