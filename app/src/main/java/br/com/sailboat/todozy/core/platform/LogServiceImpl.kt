package br.com.sailboat.todozy.core.platform

import timber.log.Timber

class LogServiceImpl: LogService {

    override fun error(t: Throwable) {
        Timber.e(t)
    }

    override fun debug(text: String) {
        Timber.d(text)
    }

}