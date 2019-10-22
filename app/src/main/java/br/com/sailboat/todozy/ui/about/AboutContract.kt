package br.com.sailboat.todozy.ui.about

import br.com.sailboat.todozy.ui.base.mpv.BaseMVPContract
import br.com.sailboat.todozy.ui.model.ItemView

interface AboutContract {

    interface View: BaseMVPContract.View {
        fun updateList()
    }

    interface Presenter: BaseMVPContract.Presenter {
        fun getAbout(): List<ItemView>
    }

}