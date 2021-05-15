package com.example.forexsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.forexsample.api.Currency
import com.example.forexsample.api.asCurrency
import com.example.forexsample.screen.ConversionScreen
import com.example.forexsample.screen.MainScreen
import com.example.forexsample.ui.theme.ForexSampleTheme
import com.example.forexsample.viewmodel.ConversionRatesViewModel
import com.example.forexsample.viewmodel.CurrencyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currencyViewModel = CurrencyViewModel()
        val conversionRatesViewModel = ConversionRatesViewModel()

        setContent {
            val navController = rememberNavController()
            ForexSampleTheme {
                NavHost(navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(
                            currencyViewModel,
                            showDetails = { currency ->
                                navController.navigate("conversionRates/$currency")
                            }
                        )
                    }
                    composable(
                        "conversionRates/{currency}",
                        arguments = listOf(navArgument("currency") {
                            type = NavType.EnumType(Currency::class.java)
                        })
                    ) {
                        ConversionScreen(
                            conversionRatesViewModel,
                            it.arguments?.getSerializable("currency").asCurrency()
                        )
                    }
                }
            }
        }
    }
}
