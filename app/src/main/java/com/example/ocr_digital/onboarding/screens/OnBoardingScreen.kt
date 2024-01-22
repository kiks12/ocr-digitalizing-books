package com.example.ocr_digital.onboarding.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ocr_digital.ui.theme.OcrdigitalTheme
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(startHomeActivity: () -> Unit) {
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val pageCount by remember { mutableIntStateOf(3) }
    val pagerState = rememberPagerState{ pageCount }

    Scaffold(
        bottomBar = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    DotsIndicator(
                        dotCount = pageCount,
                        type = ShiftIndicatorType(dotsGraphic = DotGraphic(color = MaterialTheme.colorScheme.primary)),
                        pagerState = pagerState
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    TextButton(onClick = {
                        if (pagerState.currentPage == 0) return@TextButton
                        scope.launch {
                            pagerState.scrollToPage(pagerState.currentPage-1)
                        }
                    }) {
                        Text(text = "Back")
                    }
                    TextButton(onClick = {
                        if (pagerState.currentPage == 2) {
                            loading = true
                            startHomeActivity()
                        } else {
                            scope.launch {
                                pagerState.scrollToPage(pagerState.currentPage+1)
                            }
                        }
                    }) {
                        Text(
                            text = if (pagerState.currentPage == 2) {
                                "Done"
                            } else {
                                "Next"
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.padding(innerPadding)
            ) {
                if (pagerState.currentPage == 0) {
                    WelcomeScreen()
                }
                if (pagerState.currentPage == 1) {
                    AppDetailsScreen()
                }
                if (pagerState.currentPage == 2) {
                    GetStartedScreen()
                }
            }
        }
    }
}

@Preview
@Composable
fun OnBoardingScreenPreview() {
    OcrdigitalTheme {
        OnBoardingScreen{}
    }
}

