package br.com.sailboat.todozy.feature.settings.presentation

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.sailboat.todozy.feature.about.presentation.AboutHelper
import br.com.sailboat.todozy.feature.about.presentation.startAboutActivity
import br.com.sailboat.todozy.feature.settings.R
import br.com.sailboat.todozy.feature.settings.databinding.FrgSettingsBinding
import br.com.sailboat.todozy.uicomponent.model.RequestCode
import br.com.sailboat.todozy.utility.android.mvp.BaseMVPFragment
import org.koin.android.ext.android.inject

class SettingsFragment : BaseMVPFragment<SettingsContract.Presenter>(), SettingsContract.View {

    override val presenter: SettingsContract.Presenter by inject()

    private lateinit var binding: FrgSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FrgSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initViews() = with(binding) {
        appbar.toolbar.setTitle(R.string.settings)
        appbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        appbar.toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        initToneViews()
        initAbout()
        initCheckBoxVibrate()
        initToneVibrate()
    }

    override fun setCurrentToneAlarm(tone: Uri) {
        val ringtone = RingtoneManager.getRingtone(activity, tone)
        binding.tvSettingsCurrentTone.text = ringtone.getTitle(activity)
    }

    override fun setCurrentToneAlarmNone() {
        binding.tvSettingsCurrentTone.setText(R.string.none)
    }

    override fun showAlarmChooser(alarmSound: Uri?) {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.notification_tone))
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, alarmSound)

        this@SettingsFragment.startActivityForResult(
            intent,
            RequestCode.REQUEST_SETTING_TONE.ordinal
        )
    }

    override fun setVibrateAlarm(shouldVibrate: Boolean) {
        binding.cbSettingsVibrate.isChecked = shouldVibrate
    }

    private fun initAbout() {
        binding.tvSettingsAbout.setOnClickListener {
            activity?.run { startAboutActivity(AboutHelper(this).getInfo()) }
        }
    }

    private fun initToneVibrate() = with(binding) {
        flSettingsVibrate.setOnClickListener { cbSettingsVibrate.performClick() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.REQUEST_SETTING_TONE.ordinal -> {
                    val uri =
                        data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                    uri?.run { presenter.onSelectAlarmTone(uri) }
                }
            }
        }
    }

    private fun initCheckBoxVibrate() = activity?.run {
        binding.cbSettingsVibrate.setOnCheckedChangeListener { _, isChecked ->
            presenter.onClickVibrateAlarm(isChecked)
        }
    }

    private fun initToneViews() = activity?.run {
        binding.llSettingsTone.setOnClickListener { presenter.onClickAlarmTone() }
    }

}