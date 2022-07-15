package com.ebenezer.gana.datacomm.data.model.repository

import com.ebenezer.gana.datacomm.data.model.Result

interface PayTevRepository {

    suspend fun getBalance(): Result<Double>
    suspend fun buyAirtime(network: String?, phoneNumber: String?, amount: String?): Result<String>
    suspend fun buyData(network: String?, phoneNumber: String?, amount: String?): Result<String>


}