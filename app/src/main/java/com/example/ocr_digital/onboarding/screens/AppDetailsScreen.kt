package com.example.ocr_digital.onboarding.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocr_digital.ui.theme.OcrDigitalTheme

@Composable
fun AppDetailsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(text = "Digitalizing Books!", fontSize = 30.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "With our OCR technology, effortlessly convert images to text and unlock a new level of productivity. Whether it's extracting information from documents or simplifying your workflow, our app is here to make your life easier",
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun AppDetailsScreenPreview() {
    OcrDigitalTheme {
        Scaffold {
            Box(modifier = Modifier.padding(it)) {
                AppDetailsScreen()
            }
        }
    }
}
