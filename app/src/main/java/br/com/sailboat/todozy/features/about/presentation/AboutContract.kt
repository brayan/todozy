package br.com.sailboat.todozy.features.about.presentation

import br.com.sailboat.todozy.core.presentation.base.mvp.BaseMVPContract
import br.com.sailboat.todozy.core.presentation.model.ItemView

interface AboutContract {

    interface View : BaseMVPContract.View {
        fun updateList()
        fun extractAboutInfo(): List<ItemView>
    }

    interface Presenter : BaseMVPContract.Presenter {
        fun getAbout(): List<ItemView>
    }

}