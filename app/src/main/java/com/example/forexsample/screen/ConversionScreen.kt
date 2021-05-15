package com.example.forexsample.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.forexsample.api.Conversion
import com.example.forexsample.api.Currency
import com.example.forexsample.api.CurrencyInfo
import com.example.forexsample.viewmodel.ConversionRatesViewModel

@Composable
fun ConversionScreen(
    conversionRatesViewModel: ConversionRatesViewModel,
    selected: Currency?,
) {
    val rates: List<Conversion> by conversionRatesViewModel.conversionRates.observeAsState(listOf())
    val loading: Boolean by conversionRatesViewModel.loading.observeAsState(false)
    val error: Boolean by conversionRatesViewModel.error.observeAsState(false)

    if (selected == null) {
        Toast.makeText(LocalContext.current, "Invalid selected currency", Toast.LENGTH_SHORT)
            .show()
        return
    }
    conversionRatesViewModel.loadConversionRates(selected)

    when {
        loading -> {
            CircularProgressIndicator()
        }
        !loading -> {
            rates.firstOrNull { it.base == selected }
                ?.also { ConversionList(it) }
        }
        error -> {
            Toast.makeText(
                LocalContext.current,
                "Failed to fetch conversion rate for $selected",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

@Composable
fun ConversionList(conversion: Conversion) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(32.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Currency: ${conversion.base}, amount: ${conversion.amount}",
                    textAlign = TextAlign.Center,
                )
            }
            ItemList(
                items = conversion.rates.map(::CurrencyInfo),
                showDetails = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemPreview() {
    ConversionList(
        conversion = Conversion(
            amount = 1,
            base = Currency.AUD,
            date = "2021-05-12",
            rates = mapOf(Currency.CNY to 5.02)
        )
    )
}
