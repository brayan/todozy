package br.com.sailboat.todozy.features.about.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.base.mvp.BaseMVPFragment
import br.com.sailboat.todozy.core.presentation.model.ItemView
import kotlinx.android.synthetic.main.frg_about.*
import org.koin.android.ext.android.inject
import java.util.*

class AboutFragment : BaseMVPFragment<AboutContract.Presenter>(), AboutContract.View, AboutAdapter.Callback {

    override val presenter: AboutContract.Presenter by inject()
    override val layoutId = R.layout.frg_about

    companion object {
        fun newInstance(itemViews: ArrayList<ItemView>): AboutFragment {
            val args = Bundle()
            args.putSerializable("RECYCLER_ITEMS", itemViews)
            val fragment = AboutFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun initViews() {
        initToolbar()
        initRecyclerView()
    }

    override fun updateList() {
        recycler.adapter?.notifyDataSetChanged()
    }

    override fun extractAboutInfo(): List<ItemView> {
        return arguments?.getSerializable("RECYCLER_ITEMS") as List<ItemView>
    }

    override fun getAbout() = presenter.getAbout()

    private fun initToolbar() {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbar)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        toolbar.setTitle(R.string.about)
    }

    private fun initRecyclerView() {
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = AboutAdapter(this)
    }

}