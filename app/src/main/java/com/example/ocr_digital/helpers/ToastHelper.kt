package com.example.ocr_digital.helpers

import android.content.Context
import android.widget.Toast

class ToastHelper(private val context: Context){

    fun makeToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}