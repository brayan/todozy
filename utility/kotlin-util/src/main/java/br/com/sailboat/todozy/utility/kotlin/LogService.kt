package br.com.sailboat.todozy.utility.kotlin

interface LogService {
    fun error(t: Throwable)
    fun debug(text: String)
}
