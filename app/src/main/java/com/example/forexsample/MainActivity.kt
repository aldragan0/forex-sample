package com.example.forexsample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.forexsample.api.Currency
import com.example.forexsample.api.asCurrency
import com.example.forexsample.ui.theme.ForexSampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            ForexSampleTheme {
                NavHost(navController, startDestination = "main") {
                    composable("main") {
                        MainScreen(showDetails = { currency ->
                            Log.i("Main Activity", "navigating to $currency")
                            navController.navigate(
                                "conversionRates/$currency"
                            )
                        })
                    }
                    composable(
                        "conversionRates/{currency}",
                        arguments = listOf(navArgument("currency") {
                            type = NavType.EnumType(Currency::class.java)
                        })
                    ) {
                        ConversionScreen(it.arguments?.getSerializable("currency").asCurrency())
                    }
                }
            }
        }
    }
}
