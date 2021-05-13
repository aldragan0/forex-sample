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
    constructor(initial: Conversion, updated: Conversion) : this(
        initial.amount,
        initial.base,
        initial.date,
        initial.rates + Pair(updated.base, updated.rates.values.first())
    )
}
