package br.com.sailboat.todozy.feature.about.impl.presentation

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.utility.android.activity.BaseActivity

fun Context.startAboutActivity() {
    val starter = Intent(this, AboutActivity::class.java)
    startActivity(starter)
}

internal class AboutActivity : BaseActivity() {
    override fun newFragmentInstance(): Fragment {
        return AboutFragment.newInstance()
    }
}
