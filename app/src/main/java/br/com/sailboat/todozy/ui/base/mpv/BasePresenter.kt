package br.com.sailboat.todozy.ui.base.mpv

import br.com.sailboat.todozy.ui.helper.CoroutineContextProvider
import br.com.sailboat.todozy.ui.model.ViewResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

@Suppress("UNCHECKED_CAST")
abstract class BasePresenter<V : BaseMVPContract.View> : BaseMVPContract.Presenter {

    private var firstSession = true
    var contextProvider = CoroutineContextProvider()
    var view: V? = null

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

    fun launchAsync(block: suspend CoroutineScope.() -> Unit) {
        GlobalScope.launch(contextProvider.main) {
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