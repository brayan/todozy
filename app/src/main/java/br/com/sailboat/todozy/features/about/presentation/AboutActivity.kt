package br.com.sailboat.todozy.features.about.presentation

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.core.presentation.base.BaseActivity
import br.com.sailboat.todozy.core.presentation.model.ItemView
import java.util.*

fun Context.startAboutActivity(itemViews: ArrayList<ItemView>) {
    val starter = Intent(this, AboutActivity::class.java)
    starter.putExtra("RECYCLER_ITEMS", itemViews)
    startActivity(starter)
}

class AboutActivity : BaseActivity() {

    companion object {
        fun start(context: Context, itemViews: ArrayList<ItemView>) {
            val starter = Intent(context, AboutActivity::class.java)
            starter.putExtra("RECYCLER_ITEMS", itemViews)
            context.startActivity(starter)
        }
    }

    override fun newFragmentInstance(): Fragment {
        val extras = intent!!.getSerializableExtra("RECYCLER_ITEMS") as ArrayList<ItemView>
        return AboutFragment.newInstance(extras)
    }

}