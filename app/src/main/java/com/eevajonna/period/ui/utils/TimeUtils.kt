package com.eevajonna.period.ui.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

object TimeUtils {
    fun localDateToMilliseconds(date: LocalDate): Long {
        return ZonedDateTime.of(date.atStartOfDay(), ZoneId.of("UTC")).toInstant().toEpochMilli()
    }

    fun milliSecondsToLocalDate(millis: Long?): LocalDate? {
        if (millis == null) return null
        val instant = Instant.ofEpochMilli(millis)
        return instant.atZone(ZoneId.of("UTC")).toLocalDateTime().toLocalDate()
    }

    fun instantToLocalDate(instant: Instant): LocalDate {
        return instant.atZone(ZoneId.of("UTC")).toLocalDateTime().toLocalDate()
    }
}
