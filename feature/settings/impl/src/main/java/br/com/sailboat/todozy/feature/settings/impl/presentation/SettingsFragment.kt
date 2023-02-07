package br.com.sailboat.todozy.feature.settings.impl.presentation

import android.media.RingtoneManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.sailboat.todozy.feature.navigation.android.AboutNavigator
import br.com.sailboat.todozy.feature.settings.impl.R
import br.com.sailboat.todozy.feature.settings.impl.databinding.FrgSettingsBinding
import br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel.SettingsViewAction
import br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel.SettingsViewIntent
import br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel.SettingsViewModel
import br.com.sailboat.todozy.utility.android.fragment.BaseFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class SettingsFragment : BaseFragment() {

    private val viewModel: SettingsViewModel by viewModel()
    private val aboutNavigator: AboutNavigator by inject()

    private lateinit var binding: FrgSettingsBinding

    private val pickRingtoneLauncher = registerForActivityResult(PickRingtoneContract()) { uri ->
        uri?.run {
            viewModel.dispatchViewIntent(SettingsViewIntent.OnSelectAlarmTone(uri))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FrgSettingsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.dispatchViewIntent(SettingsViewIntent.OnStart)
    }

    override fun initViews() {
        initToolbar()
        initToneViews()
        initAbout()
        initCheckBoxVibrate()
        initToneVibrate()
    }

    private fun observeViewModel() {
        observeActions()
        viewModel.viewState.alarmSound.observe(viewLifecycleOwner) { alarmSound ->
            if (alarmSound == null) {
                binding.tvSettingsCurrentTone.setText(R.string.none)
            } else {
                val ringtone = RingtoneManager.getRingtone(activity, alarmSound)
                binding.tvSettingsCurrentTone.text = ringtone.getTitle(activity)
            }
        }
        viewModel.viewState.vibrate.observe(viewLifecycleOwner) { vibrate ->
            binding.cbSettingsVibrate.isChecked = vibrate
        }
    }

    private fun observeActions() {
        viewModel.viewState.viewAction.observe(viewLifecycleOwner) { action ->
            when (action) {
                is SettingsViewAction.NavigateToAlarmToneSelector -> navigateToAlarmToneSelector(action)
                is SettingsViewAction.NavigateToAbout -> navigateToAbout()
            }
        }
    }

    private fun navigateToAlarmToneSelector(action: SettingsViewAction.NavigateToAlarmToneSelector) {
        pickRingtoneLauncher.launch(action.uri)
    }

    private fun navigateToAbout() {
        activity?.run { aboutNavigator.navigateToAbout(this) }
    }

    private fun initToolbar() = with(binding) {
        appbar.toolbar.setTitle(R.string.settings)
        appbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        appbar.toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
    }

    private fun initToneViews() = activity?.run {
        binding.llSettingsTone.setOnClickListener {
            viewModel.dispatchViewIntent(SettingsViewIntent.OnClickMenuAlarmTone)
        }
    }

    private fun initAbout() {
        binding.tvSettingsAbout.setOnClickListener {
            viewModel.dispatchViewIntent(SettingsViewIntent.OnClickAbout)
        }
    }

    private fun initCheckBoxVibrate() = activity?.run {
        binding.cbSettingsVibrate.setOnCheckedChangeListener { _, isChecked ->
            viewModel.dispatchViewIntent(SettingsViewIntent.OnClickVibrateAlarm(isChecked))
        }
    }

    private fun initToneVibrate() = with(binding) {
        flSettingsVibrate.setOnClickListener { cbSettingsVibrate.performClick() }
    }
}
