package br.com.sailboat.todozy.utility.kotlin.extension

fun Boolean?.orFalse() = this ?: false
fun Boolean?.orTrue() = this ?: true
fun Boolean?.isTrue() = this == true
fun Boolean?.isFalse() = this == false