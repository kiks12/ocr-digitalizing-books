package com.example.ocr_digital.helpers

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private const val BASE_URL = "https://ocr-digitalizing-books-server.onrender.com"

    fun getInstance(): Retrofit? {
        return try {
            Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        } catch (error: Exception) {
            error.localizedMessage?.let { Log.w("RETROFIT ERROR", it) }
            null
        }
    }
}