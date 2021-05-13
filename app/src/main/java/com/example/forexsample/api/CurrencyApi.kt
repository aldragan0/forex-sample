package com.example.forexsample.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

object CurrencyApi {
    private const val ROOT_URL = "https://www.frankfurter.app"
    private val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl(ROOT_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: CurrencyService = retrofit.create(CurrencyService::class.java)

    suspend fun <T> handleRequest(
        coroutineContext: CoroutineContext = Dispatchers.IO,
        request: suspend () -> Response<T>,
    ): T? =
        withContext(coroutineContext) {
            val response = request()
            //TODO: remove log line
            Log.i("call request", response.toString())
            if (!response.isSuccessful) {
                Log.w(
                    "handleRequest",
                    "Failed to handle Request[$request, response-code=${response.code()}, message=${response.message()}"
                )
            }
            return@withContext response.body()
        }
}