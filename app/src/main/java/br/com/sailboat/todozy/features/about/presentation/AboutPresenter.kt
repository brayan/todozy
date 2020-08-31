package br.com.sailboat.todozy.features.about.presentation

import br.com.sailboat.todozy.core.extensions.safe
import br.com.sailboat.todozy.core.presentation.base.mvp.BasePresenter

class AboutPresenter : BasePresenter<AboutContract.View>(), AboutContract.Presenter {


    private val viewModel = AboutViewModel()


    override fun onStart() {
        val itemViews = view?.extractAboutInfo()

        viewModel.itemViews.clear()
        viewModel.itemViews.addAll(itemViews.safe())
    }

    override fun postStart() {
        view?.updateList()
    }

    override fun getAbout() = viewModel.itemViews

}