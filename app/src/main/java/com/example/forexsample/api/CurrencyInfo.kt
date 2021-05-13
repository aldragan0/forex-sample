package com.example.forexsample.api

import java.io.Serializable

data class CurrencyInfo(
    val currency: Currency,
    val description: String
) : Serializable {
    constructor(entry: Map.Entry<Currency, String>) : this(entry.key, entry.value)
}