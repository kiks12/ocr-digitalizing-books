package com.example.ocr_digital.components.plain_text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PlainTextEditor(
    text: String,
    onTextChanged: (String) -> Unit
) {
    val focusRequester = FocusRequester()

    Box(
        modifier = Modifier.fillMaxSize().shadow(4.dp)
    ) {
        PlainTextCanvas(
            text = text,
            onTextChanged = onTextChanged,
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .background(Color.White)
                .clip(MaterialTheme.shapes.small)
                .padding(16.dp)
                .height(500.dp)
        )

        DisposableEffect(Unit) {
            focusRequester.requestFocus()
            onDispose { }
        }
    }
}
