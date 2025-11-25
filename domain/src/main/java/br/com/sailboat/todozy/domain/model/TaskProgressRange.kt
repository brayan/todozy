package br.com.sailboat.todozy.domain.model

import java.time.LocalDate

enum class TaskProgressRange {
    LAST_YEAR,
    LAST_30_DAYS,
    LAST_7_DAYS,
    ;

    fun startDate(today: LocalDate): LocalDate =
        when (this) {
            LAST_YEAR -> today.minusMonths(12).plusDays(1)
            LAST_30_DAYS -> today.minusDays(29)
            LAST_7_DAYS -> today.minusDays(6)
        }
}
