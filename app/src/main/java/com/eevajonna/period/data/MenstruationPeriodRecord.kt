package com.eevajonna.period.data

import androidx.health.connect.client.records.MenstruationPeriodRecord
import com.eevajonna.period.ui.utils.TimeUtils
import java.time.LocalDate

fun MenstruationPeriodRecord.startDateToLocalDate(): LocalDate = TimeUtils.instantToLocalDate(this.startTime)

fun MenstruationPeriodRecord.endDateToLocalDate(): LocalDate = TimeUtils.instantToLocalDate(this.endTime)

fun MenstruationPeriodRecord.isCurrent(): Boolean = this.startDateToLocalDate().isEqual(this.endDateToLocalDate())
