package com.example.ocr_digital.gallery

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocr_digital.camera.CameraViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GalleryScreen(cameraViewModel: CameraViewModel) {

    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    val context = LocalContext.current
    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

//    val cropImageLauncher = rememberLauncherForActivityResult(CropImageContract()) { cropResult ->
//        if (cropResult.uriContent != null) {
//            cameraViewModel.process(context, cropResult.uriContent!!)
//        }
//    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it == null) return@rememberLauncherForActivityResult
        capturedImageUri = it
        cameraViewModel.process(context, capturedImageUri)
//        cropImageLauncher.launch(
//            input = CropImageContractOptions(
//                uri = capturedImageUri,
//                cropImageOptions = CropImageOptions(
//                    toolbarColor = Color.Black.toArgb(),
//                    activityTitle = "Crop Image",
//                    cropMenuCropButtonTitle = "Crop",
//                    toolbarBackButtonColor = Color.White.toArgb()
//                )
//            ),
//            options = null
//        )
    }

    LaunchedEffect(galleryLauncher) {
        if (cameraPermissionState.status.isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Select from Gallery",
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(15.dp))
            Button(onClick = { galleryLauncher.launch("image/*") }) {
                Text(text = "Select Image")
            }

            FilledTonalButton(onClick = cameraViewModel::finish) {
                Text(text = "Go Back")
            }
        }
    }
}