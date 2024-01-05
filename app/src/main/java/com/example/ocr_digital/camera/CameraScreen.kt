package com.example.ocr_digital.camera

import android.content.Context
import android.graphics.drawable.Icon
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.mr0xf00.easycrop.CropError
import com.mr0xf00.easycrop.CropResult
import com.mr0xf00.easycrop.crop
import com.mr0xf00.easycrop.rememberImageCropper
import com.mr0xf00.easycrop.ui.ImageCropperDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(cameraViewModel: CameraViewModel) {
    val cameraPermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        LocalContext.current.packageName + ".provider", file
    )
//    val imageCropper = rememberImageCropper()

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cropImageLauncher = rememberLauncherForActivityResult(CropImageContract()) { cropResult ->
        Log.w("CAMERA SCREEN", cropResult.uriContent.toString())
        Log.w("CAMERA SCREEN", cropResult.originalUri.toString())
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
//            scope.launch {
//                val result = imageCropper.crop(file)
//                when (result) {
//                    CropResult.Cancelled -> { }
//                    is CropError -> { }
//                    is CropResult.Success -> { result.bitmap }
//                }
//            }
//            cropImageLauncher.launch(
//                input = CropImageContractOptions(
//                    uri = uri,
//                    cropImageOptions = CropImageOptions(
//                        toolbarColor = Color.Black.toArgb(),
//                        activityTitle = "Crop Image",
//                        cropMenuCropButtonTitle = "Crop",
//                        toolbarBackButtonColor = Color.White.toArgb()
//                    )
//                ),
//                options = null
//            )
        }
    
    LaunchedEffect(cameraPermissionState) {
        val permissionResult = cameraPermissionState.status
        if (!permissionResult.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }


    Scaffold { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(10.dp)
        ) {
            if (cameraPermissionState.status.isGranted) {
                cameraLauncher.launch(uri)
            } else {
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text(text = "Allow Camera")
                }
            }
//            if (imageCropper.cropState != null) {
//                ImageCropperDialog(state = imageCropper.cropState!!)
//            }
        }
    }
}


fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}
