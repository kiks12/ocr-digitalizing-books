package com.example.ocr_digital.onboarding.walkthrough.create_folder

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ocr_digital.R
import com.example.ocr_digital.components.HorizontalPagerWalkthroughPage
import com.example.ocr_digital.ui.theme.OcrDigitalTheme
import com.tbuonomo.viewpagerdotsindicator.compose.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.compose.model.DotGraphic
import com.tbuonomo.viewpagerdotsindicator.compose.type.ShiftIndicatorType
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateFolderWalkthroughScreen(finish: () -> Unit) {
    val scope = rememberCoroutineScope()
    val pageCount by remember { mutableIntStateOf(6) }
    val pagerState = rememberPagerState { pageCount }

    Scaffold(
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth()){
                DotsIndicator(
                    dotCount = pageCount,
                    type = ShiftIndicatorType(dotsGraphic = DotGraphic(color = MaterialTheme.colorScheme.primary, size = 6.dp)),
                    pagerState = pagerState
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
                        if (pagerState.currentPage == 5) {
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
                title = { Text("Create Folder") },
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
                        painter = painterResource(R.drawable.create1),
                        contentDescription = stringResource(R.string.create_folder_one),
                        text = stringResource(R.string.create_folder_one),
                        step = 1
                    )
                }
                1 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.create2),
                        contentDescription = stringResource(R.string.create_folder_two),
                        text = stringResource(R.string.create_folder_two),
                        step = 2
                    )
                }
                2 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.create3),
                        contentDescription = stringResource(R.string.create_folder_three),
                        text = stringResource(R.string.create_folder_three),
                        step = 3
                    )
                }
                3 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.create4),
                        contentDescription = stringResource(R.string.create_folder_four),
                        text = stringResource(R.string.create_folder_four),
                        step = 4
                    )
                }
                4 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.create5),
                        contentDescription = stringResource(R.string.create_folder_five),
                        text = stringResource(R.string.create_folder_five),
                        step = 5
                    )
                }
                5 -> {
                    HorizontalPagerWalkthroughPage(
                        painter = painterResource(R.drawable.create6),
                        contentDescription = stringResource(R.string.create_folder_six),
                        text = stringResource(R.string.create_folder_six),
                        step = 6
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun CreateFolderWalkthroughScreenPreview() {
    OcrDigitalTheme {
        CreateFolderWalkthroughScreen {}
    }
}