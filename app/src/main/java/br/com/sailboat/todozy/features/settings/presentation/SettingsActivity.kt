package br.com.sailboat.todozy.features.settings.presentation

import android.content.Intent
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.core.presentation.base.BaseActivity
import br.com.sailboat.todozy.core.presentation.helper.RequestCode

class SettingsActivity : BaseActivity() {

    companion object {
        fun start(fromFragment: Fragment) {
            val intent = Intent(fromFragment.activity, SettingsActivity::class.java)
            fromFragment.startActivityForResult(intent, RequestCode.SETTINGS.ordinal)
        }
    }

    override fun newFragmentInstance() = SettingsFragment()

}