package com.eevajonna.period.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerDefaults
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.MenstruationPeriodRecord
import com.eevajonna.period.R
import com.eevajonna.period.data.endDateToLocalDate
import com.eevajonna.period.data.isCurrent
import com.eevajonna.period.data.startDateToLocalDate
import com.eevajonna.period.ui.utils.TextUtils
import com.eevajonna.period.ui.utils.TimeUtils
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    selectedPeriod: MenstruationPeriodRecord,
    onDismiss: () -> Unit,
    onConfirm: (MenstruationPeriodRecord) -> Unit,
) {
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = TimeUtils.localDateToMilliseconds(selectedPeriod.startDateToLocalDate()),
        initialSelectedEndDateMillis = if (selectedPeriod.isCurrent()) null else TimeUtils.localDateToMilliseconds(selectedPeriod.endDateToLocalDate()),
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val startTime =
                    if (dateRangePickerState.selectedStartDateMillis != null) {
                        Instant.ofEpochMilli(dateRangePickerState.selectedStartDateMillis!!)
                    } else
                        Instant.now()

                val endTime =
                    if (dateRangePickerState.selectedEndDateMillis != null) {
                        Instant.ofEpochMilli(dateRangePickerState.selectedEndDateMillis!!)
                    } else
                        startTime.plusSeconds(60) // This needs to be different from start time, but within the same day to work as intended on my code.

                val updated = MenstruationPeriodRecord(
                    startTime = startTime,
                    endTime = endTime,
                    startZoneOffset = selectedPeriod.startZoneOffset,
                    endZoneOffset = selectedPeriod.endZoneOffset,
                    metadata = selectedPeriod.metadata,
                )
                onConfirm(updated)
            }) {
                Text(stringResource(R.string.button_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.button_cancel))
            }
        },
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                DateRangePickerDefaults.DateRangePickerTitle(
                    dateRangePickerState,
                    modifier = Modifier.padding(DateRangePickerDialog.titlePadding),
                )
            },
            headline = {
                DateRangePickerHeadline(
                    state = dateRangePickerState,
                    modifier = Modifier.padding(DateRangePickerDialog.titlePadding),
                )
            },
            modifier = Modifier
                .weight(1f)
                .padding(DateRangePickerDialog.datePickerPadding),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DateRangePickerHeadline(
    state: DateRangePickerState,
    modifier: Modifier,
) {
    val startDateLocalDate = TimeUtils.milliSecondsToLocalDate(state.selectedStartDateMillis)
    val startDate = startDateLocalDate?.let { TextUtils.formatDate(startDateLocalDate) } ?: ""

    val endDateLocalDate = TimeUtils.milliSecondsToLocalDate(state.selectedEndDateMillis)
    val endDate = endDateLocalDate?.let { TextUtils.formatDate(endDateLocalDate) } ?: ""

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(DateRangePickerDialog.titlePadding),
    ) {
        Text(text = startDate)
        Text(text = "-")
        Text(text = endDate)
    }
}

object DateRangePickerDialog {
    val titlePadding = 4.dp
    val datePickerPadding = 16.dp
}
