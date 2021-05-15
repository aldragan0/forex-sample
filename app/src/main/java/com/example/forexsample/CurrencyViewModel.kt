package com.example.forexsample

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.forexsample.api.CurrencyApi
import com.example.forexsample.api.CurrencyInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await

class CurrencyViewModel : ViewModel() {
    private val _items = MutableLiveData(listOf<CurrencyInfo<String>>())
    private val _loading = MutableLiveData(true)
    private val _error = MutableLiveData(false)

    val items: LiveData<List<CurrencyInfo<String>>> = _items
    val loading: LiveData<Boolean> = _loading
    val error: LiveData<Boolean> = _error

    fun loadCurrencies() {
        viewModelScope.launch(Dispatchers.Default) {
            runCatching { fetchCurrencies() }
                .onSuccess {
                    _error.postValue(false)
                    _items.postValue(it.map(::CurrencyInfo))
                }
                .onFailure {
                    Log.w("loadCurrencies", it)
                    _error.postValue(true)
                }
            _loading.postValue(false)
        }
    }

    private suspend fun fetchCurrencies() =
        CurrencyApi.handleRequest {
            CurrencyApi.service.getCurrencies().await()
        }
}
