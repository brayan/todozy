package br.com.sailboat.todozy.features.about.presentation

import br.com.sailboat.todozy.core.presentation.base.mvp.BaseMVPContract
import br.com.sailboat.todozy.uicomponent.model.UiModel

interface AboutContract {

    interface View : BaseMVPContract.View {
        fun updateList()
        fun extractAboutInfo(): List<UiModel>
    }

    interface Presenter : BaseMVPContract.Presenter {
        fun getAbout(): List<UiModel>
    }

}