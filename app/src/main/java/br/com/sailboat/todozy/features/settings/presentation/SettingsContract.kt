package br.com.sailboat.todozy.features.settings.presentation

import br.com.sailboat.todozy.core.presentation.base.mvp.BaseMVPContract

interface SettingsContract {

    interface View : BaseMVPContract.View {
        fun setCurrentTone(tone: String)
    }

    interface Presenter : BaseMVPContract.Presenter {

    }

}