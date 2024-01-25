package com.example.ocr_digital.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocr_digital.R
import com.example.ocr_digital.ui.theme.OcrDigitalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HorizontalPagerWalkthroughPage(painter: Painter, contentDescription: String, text: String, step: Int) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = Modifier.size(530.dp)
            )
        }
        item { Spacer(modifier = Modifier.height(15.dp)) }
        item {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Badge(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ){
                    Text(text = "Step $step", fontSize = 12.sp, modifier = Modifier.padding(5.dp))
                }
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = text, textAlign = TextAlign.Center)
            }
        }
    }
}

@Preview
@Composable
fun HorizontalPagerWalkthroughPagePreview() {
    val painter = painterResource(R.drawable.create1)
    val text = stringResource(R.string.create_folder_six)

    OcrDigitalTheme {
        Scaffold { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                HorizontalPagerWalkthroughPage(painter = painter, contentDescription = text, text = text, step = 1)
            }
        }
    }
}