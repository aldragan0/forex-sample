package com.example.forexsample.api

import com.google.gson.annotations.SerializedName

data class Conversion(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("base")
    val base: Currency,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: Map<Currency, Double>
) {
    constructor(currency: Currency) : this(
        amount = 1,
        base = currency,
        date = "",
        rates = mapOf()
    )

    constructor(initial: Conversion, updated: Conversion) : this(
        updated.amount,
        initial.base,
        updated.date,
        initial.rates + Pair(updated.base, updated.rates.values.first())
    )
}
