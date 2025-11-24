package br.com.sailboat.todozy.platform.impl

import br.com.sailboat.todozy.utility.kotlin.LogService
import timber.log.Timber

internal class LogServiceImpl : LogService {
    override fun error(t: Throwable) {
        Timber.e(t)
    }

    override fun debug(text: String) {
        Timber.d(text)
    }
}
