package com.eevajonna.period

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.eevajonna.period.ui.screens.MainScreen
import com.eevajonna.period.ui.screens.Period
import com.eevajonna.period.ui.theme.PeriodTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PeriodTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainScreen(
                        listOf(
                            Period(
                                LocalDate.now().minusDays(1),
                                null,
                            ),
                            Period(
                                LocalDate.now().minusMonths(1).minusDays(2),
                                LocalDate.now().minusMonths(1).plusDays(2),
                            ),
                            Period(
                                LocalDate.now().minusMonths(2).minusDays(2),
                                LocalDate.now().minusMonths(2).plusDays(2),
                            ),
                            Period(
                                LocalDate.now().minusMonths(3).minusDays(2),
                                LocalDate.now().minusMonths(3).plusDays(2),
                            ),
                            Period(
                                LocalDate.now().minusMonths(4).minusDays(2),
                                LocalDate.now().minusMonths(4).plusDays(2),
                            ),
                        ),
                    )
                }
            }
        }
    }
}
