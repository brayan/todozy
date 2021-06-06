package br.com.sailboat.todozy.features.about.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.base.mvp.BaseMVPFragment
import br.com.sailboat.todozy.core.presentation.model.ItemView
import br.com.sailboat.todozy.databinding.FrgAboutBinding
import org.koin.android.ext.android.inject
import java.util.*

class AboutFragment : BaseMVPFragment<AboutContract.Presenter>(), AboutContract.View,
    AboutAdapter.Callback {

    override val presenter: AboutContract.Presenter by inject()

    companion object {
        fun newInstance(itemViews: ArrayList<ItemView>): AboutFragment {
            val args = Bundle()
            args.putSerializable("RECYCLER_ITEMS", itemViews)
            val fragment = AboutFragment()
            fragment.arguments = args

            return fragment
        }
    }

    private lateinit var binding: FrgAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FrgAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initViews() {
        initToolbar()
        initRecyclerView()
    }

    override fun updateList() {
        binding.recycler.adapter?.notifyDataSetChanged()
    }

    override fun extractAboutInfo(): List<ItemView> {
        return arguments?.getSerializable("RECYCLER_ITEMS") as List<ItemView>
    }

    override fun getAbout() = presenter.getAbout()

    private fun initToolbar() = with(binding) {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbar)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        toolbar.setTitle(R.string.about)
    }

    private fun initRecyclerView() = with(binding) {
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = AboutAdapter(this@AboutFragment)
    }

}