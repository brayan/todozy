package br.com.sailboat.todozy.ui.base.mpv

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.sailboat.todozy.ui.base.BaseFragment
import br.com.sailboat.todozy.ui.helper.hideKeyboard
import br.com.sailboat.todozy.ui.helper.log
import br.com.sailboat.todozy.ui.model.ViewResult

abstract class BaseMVPFragment<P : BaseMVPContract.Presenter> : BaseFragment(), BaseMVPContract.View {

    abstract val presenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        retainInstance = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            presenter.onResult(ViewResult.SUCCESS)
        } else {
            presenter.onResult(ViewResult.FAILURE)
        }
    }

    override fun log(exception: Exception)  {
        exception.log()
    }

    override fun hideKeyboard() {
        activity?.hideKeyboard()
    }

    override fun showSimpleMessage(message: String) {
        activity?.run { Toast.makeText(this, message, Toast.LENGTH_LONG).show() }
    }

    override fun closeWithResultOk() {
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    override fun closeWithResultNotOk() {
        activity?.setResult(Activity.RESULT_CANCELED)
        activity?.finish()
    }

}