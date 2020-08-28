package br.com.sailboat.todozy.features.settings.presentation

import br.com.sailboat.todozy.core.presentation.base.mvp.BasePresenter

class SettingsPresenter : BasePresenter<SettingsContract.View>(), SettingsContract.Presenter {


    override fun postStart() {
        updateToneText()
    }

//    fun onResultOkRingtonePicker(data: Intent) {
//        val uri = data.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
//        PreferencesHelper.setCurrentNotificationSound(uri, getContext())
//        updateToneText()
//    }

    private fun updateToneText() {
//        try {
//            val uri = PreferencesHelper.getCurrentNotificationSound(getContext())
//            if (uri == null) {
//                view?.setCurrentTone(getString(R.string.none))
//            } else {
//                val ringtone = RingtoneManager.getRingtone(getContext(), uri)
//                view?.setCurrentTone(ringtone.getTitle(getContext()))
//            }
//        } catch (e: Exception) {
//            view?.setCurrentTone(getString(R.string.none))
//        }

    }


}