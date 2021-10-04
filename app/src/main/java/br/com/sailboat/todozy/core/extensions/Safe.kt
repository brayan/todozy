package br.com.sailboat.todozy.core.extensions

import java.util.*

fun Int?.orZero() = this ?: 0
fun Long?.orZero() = this ?: 0
fun Double?.orZero() = this ?: 0.0
fun Boolean?.orFalse() = this ?: false
fun Boolean?.isFalse() = this == false
fun Boolean?.isTrue() = this == true
fun Calendar?.orNewCalendar() = this ?: Calendar.getInstance()