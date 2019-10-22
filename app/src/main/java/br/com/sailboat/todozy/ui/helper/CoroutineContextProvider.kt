package br.com.sailboat.todozy.ui.helper

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class CoroutineContextProvider {
    open val main: CoroutineContext by lazy { Dispatchers.Main }
    open val default: CoroutineContext by lazy { Dispatchers.Default }
}