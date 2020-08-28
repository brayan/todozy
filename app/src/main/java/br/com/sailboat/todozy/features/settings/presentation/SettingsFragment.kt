package br.com.sailboat.todozy.features.settings.presentation

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.platform.PreferencesHelper
import br.com.sailboat.todozy.core.presentation.base.mvp.BaseMVPFragment
import br.com.sailboat.todozy.core.presentation.helper.AboutHelper
import br.com.sailboat.todozy.core.presentation.helper.RequestCode
import br.com.sailboat.todozy.features.about.presentation.AboutActivity
import kotlinx.android.synthetic.main.frg_settings.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.inject

class SettingsFragment : BaseMVPFragment<SettingsContract.Presenter>(), SettingsContract.View {

    override val presenter: SettingsContract.Presenter by inject()
    override val layoutId = R.layout.frg_settings

    override fun initViews() {
        toolbar.setTitle(R.string.settings)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { activity!!.onBackPressed() }

        initToneViews()
        initAbout()
        initCheckBoxVibrate()
        initToneVibrate()
    }

    override fun setCurrentTone(tone: String) {
        frg_settings__tv__current_tone.text = tone
    }

    private fun initAbout() {
        frg_settings__tv__about.setOnClickListener { AboutActivity.start(activity!!, AboutHelper(activity!!).getInfo()) }
    }

    private fun initToneVibrate() {
        frg_settings__frame__vibrate.setOnClickListener { frg_settings__cb__vibrate!!.performClick() }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.REQUEST_SETTING_TONE.ordinal -> {
                    //presenter.onResultOkRingtonePicker(data)
                }
            }
        }


    }

    private fun initCheckBoxVibrate() {
        //frg_settings__cb__vibrate.isChecked = PreferencesHelper.isVibrationSettingAllowed(getActivity())
        //frg_settings__cb__vibrate.setOnCheckedChangeListener { buttonView, isChecked -> PreferencesHelper.setCurrentVibrateSetting(isChecked, getActivity()) }
    }

    private fun initToneViews() {
        frg_settings__ll__tone.setOnClickListener {
            val uri = PreferencesHelper(activity!!).getCurrentNotificationSound()

            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.notification_tone))
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, uri)

            this@SettingsFragment.startActivityForResult(intent, RequestCode.REQUEST_SETTING_TONE.ordinal)
        }
    }

}