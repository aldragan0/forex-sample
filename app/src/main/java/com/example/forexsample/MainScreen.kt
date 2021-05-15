package com.example.forexsample

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.forexsample.ui.theme.ForexSampleTheme

@Composable
fun MainScreen(
    currencyViewModel: CurrencyViewModel,
    showDetails: (Currency) -> Unit
) {
    val items: List<CurrencyInfo<String>> by currencyViewModel.items.observeAsState(listOf())
    val loading: Boolean by currencyViewModel.loading.observeAsState(true)
    val error: Boolean by currencyViewModel.error.observeAsState(false)

    when {
        loading || items.isEmpty() -> {
            currencyViewModel.loadCurrencies()
            CircularProgressIndicator()
        }
        error -> {
            Toast.makeText(LocalContext.current, "Failed to fetch data", Toast.LENGTH_SHORT)
                .show()
        }
        else -> {
            ItemList(items, showDetails)
        }
    }
}

@Composable
fun ConversionScreen(
    conversionRatesViewModel: ConversionRatesViewModel,
    selected: Currency?,
) {
    val rates: List<Conversion> by conversionRatesViewModel.conversionRates.observeAsState(listOf())
    val loading: Boolean by conversionRatesViewModel.loading.observeAsState(false)

    Log.i("conversionScreen", "rates=$rates, loading=$loading")

    if (selected == null) {
        Toast.makeText(LocalContext.current, "Invalid selected currency", Toast.LENGTH_SHORT)
            .show()
        return
    }
    conversionRatesViewModel.loadConversionRates(selected)

    if (loading) {
        CircularProgressIndicator()
    } else {
        rates.firstOrNull { it.base == selected }
            ?.also { ConversionList(it) }
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

@Composable
fun <T> ItemList(
    items: List<CurrencyInfo<T>>,
    showDetails: ((Currency) -> Unit)? = null
) {
    LazyColumn {
        items(items) {
            ListItem(it.currency, it.value, showDetails)
        }
    }
}

@Composable
fun <T> ListItem(
    currency: Currency,
    value: T,
    showDetails: ((Currency) -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable(
                enabled = showDetails != null,
                onClick = { showDetails?.invoke(currency) }
            )
    ) {
        Row {
            Text(
                text = currency.toString(),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(),
            )
            Text(
                text = value.toString(),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewListItem() {
    ForexSampleTheme {
        ListItem(currency = Currency.AUD, value = "Description here") {}
    }
}
