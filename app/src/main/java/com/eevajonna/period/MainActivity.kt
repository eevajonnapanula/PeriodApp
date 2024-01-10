package com.eevajonna.period

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.eevajonna.period.data.HealthConnectManager
import com.eevajonna.period.ui.screens.MainScreen
import com.eevajonna.period.ui.theme.PeriodTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val healthConnectManager = HealthConnectManager(application)

        setContent {
            PeriodTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainScreen(
                        healthConnectManager,
                    )
                }
            }
        }
    }
}
