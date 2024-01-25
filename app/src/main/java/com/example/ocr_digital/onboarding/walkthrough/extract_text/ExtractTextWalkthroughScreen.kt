package com.example.ocr_digital.onboarding.walkthrough.extract_text

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
fun ExtractTextWalkthroughScreen(finish: () -> Unit) {
    val scope = rememberCoroutineScope()
    val pageCount by remember { mutableIntStateOf(15) }
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
                title = { Text("Extract Text") },
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
                        painter = painterResource(R.drawable.extract1),
                        contentDescription = stringResource(R.string.extract_text_one),
                        text = stringResource(R.string.extract_text_one),
                        step = 1
                    )
                }
                1 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.extract2),
                        contentDescription = stringResource(R.string.extract_text_two),
                        text = stringResource(R.string.extract_text_two),
                        step = 2
                    )
                }
                2 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.extract3),
                        contentDescription = stringResource(R.string.extract_text_three),
                        text = stringResource(R.string.extract_text_three),
                        step = 3
                    )
                }
                3 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.extract4),
                        contentDescription = stringResource(R.string.extract_text_four),
                        text = stringResource(R.string.extract_text_four),
                        step = 4
                    )
                }
                4 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.extract5),
                        contentDescription = stringResource(R.string.extract_text_five),
                        text = stringResource(R.string.extract_text_five),
                        step = 5
                    )
                }
                5 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.extract6),
                        contentDescription = stringResource(R.string.extract_text_six),
                        text = stringResource(R.string.extract_text_six),
                        step = 6
                    )
                }
                6 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.extract7),
                        contentDescription = stringResource(R.string.extract_text_seven),
                        text = stringResource(R.string.extract_text_seven),
                        step = 7
                    )
                }
                7 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.extract8),
                        contentDescription = stringResource(R.string.extract_text_eight),
                        text = stringResource(R.string.extract_text_eight),
                        step = 8
                    )
                }
                8 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.extract9),
                        contentDescription = stringResource(R.string.extract_text_nine),
                        text = stringResource(R.string.extract_text_nine),
                        step = 9
                    )
                }
                9 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.extract10),
                        contentDescription = stringResource(R.string.extract_text_ten),
                        text = stringResource(R.string.extract_text_ten),
                        step = 10
                    )
                }
                10 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.extract11),
                        contentDescription = stringResource(R.string.extract_text_eleven),
                        text = stringResource(R.string.extract_text_eleven),
                        step = 11
                    )
                }
                11 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.extract12),
                        contentDescription = stringResource(R.string.extract_text_twelve),
                        text = stringResource(R.string.extract_text_twelve),
                        step = 12
                    )
                }
                12 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.extract13),
                        contentDescription = stringResource(R.string.extract_text_thirteen),
                        text = stringResource(R.string.extract_text_thirteen),
                        step = 13
                    )
                }
                13 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.extract14),
                        contentDescription = stringResource(R.string.extract_text_fourteen),
                        text = stringResource(R.string.extract_text_fourteen),
                        step = 14
                    )
                }
                14 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.extract15),
                        contentDescription = stringResource(R.string.extract_text_fifteen),
                        text = stringResource(R.string.extract_text_fifteen),
                        step = 15
                    )
                }
            }
        }
    }
}