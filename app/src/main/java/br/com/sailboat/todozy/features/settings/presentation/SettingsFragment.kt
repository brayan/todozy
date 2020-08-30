package br.com.sailboat.todozy.features.settings.presentation

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.base.mvp.BaseMVPFragment
import br.com.sailboat.todozy.core.presentation.helper.AboutHelper
import br.com.sailboat.todozy.core.presentation.helper.RequestCode
import br.com.sailboat.todozy.features.about.presentation.startAboutActivity
import kotlinx.android.synthetic.main.frg_settings.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.inject

class SettingsFragment : BaseMVPFragment<SettingsContract.Presenter>(), SettingsContract.View {

    override val presenter: SettingsContract.Presenter by inject()
    override val layoutId = R.layout.frg_settings

    override fun initViews() {
        toolbar.setTitle(R.string.settings)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        initToneViews()
        initAbout()
        initCheckBoxVibrate()
        initToneVibrate()
    }

    override fun setCurrentToneAlarm(tone: Uri) {
        val ringtone = RingtoneManager.getRingtone(activity, tone)
        tvSettingsCurrentTone.text = ringtone.getTitle(activity)
    }

    override fun setCurrentToneAlarmNone() {
        tvSettingsCurrentTone.setText(R.string.none)
    }

    override fun showAlarmChooser(alarmSound: Uri?) {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.notification_tone))
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, alarmSound)

        this@SettingsFragment.startActivityForResult(intent, RequestCode.REQUEST_SETTING_TONE.ordinal)
    }

    override fun setVibrateAlarm(shouldVibrate: Boolean) {
        cbSettingsVibrate.isChecked = shouldVibrate
    }

    private fun initAbout() {
        tvSettingsAbout.setOnClickListener {
            activity?.run { startAboutActivity(AboutHelper(this).getInfo()) }
        }
    }

    private fun initToneVibrate() {
        flSettingsVibrate.setOnClickListener { cbSettingsVibrate.performClick() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.REQUEST_SETTING_TONE.ordinal -> {
                    val uri = data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                    uri?.run { presenter.onSelectAlarmTone(uri) }
                }
            }
        }
    }

    private fun initCheckBoxVibrate() = activity?.run {
        cbSettingsVibrate.setOnCheckedChangeListener { _, isChecked ->
            presenter.onClickVibrateAlarm(isChecked)
        }
    }

    private fun initToneViews() = activity?.run {
        llSettingsTone.setOnClickListener { presenter.onClickAlarmTone() }
    }

}