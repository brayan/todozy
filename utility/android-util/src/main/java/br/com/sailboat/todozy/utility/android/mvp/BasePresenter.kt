package br.com.sailboat.todozy.utility.android.mvp

import br.com.sailboat.todozy.utility.android.view.ViewResult
import br.com.sailboat.todozy.utility.kotlin.coroutines.CoroutineContextProvider
import kotlinx.coroutines.*

@Suppress("UNCHECKED_CAST")
abstract class BasePresenter<V : BaseMVPContract.View> : BaseMVPContract.Presenter {

    private var firstSession = true

    var contextProvider = CoroutineContextProvider()
    var view: V? = null

    val scope = CoroutineScope(Job() + contextProvider.main)

    override fun attachView(view: BaseMVPContract.View) {
        this.view = view as? V
    }

    override fun start() {
        if (firstSession) {
            firstSession = false
            onStart()
        } else {
            onRestart()
        }
        postStart()
    }

    override fun destroy() {
        this.view = null
        scope.cancel()
    }

    fun launchMain(block: suspend CoroutineScope.() -> Unit) {
        scope.launch(contextProvider.main) {
            supervisorScope { block() }
        }
    }

    override open fun onResult(result: ViewResult) {
        onStart()
    }


    open fun onStart() {}
    open fun onRestart() {}
    open fun postStart() {}

}