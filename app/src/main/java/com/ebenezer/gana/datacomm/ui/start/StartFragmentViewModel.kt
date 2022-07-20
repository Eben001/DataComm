package com.ebenezer.gana.datacomm.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ebenezer.gana.datacomm.R
import com.ebenezer.gana.datacomm.data.model.Failure
import com.ebenezer.gana.datacomm.data.model.Success
import com.ebenezer.gana.datacomm.data.model.repository.PayTevRepository
import com.ebenezer.gana.datacomm.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class StartFragmentViewModel @Inject constructor(
    private val repository: PayTevRepository
) : ViewModel() {

    private val _balance = MutableLiveData<Double?>()
    val balance: LiveData<Double?> = _balance

    private val _error = MutableLiveData<UiText>()
    val error: LiveData<UiText> = _error

    private val _greetingText = MutableLiveData<UiText>()
    val greetingText: LiveData<UiText> = _greetingText

    private val _isBalanceLoaded = MutableLiveData(false)
    val isBalanceLoaded: LiveData<Boolean> = _isBalanceLoaded


    /**
     * A function to set the Greeting's message according to the hour of the day
     * @param hourOfDay the hour of the day in a 24hour time
     */
    fun setGreetingMessage(hourOfDay: Int) {
        when (hourOfDay) {
            in 1..11 -> _greetingText.value = UiText.StringResource(R.string.greeting_morning)
            in 12..16 -> _greetingText.value = UiText.StringResource(R.string.greeting_afternoon)
            in 17..23 -> _greetingText.value = UiText.StringResource(R.string.greeting_evening)

            else -> {
                _greetingText.value = UiText.StringResource(R.string.greeting_morning)
            }
        }

    }

    /**
     * A function to get user's balance from the network
     * @exception UnknownHostException Exception thrown to indicate that the IP address of a host could not be determined.
     * This is usually when there's no internet connection
     * @exception Exception catches general exception
     */
    fun getBalance() = try {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getBalance()) {
                is Success -> {
                    withContext(Dispatchers.Main) {
                        _balance.value = result.data
                        _isBalanceLoaded.value = true

                    }
                }
                is Failure -> {
                    when (result.error) {
                        is UnknownHostException -> {
                            withContext(Dispatchers.Main) {
                                _error.value =
                                    UiText.StringResource(resId = R.string.err_unable_to_load_balance)
                            }
                        }
                        is Exception -> {
                            withContext(Dispatchers.Main) {
                                _error.value =
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
}