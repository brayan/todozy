package br.com.sailboat.todozy.core.platform

interface LogService {
    fun error(t: Throwable)
    fun debug(text: String)
}