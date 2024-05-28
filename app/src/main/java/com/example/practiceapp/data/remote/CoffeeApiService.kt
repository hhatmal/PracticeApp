package com.example.practiceapp.data.remote

import retrofit2.http.GET


interface CoffeeApiService {
    @GET("coffee/hot")
    suspend fun getCoffees() : List<CoffeeDto>
}