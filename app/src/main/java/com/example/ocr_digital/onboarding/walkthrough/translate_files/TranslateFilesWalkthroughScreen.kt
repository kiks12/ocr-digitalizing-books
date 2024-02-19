package com.example.ocr_digital.onboarding.walkthrough.translate_files

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ocr_digital.R
import com.example.ocr_digital.components.HorizontalPagerWalkthroughPage
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TranslateFilesWalkthroughScreen(finish: () -> Unit) {
    val scope = rememberCoroutineScope()
    val pageCount by remember { mutableIntStateOf(7) }
    val pagerState = rememberPagerState { pageCount }

    Scaffold(
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth()){
                DotsIndicator(
                    dotCount = pageCount,
                    type = ShiftIndicatorType(dotsGraphic = DotGraphic(color = MaterialTheme.colorScheme.primary, size = 6.dp)),
                    pagerState = pagerState,
                )
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
                        Text("Prev")
                    }
                    TextButton(onClick = {
                        if (pagerState.currentPage == 14) {
                            finish()
                        } else {
                            scope.launch {
                                pagerState.scrollToPage(pagerState.currentPage+1)
                            }
                        }
                    }) {
                        Text(if (pagerState.currentPage == 5) "Done" else "Next")
                    }
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Translate Material") },
                actions = {
                    TextButton(onClick = { finish() }) {
                        Text(text = "Skip")
                    }
                }
            )
        }
    ){ innerPadding ->
        HorizontalPager(state = pagerState, modifier = Modifier
            .padding(innerPadding)
            .padding(15.dp)
            .fillMaxSize()) {
            when (pagerState.currentPage) {
                0 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.translate1),
                        contentDescription = stringResource(R.string.translate_one),
                        text = stringResource(R.string.translate_one),
                        step = 1
                    )
                }
                1 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.translate2),
                        contentDescription = stringResource(R.string.translate_two),
                        text = stringResource(R.string.translate_two),
                        step = 2
                    )
                }
                2 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.translate3),
                        contentDescription = stringResource(R.string.translate_three),
                        text = stringResource(R.string.translate_three),
                        step = 3
                    )
                }
                3 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.translate4),
                        contentDescription = stringResource(R.string.translate_four),
                        text = stringResource(R.string.translate_four),
                        step = 4
                    )
                }
                4 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.translate5),
                        contentDescription = stringResource(R.string.translate_five),
                        text = stringResource(R.string.translate_five),
                        step = 5
                    )
                }
                5 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.translate6),
                        contentDescription = stringResource(R.string.translate_six),
                        text = stringResource(R.string.translate_six),
                        step = 6
                    )
                }
                6 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.translate7),
                        contentDescription = stringResource(R.string.translate_seven),
                        text = stringResource(R.string.translate_seven),
                        step = 7
                    )
                }
            }
        }
    }
}