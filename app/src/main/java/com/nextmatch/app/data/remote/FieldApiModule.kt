package com.nextmatch.app.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object FieldApiModule {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val service: FieldApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(FieldApiService::class.java)
    }
}
