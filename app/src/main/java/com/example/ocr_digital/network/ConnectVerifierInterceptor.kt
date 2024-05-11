package com.example.ocr_digital.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectVerifierInterceptor(val isNetworkAvailable: () -> Boolean): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable()){
            throw IOException("No Network Available!")
        }
        val request = chain.request()
        return chain.proceed(request)
    }
}