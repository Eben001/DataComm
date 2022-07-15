package com.ebenezer.gana.datacomm

import android.app.Application
import com.ebenezer.gana.datacomm.data.model.repository.PayTevRepository
import com.ebenezer.gana.datacomm.data.model.repository.PayTevRepositoryImpl
import com.ebenezer.gana.datacomm.data.network.buildApiService

class App : Application() {


    companion object {

        private val apiService by lazy { buildApiService() }
        val repository:PayTevRepository by lazy {
            PayTevRepositoryImpl(apiService)
        }

    }


}