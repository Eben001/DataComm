package com.ebenezer.gana.datacomm.ui.airtime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.datacomm.App.Companion.repository
import com.ebenezer.gana.datacomm.R
import com.ebenezer.gana.datacomm.data.model.Failure
import com.ebenezer.gana.datacomm.data.model.Success
import com.ebenezer.gana.datacomm.data.model.request.AirtimeRequest
import com.ebenezer.gana.datacomm.utils.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException

class PurchaseAirtimeViewModel : ViewModel() {


    private val _result = MutableLiveData<UiText>()
    val result: LiveData<UiText> = _result

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
                repository.buyAirtime(newAirtimeRequest.network, newAirtimeRequest.phone, newAirtimeRequest.amount)) {
                is Success -> {
                    withContext(Dispatchers.Main) {
                        _result.value = UiText.DynamicString(result.data)
                    }
                }
                is Failure -> {
                    when (result.error) {
                        is HttpException -> {
                            withContext(Dispatchers.Main) {
                                _result.value =
                                    UiText.StringResource(resId = R.string.err_http_error)
                            }
                        }

                        is UnknownHostException -> {
                            withContext(Dispatchers.Main) {
                                _result.value =
                                    UiText.StringResource(resId = R.string.err_check_internet_connection)
                            }
                        }

                        is Exception -> {
                            withContext(Dispatchers.Main) {
                                _result.value =
                                    UiText.StringResource(resId = R.string.general_exception)
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