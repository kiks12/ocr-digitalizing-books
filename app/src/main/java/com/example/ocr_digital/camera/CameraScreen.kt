package com.example.ocr_digital.camera

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(cameraViewModel: CameraViewModel) {
    val cameraPermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        LocalContext.current.packageName + ".provider", file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cropImageLauncher = rememberLauncherForActivityResult(CropImageContract()) { cropResult ->
        if (cropResult.uriContent != null) {
            cameraViewModel.process(context, cropResult.uriContent!!)
        }
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
            cropImageLauncher.launch(
                input = CropImageContractOptions(
                    uri = uri,
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
    
    LaunchedEffect(cameraPermissionState) {
        val permissionResult = cameraPermissionState.status
        if (!permissionResult.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        } else {
            cameraLauncher.launch(uri)
        }
    }


    Scaffold { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Take a Picture",
                fontSize = 30.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(15.dp))

            if (cameraPermissionState.status.isGranted) {
                Button(onClick = { cameraLauncher.launch(uri) }) {
                    Text("Open Camera")
                }
                cameraLauncher.launch(uri)
            } else {
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text(text = "Allow Camera")
                }
            }

            FilledTonalButton(onClick = cameraViewModel::finish) {
                Text(text = "Go Back")
            }
        }
    }
}


@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmm-ss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
}
