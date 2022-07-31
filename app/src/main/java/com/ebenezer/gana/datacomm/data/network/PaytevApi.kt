package com.ebenezer.gana.datacomm.data.network

import com.ebenezer.gana.datacomm.data.model.Balance
import com.ebenezer.gana.datacomm.data.model.response.AirtimeResponse
import com.ebenezer.gana.datacomm.data.model.response.DataResponse
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Holds the API calls for the DataComm app.
 */
interface PaytevApi {
    @GET("balance?format=json")
    suspend fun getBalance(): Balance

    @GET("airtime?format=json")
    suspend fun buyAirtime(
        @Query("network") network: String?,
        @Query("phone") phoneNumber: String?,
        @Query("amount") amount: String?

    ): AirtimeResponse

    @GET("data?format=json")
    suspend fun buyData(
        @Query("network") network: String?,
        @Query("phone") phoneNumber: String?,
        @Query("amount") amount: String?,
    ): DataResponse
}