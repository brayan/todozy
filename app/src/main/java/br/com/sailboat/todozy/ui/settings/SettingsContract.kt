package br.com.sailboat.todozy.ui.settings

import br.com.sailboat.todozy.ui.base.mpv.BaseMVPContract

interface SettingsContract {

    interface View : BaseMVPContract.View {
        fun setCurrentTone(tone: String)
    }

    interface Presenter : BaseMVPContract.Presenter {

    }

}