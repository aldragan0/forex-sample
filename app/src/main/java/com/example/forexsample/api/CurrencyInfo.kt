package com.example.forexsample.api

import java.io.Serializable

data class CurrencyInfo<T>(
    val currency: Currency,
    val value: T
) : Serializable {
    constructor(entry: Map.Entry<Currency, T>) : this(entry.key, entry.value)
}