package br.com.sailboat.todozy.feature.about.presentation

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.feature.about.presentation.AboutFragment
import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.utility.android.activity.BaseActivity

fun Context.startAboutActivity(uiModels: ArrayList<UiModel>) {
    val starter = Intent(this, AboutActivity::class.java)
    starter.putExtra("RECYCLER_ITEMS", uiModels)
    startActivity(starter)
}

class AboutActivity : BaseActivity() {

    override fun newFragmentInstance(): Fragment {
        val extras = intent.getSerializableExtra("RECYCLER_ITEMS") as ArrayList<UiModel>
        return AboutFragment.newInstance(extras)
    }

}