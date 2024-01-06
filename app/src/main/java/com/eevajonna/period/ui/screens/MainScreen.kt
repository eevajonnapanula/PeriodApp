package com.eevajonna.period.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.eevajonna.period.R
import com.eevajonna.period.ui.components.DateRangePickerDialog
import com.eevajonna.period.ui.components.PeriodRow
import com.eevajonna.period.ui.theme.PeriodTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(periods: List<Period>) {
    var showDatePickerDialog by remember {
        mutableStateOf(false)
    }
    var selectedPeriod by remember {
        mutableStateOf(Period.EMPTY)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedPeriod = Period.EMPTY
                showDatePickerDialog = true
            }) {
                Icon(Icons.Default.Add, stringResource(R.string.button_add_new_period))
            }
        },
    ) { paddingVals ->
        val periodsPerYear = periods.groupBy { period -> period.startDate.year }
        LazyColumn(
            modifier = Modifier
                .padding(paddingVals)
                .padding(MainScreen.padding),
        ) {
            periodsPerYear.entries
                .sortedByDescending { it.key }
                .map { (year, periodsForYear) ->
                    item {
                        Text(
                            year.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.semantics { heading() },
                        )
                    }
                    items(periodsForYear) {
                        PeriodRow(period = it) {
                            selectedPeriod = it
                            showDatePickerDialog = true
                        }
                    }
                }
        }
        if (showDatePickerDialog) {
            DateRangePickerDialog(
                selectedPeriod = selectedPeriod,
                onDismiss = { showDatePickerDialog = false },
            ) { updatedSelectedPeriod ->
                showDatePickerDialog = false
                selectedPeriod = updatedSelectedPeriod
            }
        }
    }
}

@Preview(name = "Light Mode")
@Composable
fun MainScreenPreview() {
    val periods = listOf(
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
    )
    PeriodTheme {
        MainScreen(periods)
    }
}

data class Period(
    val startDate: LocalDate,
    val endDate: LocalDate?,
) {
    val isCurrent = endDate == null
    companion object {
        val EMPTY = Period(LocalDate.now(), null)
    }
}

object MainScreen {
    val padding = 16.dp
}
