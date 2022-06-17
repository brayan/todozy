package br.com.sailboat.todozy.domain.service

interface LogService {
    fun error(t: Throwable)
    fun debug(text: String)
}
