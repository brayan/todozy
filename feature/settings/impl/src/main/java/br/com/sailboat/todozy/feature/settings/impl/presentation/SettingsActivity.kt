package br.com.sailboat.todozy.feature.settings.impl.presentation

import android.content.Intent
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.uicomponent.model.RequestCode
import br.com.sailboat.todozy.utility.android.activity.BaseActivity

fun Fragment.startSettingsActivity() {
    val intent = Intent(activity, SettingsActivity::class.java)
    startActivityForResult(intent, RequestCode.SETTINGS.ordinal)
}

class SettingsActivity : BaseActivity() {
    override fun newFragmentInstance() = SettingsFragment()
}