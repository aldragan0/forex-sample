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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.forexsample.api.Conversion
import com.example.forexsample.api.Currency
import com.example.forexsample.api.CurrencyInfo
import com.example.forexsample.ui.theme.ForexSampleTheme

@Composable
fun MainScreen(
    currencyViewModel: CurrencyViewModel = viewModel(),
    showDetails: (Currency) -> Unit
) {
    val items: List<CurrencyInfo> by currencyViewModel.items.observeAsState(listOf())
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
            MainList(items, showDetails)
        }
    }
}

@Composable
fun ConversionScreen(
    selected: Currency?,
    otherViewModel: OtherViewModel = viewModel()
) {
    val rates: List<Conversion> by otherViewModel.conversionRates.observeAsState(listOf())
    val loading: Boolean by otherViewModel.loading.observeAsState(true)

    //TODO: remove these log lines
    Log.i("ConversionScreen", "rates=${rates.size}")
    Log.i("ConversionScreen", "loading=${loading}")

    rates.forEach {
        Log.i("ConversionScreen", "$it")
    }

    when {
        selected == null -> {
            Toast.makeText(LocalContext.current, "Invalid selected currency", Toast.LENGTH_SHORT)
                .show()
        }
        loading -> {
            otherViewModel.loadConversionRates(selected)
            CircularProgressIndicator()
        }
        !loading -> {
            val item = rates.firstOrNull { it.base == selected }
            //TODO: remove these log lines
            Log.i("ConversionScreen", "$item")
            if (item != null) {
                ConversionList(item)
            }
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

@Composable
fun ConversionList(conversion: Conversion) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .height(32.dp)
            .fillMaxWidth()
    ) {
        Row {
            Text(text = "Currency: ${conversion.base}, amount: ${conversion.amount}")
        }
    }
}

@Composable
fun MainList(
    items: List<CurrencyInfo>,
    showDetails: (Currency) -> Unit
) {
    LazyColumn {
        items(items, { it.currency }) {
            ListItem(it.currency, it.description, showDetails)
        }
    }
}

@Composable
fun ListItem(
    currency: Currency,
    name: String,
    showDetails: (Currency) -> Unit
) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable(onClick = { showDetails(currency) })
    ) {
        Row {
            Text(
                text = currency.toString(),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(),
            )
            Text(
                text = name,
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
        ListItem(currency = Currency.AUD, name = "Description here") {}
    }
}
