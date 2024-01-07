package com.eevajonna.period.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.MenstruationPeriodRecord
import com.eevajonna.period.R
import com.eevajonna.period.data.endDateToLocalDate
import com.eevajonna.period.data.startDateToLocalDate

@Composable
fun PeriodRow(
    period: MenstruationPeriodRecord,
    onDeleteIconClick: () -> Unit,
    onEditIconClick: () -> Unit,
) {
    var menuOpen by remember { mutableStateOf(false) }
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
        PeriodCanvas(startDate = period.startDateToLocalDate(), endDate = period.endDateToLocalDate(), modifier = Modifier.weight(6f))
        Column(modifier = Modifier.weight(1f)) {
            IconButton(onClick = { menuOpen = true }) {
                Icon(Icons.Default.MoreVert, stringResource(R.string.button_edit))
            }
            DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                DropdownMenuItem(
                    text = { Text("Edit") },
                    onClick = {
                        menuOpen = false
                        onEditIconClick()
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = null,
                        )
                    },
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    onClick = {
                        menuOpen = false
                        onDeleteIconClick()
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = null,
                        )
                    },
                )
            }
        }
    }
}

object PeriodRow {
    val verticalPadding = 12.dp
    val horizontalPadding = 8.dp
    val shape = RoundedCornerShape(8.dp)
}
