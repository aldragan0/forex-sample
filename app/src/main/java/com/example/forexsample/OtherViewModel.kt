package com.example.forexsample

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forexsample.api.Conversion
import com.example.forexsample.api.Currency
import com.example.forexsample.api.CurrencyApi
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class OtherViewModel : ViewModel() {
    private val _conversionRates = MutableLiveData(listOf<Conversion>())
    private val _loading = MutableLiveData(true)

    val conversionRates: LiveData<List<Conversion>> = _conversionRates
    val loading: LiveData<Boolean> = _loading

    fun loadConversionRates(referenceCurrency: Currency) {
        viewModelScope.launch {
            if (_conversionRates.value!!.any { it.base == referenceCurrency }) {
                _loading.value = false
            }

            //TODO: remove this log line
            Log.i(
                "loadConversionRates",
                "Thread[${Thread.currentThread().name}, ${Thread.currentThread().id}] entering here with: cLoading=${_loading.value}, loading=${_loading.value}"
            )
            if (_loading.value == null || _loading.value == false) {
                return@launch
            }

            kotlin.runCatching { }

            Currency.values()
                .filterNot { it == referenceCurrency }
                .subList(0, 1)
                .mapNotNull { fetchConversionRate(it, referenceCurrency) }
                .reduce(::Conversion)
                .also {
                    _loading.value = false
                    _conversionRates.value = conversionRates.value!! + it
                }
        }
    }

    private suspend fun fetchConversionRate(from: Currency, to: Currency) =
        CurrencyApi.handleRequest {
            CurrencyApi.service.getRate(from, to).awaitResponse()
        }
}