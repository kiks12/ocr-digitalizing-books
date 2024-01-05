package com.example.ocr_digital.components.plain_text

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import com.example.ocr_digital.ui.theme.OcrdigitalTheme

@Composable
fun PlainTextCanvas(
    text: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = text,
        onValueChange = { newText -> onTextChanged(newText) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        modifier = modifier
    )
}


@Preview
@Composable
fun PlainTextCanvasPreview() {
    OcrdigitalTheme {
        PlainTextCanvas(
            text = "Hello, this is a preview",
            onTextChanged = {}
        )
    }
}
