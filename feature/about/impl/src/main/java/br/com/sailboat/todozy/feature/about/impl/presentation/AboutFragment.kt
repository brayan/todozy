package br.com.sailboat.todozy.feature.about.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.feature.about.impl.R
import br.com.sailboat.todozy.feature.about.impl.databinding.FragmentAboutBinding
import br.com.sailboat.todozy.feature.about.impl.presentation.viewmodel.AboutViewAction
import br.com.sailboat.todozy.feature.about.impl.presentation.viewmodel.AboutViewModel
import br.com.sailboat.todozy.feature.about.impl.presentation.viewmodel.AboutViewState
import br.com.sailboat.todozy.utility.android.fragment.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class AboutFragment : BaseFragment() {

    private val viewModel: AboutViewModel by viewModel()

    private var aboutAdapter: AboutAdapter? = null

    private lateinit var binding: FragmentAboutBinding

    companion object {
        fun newInstance() = AboutFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentAboutBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.dispatchViewIntent(AboutViewAction.OnStart)
    }

    private fun observeViewModel() {
        observeActions()
        viewModel.viewState.itemViews.observe(viewLifecycleOwner) { items ->
            aboutAdapter?.submitList(items)
        }
    }

    private fun observeActions() {
        viewModel.viewState.action.observe(viewLifecycleOwner) { action ->
            when (action) {
                is AboutViewState.Action.ShowErrorLoadingAbout -> showErrorLoadingAbout()
            }
        }
    }

    override fun initViews() {
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar() = with(binding) {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbar)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        toolbar.setTitle(R.string.about)
    }

    private fun initRecyclerView() = with(binding) {
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = AboutAdapter().apply {
            aboutAdapter = this
        }
    }

    private fun showErrorLoadingAbout() {
        Toast.makeText(activity, R.string.msg_error, Toast.LENGTH_SHORT).show()
    }
}
