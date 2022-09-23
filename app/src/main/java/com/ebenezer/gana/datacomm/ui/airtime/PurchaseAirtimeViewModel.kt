package com.ebenezer.gana.datacomm.ui.airtime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.datacomm.R
import com.ebenezer.gana.datacomm.data.model.Failure
import com.ebenezer.gana.datacomm.data.model.Success
import com.ebenezer.gana.datacomm.data.model.request.AirtimeRequest
import com.ebenezer.gana.datacomm.data.repository.PayTevRepository
import com.ebenezer.gana.datacomm.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class PurchaseAirtimeViewModel @Inject constructor(
    private val repository: PayTevRepository
) : ViewModel() {

    private val _resultSharedFlow = MutableSharedFlow<UiText?>()
    val resultSharedFlow: SharedFlow<UiText?> = _resultSharedFlow.asSharedFlow()

    /**
     * A function to buy the Actual Airtime from the network
     * @param newAirtimeRequest The airtime request which contains the network id, phone and the amount to be sent to the webserver
     * @exception HttpException Exception thrown from unexpected http response
     * @exception UnknownHostException Exception thrown to indicate that the IP address of a host could not be determined.
     * This is usually when there's no internet connection
     * @exception Exception Exception thrown for unspecific exceptions
     */
    private fun buyNewAirtime(newAirtimeRequest: AirtimeRequest) = try {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result =
                repository.buyAirtime(
                    newAirtimeRequest.network,
                    newAirtimeRequest.phone,
                    newAirtimeRequest.amount
                )) {
                is Success -> {
                    withContext(Dispatchers.Main) {
                        _resultSharedFlow.emit(
                            UiText.DynamicString(result.data)
                        )
                    }
                }
                is Failure -> {
                    when (result.error) {
                        is HttpException -> {
                            withContext(Dispatchers.Main) {
                                _resultSharedFlow.emit(
                                    UiText.StringResource(resId = R.string.err_http_error)
                                )
                            }
                        }

                        is UnknownHostException -> {
                            withContext(Dispatchers.Main) {
                                _resultSharedFlow.emit(
                                    UiText.StringResource(resId = R.string.err_check_internet_connection)
                                )
                            }
                        }

                        is Exception -> {
                            withContext(Dispatchers.Main) {
                                _resultSharedFlow.emit(
                                    UiText.StringResource(resId = R.string.general_exception)
                                )
                            }
                        }
                    }

                }
            }
        }

    } catch (error: Throwable) {
        error.printStackTrace()

    }


    /**
     * A function that will called from PurchaseAirtimeFragment to prepare the selected networkId, PhoneNumber, and amount to purchase Airtime
     * @param network the selected network ID
     * @param phone phone number to be credited
     */
    fun buyAirtime(network: String, phone: String, amount: String) {
        val newAirtime = AirtimeRequest(network, phone, amount)
        buyNewAirtime(newAirtime)
    }
}