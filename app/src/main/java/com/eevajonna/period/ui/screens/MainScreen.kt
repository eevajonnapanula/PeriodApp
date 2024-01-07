package com.eevajonna.period.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.eevajonna.period.R
import com.eevajonna.period.data.HealthConnectManager
import com.eevajonna.period.data.PERMISSIONS
import com.eevajonna.period.ui.PeriodViewModel
import com.eevajonna.period.ui.PeriodViewModelFactory
import com.eevajonna.period.ui.components.DateRangePickerDialog
import com.eevajonna.period.ui.components.PeriodRow
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(healthConnectManager: HealthConnectManager) {
    val periods = emptyList<Period>()

    val viewModel: PeriodViewModel = viewModel(
        factory = PeriodViewModelFactory(
            healthConnectManager = healthConnectManager,
        ),
    )

    val permissionsLauncher =
        rememberLauncherForActivityResult(viewModel.permissionsLauncher) {
            // TODO
        }

    var showDatePickerDialog by remember {
        mutableStateOf(false)
    }
    var selectedPeriod by remember {
        mutableStateOf(Period.EMPTY)
    }


    LaunchedEffect(Unit) {
        if (viewModel.permissionsGranted) {
            // TODO
        } else {
            permissionsLauncher.launch(PERMISSIONS)
        }
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
