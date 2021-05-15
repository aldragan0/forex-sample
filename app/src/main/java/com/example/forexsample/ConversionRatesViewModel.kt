package com.example.forexsample

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forexsample.api.Conversion
import com.example.forexsample.api.Currency
import com.example.forexsample.api.CurrencyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await

class ConversionRatesViewModel : ViewModel() {
    private val _conversionRates = MutableLiveData(listOf<Conversion>())
    private val _loading = MutableLiveData(false)
    private val _error = MutableLiveData(false)

    val conversionRates: LiveData<List<Conversion>> = _conversionRates
    val loading: LiveData<Boolean> = _loading
    val error: LiveData<Boolean> = _error

    fun loadConversionRates(referenceCurrency: Currency) {
        viewModelScope.launch(Dispatchers.Default) {
            if (_conversionRates.value!!.any { it.base == referenceCurrency }) {
                return@launch
            } else if (_loading.value == false) {
                _error.postValue(false)
                _loading.postValue(true)
                return@launch
            }

            runCatching {
                Currency.values()
                    .filterNot { it == referenceCurrency }
                    .map { fetchConversionRate(it, referenceCurrency) }
                    .fold(Conversion(referenceCurrency), ::Conversion)
            }.onSuccess {
                _conversionRates.postValue(_conversionRates.value!! + it)
            }.onFailure {
                Log.w("loadConversionRates", it)
                _error.postValue(true)
            }
            _loading.postValue(false)
        }
    }

    private suspend fun fetchConversionRate(from: Currency, to: Currency) =
        CurrencyApi.handleRequest {
            CurrencyApi.service.getRate(from, to).await()
        }
}