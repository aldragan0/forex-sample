package com.example.forexsample.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    @GET("/currencies")
    fun getCurrencies(): Call<Map<Currency, String>>

    @GET("/latest")
    fun getRate(
        @Query("from") from: Currency,
        @Query("to") to: Currency
    ): Call<Conversion>
}