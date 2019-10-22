package br.com.sailboat.todozy.ui.about

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.ui.base.BaseActivity
import br.com.sailboat.todozy.ui.model.ItemView
import java.util.ArrayList

class AboutActivity: BaseActivity() {

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