package com.vx.fridaguardmobile.ui.componets.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.vx.fridaguardmobile.R

@Composable
fun LoadingIndicator(isLoading: Boolean, isBackground: Boolean = false, modifier: Modifier = Modifier) {
    if (isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = if (isBackground) Color(0xFF0C0517).copy(alpha = 0.5f) else Color.Transparent)
                .zIndex(1f)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {}
                )
        ) {
            CircularProgressIndicator(
                color = Color(0xFF8E2DE2),
                modifier = modifier
            )
        }
    }
}