package br.com.sailboat.todozy.features.about.presentation

import br.com.sailboat.todozy.core.presentation.base.mvp.BasePresenter

class AboutPresenter : BasePresenter<AboutContract.View>(), AboutContract.Presenter {


    private val viewModel = AboutViewModel()


    override fun onStart() {
//        val itemViews = arguments.getSerializable("RECYCLER_ITEMS") as List<ItemView>?
//        viewModel.itemViews.clear()
//        viewModel.itemViews.addAll(itemViews!!)
    }

    override fun postStart() {
        view?.updateList()
    }

    override fun getAbout() = viewModel.itemViews

}