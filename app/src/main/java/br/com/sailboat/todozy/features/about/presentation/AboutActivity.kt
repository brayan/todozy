package br.com.sailboat.todozy.features.about.presentation

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.core.presentation.base.BaseActivity
import br.com.sailboat.todozy.core.presentation.model.ItemView

fun Context.startAboutActivity(itemViews: ArrayList<ItemView>) {
    val starter = Intent(this, AboutActivity::class.java)
    starter.putExtra("RECYCLER_ITEMS", itemViews)
    startActivity(starter)
}

class AboutActivity : BaseActivity() {

    override fun newFragmentInstance(): Fragment {
        val extras = intent.getSerializableExtra("RECYCLER_ITEMS") as ArrayList<ItemView>
        return AboutFragment.newInstance(extras)
    }

}