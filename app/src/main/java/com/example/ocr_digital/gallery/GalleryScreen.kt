package com.example.ocr_digital.gallery

import android.Manifest
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
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

    val cropImageLauncher = rememberLauncherForActivityResult(CropImageContract()) { cropResult ->
        if (cropResult.uriContent != null) {
            Log.w("GALLERY SCREEN", "Process Image")
            cameraViewModel.process(context, cropResult.uriContent!!)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        capturedImageUri = it!!
        Log.w("GALLERY SCREEN", it.path!!)
        Log.w("GALLERY SCREEN", "LAUNCH IMAGE CROPPER")
        cropImageLauncher.launch(
            input = CropImageContractOptions(
                uri = capturedImageUri,
                cropImageOptions = CropImageOptions(
                    toolbarColor = Color.Black.toArgb(),
                    activityTitle = "Crop Image",
                    cropMenuCropButtonTitle = "Crop",
                    toolbarBackButtonColor = Color.White.toArgb()
                )
            ),
            options = null
        )
    }

    LaunchedEffect(galleryLauncher) {
        if (cameraPermissionState.status.isGranted) {
            galleryLauncher.launch("image/*")
        } else {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { galleryLauncher.launch("image/*") }) {
                Text(text = "Select Image")
            }
        }
    }
}