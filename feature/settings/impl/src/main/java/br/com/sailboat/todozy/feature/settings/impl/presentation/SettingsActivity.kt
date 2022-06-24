package br.com.sailboat.todozy.feature.settings.impl.presentation

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import br.com.sailboat.todozy.utility.android.activity.BaseActivity

fun Context.startSettingsActivity(launcher: ActivityResultLauncher<Intent>) {
    val intent = Intent(this, SettingsActivity::class.java)
    launcher.launch(intent)
}

internal class SettingsActivity : BaseActivity() {
    override fun newFragmentInstance() = SettingsFragment()
}
