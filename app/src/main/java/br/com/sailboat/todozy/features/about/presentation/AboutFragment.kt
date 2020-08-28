package br.com.sailboat.todozy.features.about.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.base.mvp.BaseMVPFragment
import br.com.sailboat.todozy.core.presentation.model.ItemView
import org.koin.android.ext.android.inject
import java.util.*

class AboutFragment : BaseMVPFragment<AboutContract.Presenter>(), AboutContract.View, AboutAdapter.Callback {

    override val presenter: AboutContract.Presenter by inject()
    override val layoutId = R.layout.frg_about

    private var toolbar: Toolbar? = null
    private var recycler: RecyclerView? = null


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
        recycler!!.adapter!!.notifyDataSetChanged()
    }

    override fun getAbout() = presenter.getAbout()

    private fun initToolbar() {
        toolbar = getView()!!.findViewById(R.id.toolbar)
        val appCompatActivity = getActivity() as AppCompatActivity?
        appCompatActivity?.setSupportActionBar(toolbar)
        appCompatActivity?.getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        toolbar!!.setNavigationOnClickListener { getActivity()!!.onBackPressed() }
        toolbar!!.setTitle(R.string.about)
    }

    private fun initRecyclerView() {
        recycler = getView()!!.findViewById(R.id.recycler)
        recycler!!.layoutManager = LinearLayoutManager(getActivity())
        recycler!!.adapter = AboutAdapter(this)
    }

}