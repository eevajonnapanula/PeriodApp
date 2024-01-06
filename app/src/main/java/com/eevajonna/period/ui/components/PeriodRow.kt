package com.eevajonna.period.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eevajonna.period.R
import com.eevajonna.period.ui.screens.Period

@Composable
fun PeriodRow(period: Period, onEditIconClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PeriodRow.verticalPadding)
            .clip(PeriodRow.shape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = PeriodRow.verticalPadding, horizontal = PeriodRow.horizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(PeriodRow.horizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PeriodCanvas(startDate = period.startDate, endDate = period.endDate, modifier = Modifier.weight(6f))
        IconButton(onClick = { onEditIconClick() }, modifier = Modifier.weight(1f)) {
            Icon(Icons.Default.Edit, stringResource(R.string.button_edit))
        }
    }
}

object PeriodRow {
    val verticalPadding = 12.dp
    val horizontalPadding = 8.dp
    val shape = RoundedCornerShape(8.dp)
}
