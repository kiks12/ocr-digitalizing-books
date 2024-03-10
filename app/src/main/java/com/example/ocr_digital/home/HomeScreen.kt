package com.example.ocr_digital.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocr_digital.R
import com.example.ocr_digital.helpers.ActivityStarterHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val state = homeViewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                actions = {
                    Text(text = "How to use?")
                    IconButton(modifier = Modifier.padding(horizontal = 1.dp), onClick = homeViewModel::startWalkthroughActivity) {
                        Icon(Icons.Outlined.Info, contentDescription = "")
                    }
                }
            )
        }
    ){ innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 15.dp)){
            Text(
                fontSize = 28.sp,
                fontWeight = FontWeight.Medium,
                text = "Welcome,"
            )
            Text(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                text = state.displayName
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                text = "You may now scan books",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(15.dp))
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(5))
                    .shadow(10.dp),
                painter = painterResource(R.drawable.overview),
                contentDescription = "Overview"
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top=15.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Text(text = "Picture")
                Icon(Icons.Default.ArrowForward, contentDescription = "Forward")
                Text(text = "Text")
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    val localContext = LocalContext.current
    val activityStarterHelper = ActivityStarterHelper(localContext)
    HomeScreen(homeViewModel = HomeViewModel(activityStarterHelper))
}