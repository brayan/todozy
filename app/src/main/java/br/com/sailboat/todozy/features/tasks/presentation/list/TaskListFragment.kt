package br.com.sailboat.todozy.features.tasks.presentation.list

import android.os.Build
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.extensions.hideFabWhenScrolling
import br.com.sailboat.todozy.core.extensions.logDebug
import br.com.sailboat.todozy.core.presentation.base.mvp.BaseMVPFragment
import br.com.sailboat.todozy.core.presentation.helper.*
import br.com.sailboat.todozy.features.about.presentation.startAboutActivity
import br.com.sailboat.todozy.features.settings.presentation.startSettingsActivity
import br.com.sailboat.todozy.features.tasks.domain.model.TaskMetrics
import br.com.sailboat.todozy.features.tasks.presentation.details.startTaskDetailsActivity
import br.com.sailboat.todozy.features.tasks.presentation.form.startTaskFormActivity
import br.com.sailboat.todozy.features.tasks.presentation.history.startTaskHistoryActivity
import kotlinx.android.synthetic.main.ept_view.*
import kotlinx.android.synthetic.main.frg_task_list.*
import kotlinx.android.synthetic.main.task_metrics.*
import org.koin.android.ext.android.inject

class TaskListFragment : BaseMVPFragment<TaskListContract.Presenter>(), TaskListContract.View {

    override val presenter: TaskListContract.Presenter by inject()
    override val layoutId = R.layout.frg_task_list

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

    override fun initViews() {
        setMainTitle()

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        tvEmptyViewMessagePrimary.setText(R.string.no_tasks)
        tvEmptyViewMessageSecondary.setText(R.string.ept_click_to_add)

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

    override fun hideEmptyView() = ept_view.gone()

    override fun hideMetrics() = appbar_task_list__fl__metrics.gone()

    override fun hideTasks() = recycler.gone()

    override fun removeTaskFromList(position: Int) {
        recycler.adapter?.notifyItemRemoved(position)
    }

    override fun setMainTitle() = toolbar.setTitle(R.string.app_name)

    override fun setEmptyTitle() {
        toolbar.title = ""
    }

    override fun showEmptyView() = ept_view.visible()

    override fun showErrorOnSwipeTask() {
        // TODO: IMPROVE THIS
        Toast.makeText(activity, getString(R.string.msg_error), Toast.LENGTH_LONG).show()
    }

    override fun showMetrics(taskMetrics: TaskMetrics) {
        tvMetricsFire.text = taskMetrics.consecutiveDone.toString()
        tvMetricsDone.text = taskMetrics.doneTasks.toString()
        tvMetricsNotDone.text = taskMetrics.notDoneTasks.toString()

        if (taskMetrics.consecutiveDone == 0) {
            tvMetricsFire.gone()
        } else {
            tvMetricsFire.visible()
        }

        appbar.setExpanded(true, true)
        appbar_task_list__fl__metrics.visible()
    }

    override fun showNewTask() = startTaskFormActivity()

    override fun showSettings() = startSettingsActivity()

    override fun showTasks() = recycler.visible()

    override fun showTaskDetails(taskId: Long) = startTaskDetailsActivity(taskId)

    override fun showTaskHistory() {
        activity?.startTaskHistoryActivity()
    }

    override fun updateTasks() {
        recycler.adapter?.notifyDataSetChanged()
    }

    override fun navigateToAbout() {
        activity?.run { startAboutActivity(AboutHelper(this).getInfo()) }
    }

    private fun initRecyclerView() {
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

}