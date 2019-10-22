package br.com.sailboat.todozy.ui.settings

import android.content.Intent
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.ui.base.BaseActivity
import br.com.sailboat.todozy.ui.helper.RequestCode

class SettingsActivity : BaseActivity() {

    companion object {
        fun start(fromFragment: Fragment) {
            val intent = Intent(fromFragment.activity, SettingsActivity::class.java)
            fromFragment.startActivityForResult(intent, RequestCode.SETTINGS.ordinal)
        }
    }

    override fun newFragmentInstance() = SettingsFragment()

}