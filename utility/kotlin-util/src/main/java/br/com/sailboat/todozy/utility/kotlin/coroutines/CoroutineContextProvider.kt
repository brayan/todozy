package br.com.sailboat.todozy.utility.kotlin.coroutines

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class CoroutineContextProvider {
    open val main: CoroutineContext by lazy { Dispatchers.Main }
    open val default: CoroutineContext by lazy { Dispatchers.Default }
    open val io: CoroutineContext by lazy { Dispatchers.IO }
}
