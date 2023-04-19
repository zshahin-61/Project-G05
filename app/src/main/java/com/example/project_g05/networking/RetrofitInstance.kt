package com.example.project_g05.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

 object RetrofitInstance {

        private val httpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor(
                HttpLoggingInterceptor.Logger { message ->
                    println("LOG-APP: $message")
                }).apply {
                level= HttpLoggingInterceptor.Level.BODY
            })
            .build()

        private const val BASE_URL =" https://developer.nps.gov"

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }

    }