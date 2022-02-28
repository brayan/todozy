package br.com.sailboat.todozy.core.presentation.base.mvp

import br.com.sailboat.todozy.utility.android.view.ViewResult

interface BaseMVPContract {

    interface View {
        fun log(exception: Exception)
        fun hideKeyboard()
        fun showSimpleMessage(message: String)
        fun closeWithResultOk()
        fun closeWithResultNotOk()
        fun showProgress()
        fun hideProgress()
    }

    interface Presenter {
        fun attachView(view: View)
        fun start()
        fun destroy()
        fun onResult(result: ViewResult)
    }

}