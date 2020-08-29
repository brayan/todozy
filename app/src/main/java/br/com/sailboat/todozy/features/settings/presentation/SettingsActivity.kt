package br.com.sailboat.todozy.features.settings.presentation

import android.content.Intent
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.core.presentation.base.BaseActivity
import br.com.sailboat.todozy.core.presentation.helper.RequestCode

fun Fragment.startSettingsActivity() {
    val intent = Intent(activity, SettingsActivity::class.java)
    startActivityForResult(intent, RequestCode.SETTINGS.ordinal)
}

class SettingsActivity : BaseActivity() {
    override fun newFragmentInstance() = SettingsFragment()
}