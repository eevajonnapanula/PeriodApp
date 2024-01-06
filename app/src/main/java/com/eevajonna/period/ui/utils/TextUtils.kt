package com.eevajonna.period.ui.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object TextUtils {
    fun formatDate(localDate: LocalDate, pattern: String = "d. MMM yyyy"): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return localDate.format(formatter)
    }
}
