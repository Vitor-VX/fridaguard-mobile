package com.vx.fridaguardmobile.ui.screens.ui.logincomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.vx.fridaguardmobile.R

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    resource: ImageVector,
    isPassword: Boolean = false,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            imeAction = if (!isPassword) ImeAction.Next else ImeAction.Done,
            keyboardType = if (isPassword) KeyboardType.NumberPassword else KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        label = { Text(label, color = Color(0xFFBBBBBB)) },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = {
                    isPasswordVisible = !isPasswordVisible
                }) {
                    val visibilityOn = painterResource(id = R.drawable.ic_visibility)
                    val visibilityOff = painterResource(id = R.drawable.ic_visibility_off)

                    Icon(
                        painter = if (isPasswordVisible) visibilityOff else visibilityOn,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                        modifier = Modifier.size(26.dp),
                        tint = Color(0xFFBBBBBB)
                    )
                }
            }
        },
        leadingIcon = {
            Icon(
                imageVector = resource,
                contentDescription = null,
                tint = Color(0xFFBBBBBB)
            )
        },
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E1E1E)),
        colors = TextFieldDefaults.colors(
            unfocusedTextColor = Color.White,
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color(0xFF8E2DE2),
            focusedTextColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}