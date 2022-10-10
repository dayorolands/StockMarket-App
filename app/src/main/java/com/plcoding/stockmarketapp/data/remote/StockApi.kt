package com.plcoding.stockmarketapp.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(
        @Query("apikey") apikey: String = API_KEY
    ) : ResponseBody

    companion object{
        const val API_KEY = "NSPQPGN3RUMSF71L"
        const val BASE_URL = "https://alphavantage.co"
    }
}