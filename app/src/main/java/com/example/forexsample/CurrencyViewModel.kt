package com.example.forexsample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.forexsample.api.CurrencyApi
import com.example.forexsample.api.CurrencyInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class CurrencyViewModel : ViewModel() {
    private val backgroundScope = CoroutineScope(Dispatchers.Default)

    private val _items = MutableLiveData(listOf<CurrencyInfo>())
    private val _loading = MutableLiveData(true)
    private val _error = MutableLiveData(false)

    val items: LiveData<List<CurrencyInfo>> = _items
    val loading: LiveData<Boolean> = _loading
    val error: LiveData<Boolean> = _error

    fun loadCurrencies() {
        backgroundScope.launch {
            fetchCurrencies()
                .also {
                    _loading.postValue(false)
                    _error.postValue(it.isNullOrEmpty())
                }?.also {
                    _items.postValue(it.map(::CurrencyInfo))
                }
        }
    }

    private suspend fun fetchCurrencies() = CurrencyApi.handleRequest {
        CurrencyApi.service.getCurrencies().awaitResponse()
    }

}
