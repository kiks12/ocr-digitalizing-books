package com.example.ocr_digital.helpers

import android.content.Context
import android.content.Intent

class ActivityStarterHelper(private val context: Context){

    fun startActivity(activity: Class<*>, stringExtras: Map<String, String> = mapOf()) {
        val intent = Intent(context, activity)
        stringExtras.forEach {
            intent.putExtra(it.key, it.value)
        }
        context.startActivity(intent)
    }
}