package br.com.sailboat.todozy.domain.helper

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

fun roundValue(value: Double, decimals: Int): Double {
    var newValue = BigDecimal(value.toString())
    newValue = newValue.setScale(decimals, BigDecimal.ROUND_HALF_UP)

    return newValue.toDouble()
}

fun formatValue(value: Double, decimals: Int): String {
    if (value % 1 == 0.0) {
        val newValue = roundValue(value, decimals).toLong()
        return newValue.toString()
    } else {
        return roundValue(value, decimals).toString()
    }
}

fun formatCurrency(value: Double): String {
    val newValue = BigDecimal(value.toString())

    val format = NumberFormat.getCurrencyInstance()
    format.roundingMode = RoundingMode.HALF_UP

    return format.format(newValue)
}