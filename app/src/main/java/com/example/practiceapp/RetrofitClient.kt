package com.example.practiceapp

import com.example.practiceapp.data.remote.CoffeeApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.sampleapis.com"

    val coffeeApiService: CoffeeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoffeeApiService::class.java)
    }
}