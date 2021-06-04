package br.com.sailboat.todozy.features.tasks.presentation.list

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.extensions.hideFabWhenScrolling
import br.com.sailboat.todozy.core.extensions.logDebug
import br.com.sailboat.todozy.core.presentation.base.mvp.BaseMVPFragment
import br.com.sailboat.todozy.core.presentation.helper.*
import br.com.sailboat.todozy.databinding.FrgTaskListBinding
import br.com.sailboat.todozy.features.about.presentation.startAboutActivity
import br.com.sailboat.todozy.features.settings.presentation.startSettingsActivity
import br.com.sailboat.todozy.features.tasks.domain.model.TaskMetrics
import br.com.sailboat.todozy.features.tasks.presentation.details.startTaskDetailsActivity
import br.com.sailboat.todozy.features.tasks.presentation.form.startTaskFormActivity
import br.com.sailboat.todozy.features.tasks.presentation.history.startTaskHistoryActivity
import org.koin.android.ext.android.inject

class TaskListFragment : BaseMVPFragment<FrgTaskListBinding, TaskListContract.Presenter>(), TaskListContract.View {

    override val presenter: TaskListContract.Presenter by inject()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_list, menu)
        initMenusVisibility(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_fragments_history -> presenter.onClickMenuTaskHistory()
            R.id.menu_fragments_settings -> presenter.onClickMenuSettings()
            R.id.menu_fragments_about -> presenter.onClickMenuAbout()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun initViews() = with(binding) {
        setMainTitle()

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        eptView.tvEmptyViewMessagePrimary.setText(R.string.no_tasks)
        eptView.tvEmptyViewMessageSecondary.setText(R.string.ept_click_to_add)

        initRecyclerView()

        fab.setOnClickListener { presenter.onClickNewTask() }
    }

    override fun onSubmitSearch(search: String) {
        presenter.submitTextForSearch(search)
        "onSubmitSearch: $search".logDebug()
    }

    override fun closeNotifications() {
        activity?.apply { NotificationHelper().closeNotifications(this) }
    }

    override fun hideEmptyView() = binding.eptView.root.gone()

    override fun hideMetrics() = binding.appbarTaskListFlMetrics.gone()

    override fun hideTasks() = binding.recycler.gone()

    override fun removeTaskFromList(position: Int) {
        binding.recycler.adapter?.notifyItemRemoved(position)
    }

    override fun setMainTitle() = binding.toolbar.setTitle(R.string.app_name)

    override fun setEmptyTitle() {
        binding.toolbar.title = ""
    }

    override fun showEmptyView() = binding.eptView.root.visible()

    override fun showErrorOnSwipeTask() {
        // TODO: IMPROVE THIS
        Toast.makeText(activity, getString(R.string.msg_error), Toast.LENGTH_LONG).show()
    }

    override fun showMetrics(taskMetricsModel: TaskMetrics) = with(binding) {
        taskMetrics.tvMetricsFire.text = taskMetricsModel.consecutiveDone.toString()
        taskMetrics.tvMetricsDone.text = taskMetricsModel.doneTasks.toString()
        taskMetrics.tvMetricsNotDone.text = taskMetricsModel.notDoneTasks.toString()

        if (taskMetricsModel.consecutiveDone == 0) {
            taskMetrics.taskMetricsLlFire.gone()
        } else {
            taskMetrics.taskMetricsLlFire.visible()
        }

        appbar.setExpanded(true, true)
        appbarTaskListFlMetrics.visible()
    }

    override fun showNewTask() = startTaskFormActivity()

    override fun showSettings() = startSettingsActivity()

    override fun showTasks() = binding.recycler.visible()

    override fun showTaskDetails(taskId: Long) = startTaskDetailsActivity(taskId)

    override fun showTaskHistory() {
        activity?.startTaskHistoryActivity()
    }

    override fun updateTasks() {
        binding.recycler.adapter?.notifyDataSetChanged()
    }

    override fun navigateToAbout() {
        activity?.run { startAboutActivity(AboutHelper(this).getInfo()) }
    }

    private fun initRecyclerView() = with(binding) {
        recycler.run {
            adapter = TaskListAdapter(object : TaskListAdapter.Callback {
                override val tasksView = presenter.tasksView
                override fun onClickTask(position: Int) = presenter.onClickTask(position)
            })
            layoutManager = LinearLayoutManager(activity)
        }

        activity?.run {
            val itemTouchHelper = ItemTouchHelper(SwipeTaskLeftRight(this, object : SwipeTaskLeftRight.Callback {
                override fun onSwipeLeft(position: Int) = presenter.onSwipeTaskLeft(position)
                override fun onSwipeRight(position: Int) = presenter.onSwipeTaskRight(position)
            }))

            itemTouchHelper.attachToRecyclerView(recycler)
        }

        recycler.hideFabWhenScrolling(fab)
    }

    private fun initMenusVisibility(menu: Menu) {
        val settings = menu.findItem(R.id.menu_fragments_settings)
        val about = menu.findItem(R.id.menu_fragments_about)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            settings.isVisible = true
            about.isVisible = false
        } else {
            settings.isVisible = false
            about.isVisible = true
        }
    }

    override fun getBindingInflater(inflater: LayoutInflater, container: ViewGroup?, b: Boolean): FrgTaskListBinding {
        return FrgTaskListBinding.inflate(inflater, container, b)
    }

}