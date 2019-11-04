package br.com.sailboat.todozy.ui.base.mpv

import br.com.sailboat.todozy.ui.model.ViewResult

interface BaseMVPContract {

    interface View {
        fun log(exception: Exception)
        fun hideKeyboard()
        fun showSimpleMessage(message: String)
        fun closeWithResultOk()
        fun closeWithResultNotOk()
    }

    interface Presenter {
        fun attachView(view: View)
        fun start()
        fun destroy()
        fun onResult(result: ViewResult)
    }

}