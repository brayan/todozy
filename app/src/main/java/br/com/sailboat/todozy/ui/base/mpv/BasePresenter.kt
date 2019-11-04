package br.com.sailboat.todozy.ui.base.mpv

import br.com.sailboat.todozy.ui.helper.CoroutineContextProvider
import br.com.sailboat.todozy.ui.model.ViewResult
import kotlinx.coroutines.*

@Suppress("UNCHECKED_CAST")
abstract class BasePresenter<V : BaseMVPContract.View> : BaseMVPContract.Presenter {

    private var firstSession = true
    private val asyncJobs: MutableList<Job> = mutableListOf()

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

    override fun destroy() {
        this.view = null
        cancelAllAsync()
    }

    fun launchAsync(block: suspend CoroutineScope.() -> Unit) {
        val job: Job = GlobalScope.launch(contextProvider.main) {
            supervisorScope { block() }
        }
        asyncJobs.add(job)
        job.invokeOnCompletion { asyncJobs.remove(job) }
    }

    override open fun onResult(result: ViewResult) {
        onStart()
    }

    private fun cancelAllAsync() {
        val asyncJobsSize = asyncJobs.size

        if (asyncJobsSize > 0) {
            for (i in asyncJobsSize - 1 downTo 0) {
                asyncJobs[i].cancel()
            }
        }
    }


    open fun onStart() {}
    open fun onRestart() {}
    open fun postStart() {}

}