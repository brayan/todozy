package br.com.sailboat.todozy.feature.settings.impl.presentation

import android.app.Activity
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.feature.navigation.android.AboutNavigator
import br.com.sailboat.todozy.feature.settings.impl.databinding.FrgSettingsBinding
import br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel.SettingsViewAction
import br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel.SettingsViewIntent
import br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel.SettingsViewModel
import br.com.sailboat.todozy.utility.android.view.setSafeClickListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import br.com.sailboat.uicomponent.impl.R as UiR

internal class SettingsFragment : Fragment() {
    private val viewModel: SettingsViewModel by viewModel()
    private val aboutNavigator: AboutNavigator by inject()

    private lateinit var binding: FrgSettingsBinding

    private val exportDatabaseLauncher =
        registerForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
            uri?.run {
                viewModel.dispatchViewIntent(SettingsViewIntent.OnSelectExportDatabaseUri(uri))
            }
        }

    private val importDatabaseLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.run { showConfirmImportDatabaseDialog(uri) }
        }

    private val pickRingtoneLauncher =
        registerForActivityResult(PickRingtoneContract()) { uri ->
            uri?.run {
                viewModel.dispatchViewIntent(SettingsViewIntent.OnSelectAlarmTone(uri))
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FrgSettingsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
        viewModel.dispatchViewIntent(SettingsViewIntent.OnStart)
    }

    private fun initViews() {
        initToolbar()
        initToneViews()
        initAbout()
        initCheckBoxVibrate()
        initToneVibrate()
        initBackupViews()
    }

    private fun observeViewModel() {
        observeActions()
        viewModel.viewState.alarmSound.observe(viewLifecycleOwner) { alarmSound ->
            if (alarmSound == null) {
                binding.tvSettingsCurrentTone.setText(UiR.string.none)
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
                is SettingsViewAction.CreateDatabaseBackupDocument -> exportDatabaseLauncher.launch(action.fileName)
                is SettingsViewAction.OpenDatabaseBackupDocument -> importDatabaseLauncher.launch(
                    arrayOf(
                        "application/json",
                        "text/*",
                    ),
                )
                is SettingsViewAction.ShowDatabaseExportSuccess -> showDatabaseExportSuccess()
                is SettingsViewAction.ShowDatabaseImportSuccess -> showDatabaseImportSuccess()
                is SettingsViewAction.ShowDatabaseBackupError -> showDatabaseBackupError()
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
        appbar.toolbar.setTitle(UiR.string.settings)
    }

    private fun initToneViews() = activity?.run {
        binding.llSettingsTone.setSafeClickListener {
            viewModel.dispatchViewIntent(SettingsViewIntent.OnClickMenuAlarmTone)
        }
    }

    private fun initAbout() {
        binding.tvSettingsAbout.setSafeClickListener {
            viewModel.dispatchViewIntent(SettingsViewIntent.OnClickAbout)
        }
    }

    private fun initCheckBoxVibrate() = activity?.run {
        binding.cbSettingsVibrate.setOnCheckedChangeListener { _, isChecked ->
            viewModel.dispatchViewIntent(SettingsViewIntent.OnClickVibrateAlarm(isChecked))
        }
    }

    private fun initToneVibrate() = with(binding) {
        flSettingsVibrate.setSafeClickListener { cbSettingsVibrate.performClick() }
    }

    private fun initBackupViews() = with(binding) {
        tvSettingsExportDatabase.setSafeClickListener {
            viewModel.dispatchViewIntent(SettingsViewIntent.OnClickExportDatabase)
        }
        tvSettingsImportDatabase.setSafeClickListener {
            viewModel.dispatchViewIntent(SettingsViewIntent.OnClickImportDatabase)
        }
    }

    private fun showConfirmImportDatabaseDialog(uri: Uri) {
        AlertDialog.Builder(requireContext())
            .setTitle(UiR.string.are_you_sure)
            .setMessage(getString(UiR.string.msg_confirm_database_import))
            .setPositiveButton(UiR.string.import_action) { _, _ ->
                viewModel.dispatchViewIntent(SettingsViewIntent.OnConfirmImportDatabase(uri))
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun showDatabaseExportSuccess() {
        Toast.makeText(activity, UiR.string.msg_database_exported, Toast.LENGTH_SHORT).show()
    }

    private fun showDatabaseImportSuccess() {
        activity?.setResult(Activity.RESULT_OK)
        Toast.makeText(activity, UiR.string.msg_database_imported, Toast.LENGTH_SHORT).show()
    }

    private fun showDatabaseBackupError() {
        Toast.makeText(activity, UiR.string.msg_error, Toast.LENGTH_SHORT).show()
    }
}
