package com.vx.fridaguardmobile.ui.componets.developer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vx.fridaguardmobile.R
import kotlinx.coroutines.delay

@Composable
fun LicenseDeveloper(modifier: Modifier = Modifier) {
    val visible = remember { MutableTransitionState(false).apply { targetState = false } }

    LaunchedEffect(Unit) {
        delay(1000)
        visible.targetState = true
    }

    AnimatedVisibility(
        visibleState = visible,
        modifier = modifier,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Licensed by Vitor VX", color = Color.Gray, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(5.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_github),
                contentDescription = "ic_github",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier.size(25.dp)
            )
        }
    }
}