package com.ebenezer.gana.datacomm.data.model.repository

import com.ebenezer.gana.datacomm.data.model.Failure
import com.ebenezer.gana.datacomm.data.model.Result
import com.ebenezer.gana.datacomm.data.model.Success
import com.ebenezer.gana.datacomm.data.network.PaytevApi
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

class PayTevRepositoryImpl @Inject constructor(private val paytevService: PaytevApi) : PayTevRepository {

    override suspend fun getBalance(): Result<Double> {
        return try {
            val data = paytevService.getBalance()
            Success(data.balance!!)

        } catch (error: HttpException) {
            Failure(error)
        } catch (error: UnknownHostException) {
            Failure(error)
        } catch (error: Exception) {
            Failure(error)
        }
    }

    override suspend fun buyAirtime(network: String?, phoneNumber: String?, amount: String?): Result<String> {
        return try {
            val data = paytevService.buyAirtime(network, phoneNumber, amount)
            Success(data.details!!)
        } catch (error: HttpException) {
            Failure(error)
        } catch (error: UnknownHostException) {
            Failure(error)
        } catch (error: Exception) {
            Failure(error)
        }
    }

    override suspend fun buyData(network: String?, phoneNumber: String?, amount: String?): Result<String> {
        return try {
            val data = paytevService.buyData(network, phoneNumber, amount)
            Success(data.details!!)
        }catch (error:HttpException){
            Failure(error)
        }catch (error:UnknownHostException){
            Failure(error)
        }catch (error:Exception){
            Failure(error)
        }
    }


}