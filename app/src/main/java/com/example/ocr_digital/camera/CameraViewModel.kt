package com.example.ocr_digital.camera

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.ocr_digital.helpers.ToastHelper
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException

class CameraViewModel(
     private val toastHelper: ToastHelper,
     private val finishCallback: () -> Unit,
     private val returnData: (str: String) -> Unit
) : ViewModel() {
     private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)


     fun process(context: Context, uri: Uri) {
          try {
               val image = InputImage.fromFilePath(context, uri)
               recognizer.process(image)
                    .addOnSuccessListener { result ->
                         var text = ""
                         result.textBlocks.forEach { textBlock ->
                              textBlock.lines.forEach { line ->
                                   text += if (text.isEmpty() || text.last() == '\n') {
                                        line.text
                                   } else {
                                        " ${line.text}"
                                   }
                              }

                              text += if (text.isEmpty()) {
                                   ""
                              } else {
                                   "\n\n"
                              }
                         }
                         if (result.text.isEmpty()) {
                              toastHelper.makeToast("No Text extracted from image")
                         }

                         returnData(text)
                    }
                    .addOnFailureListener {  }
          } catch (e : IOException) {
               e.localizedMessage?.let { toastHelper.makeToast(it) }
               returnData("")
          }
     }

     fun finish() {
          finishCallback()
     }
}