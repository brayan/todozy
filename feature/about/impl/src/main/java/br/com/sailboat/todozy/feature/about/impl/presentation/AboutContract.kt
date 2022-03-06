package br.com.sailboat.todozy.feature.about.impl.presentation

import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.utility.android.mvp.BaseMVPContract

interface AboutContract {

    interface View : BaseMVPContract.View {
        fun updateList()
        fun extractAboutInfo(): List<UiModel>
    }

    interface Presenter : BaseMVPContract.Presenter {
        fun getAbout(): List<UiModel>
    }

}