package br.com.sailboat.todozy.features.about.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.databinding.FrgAboutBinding
import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.utility.android.mvp.BaseMVPFragment
import org.koin.android.ext.android.inject

class AboutFragment : BaseMVPFragment<AboutContract.Presenter>(), AboutContract.View,
    AboutAdapter.Callback {

    override val presenter: AboutContract.Presenter by inject()

    companion object {
        fun newInstance(uiModels: ArrayList<UiModel>): AboutFragment {
            val args = Bundle()
            args.putSerializable("RECYCLER_ITEMS", uiModels)
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

    override fun extractAboutInfo(): List<UiModel> {
        return arguments?.getSerializable("RECYCLER_ITEMS") as List<UiModel>
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