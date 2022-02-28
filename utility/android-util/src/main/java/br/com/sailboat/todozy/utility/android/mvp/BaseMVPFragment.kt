package br.com.sailboat.todozy.utility.android.mvp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.sailboat.todozy.utility.android.activity.hideKeyboard
import br.com.sailboat.todozy.utility.android.dialog.ProgressDialog
import br.com.sailboat.todozy.utility.android.fragment.BaseFragment
import br.com.sailboat.todozy.utility.android.log.log
import br.com.sailboat.todozy.utility.android.view.ViewResult

abstract class BaseMVPFragment<P : BaseMVPContract.Presenter> : BaseFragment(),
    BaseMVPContract.View {

    abstract val presenter: P
    private var progress = ProgressDialog()

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

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            presenter.onResult(ViewResult.SUCCESS)
        } else {
            presenter.onResult(ViewResult.FAILURE)
        }
    }

    override fun log(exception: Exception) {
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

    override fun showProgress() {
        if (progress.isAdded.not()) {
            progress.show(childFragmentManager, "PROGRESS")
        }
    }

    override fun hideProgress() {
        progress.dismissAllowingStateLoss()
    }

}