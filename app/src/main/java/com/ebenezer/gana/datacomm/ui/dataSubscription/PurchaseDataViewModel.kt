package com.ebenezer.gana.datacomm.ui.dataSubscription

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.datacomm.R
import com.ebenezer.gana.datacomm.data.model.Failure
import com.ebenezer.gana.datacomm.data.model.Success
import com.ebenezer.gana.datacomm.data.repository.PayTevRepository
import com.ebenezer.gana.datacomm.data.model.request.DataRequest
import com.ebenezer.gana.datacomm.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class PurchaseDataViewModel @Inject constructor(
    private val repository:PayTevRepository
) : ViewModel() {

    private val _result = MutableLiveData<UiText>()
    val result: LiveData<UiText> = _result

    /**
     * A function to buy the Actual Data from the network
     * @param newDataRequest The data request which contains the network id, phone and the amount to be sent to the webserver
     * @exception HttpException Exception thrown from unexpected http response
     * @exception UnknownHostException Exception thrown to indicate that the IP address of a host could not be determined.
     * This is usually when there's no internet connection
     * @exception Exception Exception thrown for unspecific exceptions
     */
    private fun buyNewData(newDataRequest: DataRequest) {
        viewModelScope.launch(Dispatchers.IO) {

            when (val result = repository.buyData(newDataRequest.network, newDataRequest.phone, newDataRequest.amount)) {
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

    }

    /**
     * A function that will called from PurchaseDataFragment to prepare the selected networkId, PhoneNumber, and amount to purchase Airtime
     * @param network the selected network ID
     * @param phone phone number to be credited
     */
    fun buyData(network: String, phone: String, amount: String) {
        val newData = DataRequest(network, phone, amount)
        buyNewData(newData)
    }

}