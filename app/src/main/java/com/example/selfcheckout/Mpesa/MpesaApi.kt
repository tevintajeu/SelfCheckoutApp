package com.example.selfcheckout.Mpesa

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MpesaApi {

    @GET("oauth/v1/generate?grant_type=client_credentials")
    fun generateToken(
        @Header("Authorization") authHeader: String,
    ): Call<AccessTokenResponse>

    @POST("mpesa/stkpush/v1/processrequest")
    fun initiateSTKPush(
        @Header("Authorization") authHeader: String, // Bearer token for authorization
        @Body stkPushRequest: StkPushRequest // Body containing the payment details
    ): Call<StkPushResponse>
}
