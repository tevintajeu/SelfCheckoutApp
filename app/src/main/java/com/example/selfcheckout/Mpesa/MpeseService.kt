package com.example.selfcheckout.Mpesa

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MpesaService {
    private const val BASE_URL = "https://sandbox.safaricom.co.ke/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
