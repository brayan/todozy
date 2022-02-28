package br.com.sailboat.todozy.features.about.presentation

import br.com.sailboat.todozy.utility.android.mvp.BasePresenter

class AboutPresenter : BasePresenter<AboutContract.View>(), AboutContract.Presenter {


    private val viewModel = AboutViewModel()


    override fun onStart() {
        val itemViews = view?.extractAboutInfo()

        viewModel.itemViews.clear()
        viewModel.itemViews.addAll(itemViews.orEmpty())
    }

    override fun postStart() {
        view?.updateList()
    }

    override fun getAbout() = viewModel.itemViews

}