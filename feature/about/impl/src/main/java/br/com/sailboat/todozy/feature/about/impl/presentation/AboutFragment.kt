package br.com.sailboat.todozy.feature.about.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.feature.about.impl.databinding.FragmentAboutBinding
import br.com.sailboat.todozy.feature.about.impl.presentation.viewmodel.AboutViewAction
import br.com.sailboat.todozy.feature.about.impl.presentation.viewmodel.AboutViewIntent
import br.com.sailboat.todozy.feature.about.impl.presentation.viewmodel.AboutViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import br.com.sailboat.uicomponent.impl.R as UiR

internal class AboutFragment : Fragment() {
    private val viewModel: AboutViewModel by viewModel()

    private var aboutAdapter: AboutAdapter? = null

    private lateinit var binding: FragmentAboutBinding

    companion object {
        fun newInstance() = AboutFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentAboutBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeViewModel()
        viewModel.dispatchViewIntent(AboutViewIntent.OnStart)
    }

    private fun observeViewModel() {
        observeActions()
        viewModel.viewState.itemViews.observe(viewLifecycleOwner) { items ->
            aboutAdapter?.submitList(items)
        }
    }

    private fun observeActions() {
        viewModel.viewState.viewAction.observe(viewLifecycleOwner) { action ->
            when (action) {
                is AboutViewAction.ShowErrorLoadingAbout -> showErrorLoadingAbout()
            }
        }
    }

    private fun initViews() {
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar() = with(binding) {
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.setSupportActionBar(toolbar)
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        toolbar.setTitle(UiR.string.about)
    }

    private fun initRecyclerView() = with(binding) {
        recycler.layoutManager = LinearLayoutManager(activity)
        recycler.adapter = AboutAdapter().apply {
            aboutAdapter = this
        }
    }

    private fun showErrorLoadingAbout() {
        Toast.makeText(activity, UiR.string.msg_error, Toast.LENGTH_SHORT).show()
    }
}
