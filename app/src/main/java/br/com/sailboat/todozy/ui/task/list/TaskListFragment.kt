package br.com.sailboat.todozy.ui.task.list

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.data.helper.AlarmManagerHelper
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.ui.base.mpv.BaseMVPFragment
import br.com.sailboat.todozy.ui.helper.*
import br.com.sailboat.todozy.ui.helper.SwipeTaskLeftRight
import br.com.sailboat.todozy.ui.task.history.TaskHistoryActivity
import br.com.sailboat.todozy.ui.settings.SettingsActivity
import br.com.sailboat.todozy.ui.task.details.TaskDetailsActivity
import br.com.sailboat.todozy.ui.task.insert.InsertTaskActivity
import kotlinx.android.synthetic.main.ept_view.*
import kotlinx.android.synthetic.main.frg_task_list.*
import kotlinx.android.synthetic.main.task_metrics.*
import org.koin.android.ext.android.inject
import java.util.*

class TaskListFragment : BaseMVPFragment<TaskListContract.Presenter>(), TaskListContract.View {

    override val presenter: TaskListContract.Presenter by inject()
    override val layoutId = R.layout.frg_task_list

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_fragments_history -> presenter.onClickMenuTaskHistory()
            R.id.menu_fragments_settings -> presenter.onClickMenuSettings()
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
    }

    override fun checkAndPerformFirstTimeConfigurations() {
        activity?.run { FirstTimeConfigurationsHelper().checkAndPerformFirstTimeConfigurations(this) }
    }

    override fun closeNotifications() {
        activity?.apply { NotificationHelper().closeNotifications(this) }
    }

    override fun hideEmptyView() = ept_view.gone()

    override fun hideMetrics()  = appbar_task_list__fl__metrics.gone()

    override fun hideTasks() = recycler.gone()

    override fun removeTaskFromList(position: Int) {
        recycler.adapter?.notifyItemRemoved(position)
    }

    override fun setAlarmUpdateTasks() {
        activity?.run { AlarmManagerHelper(this).setAlarmUpdateTasks() }
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

    override fun showNewTask() = InsertTaskActivity.start(this)

    override fun showSettings() = SettingsActivity.start(this)

    override fun showTasks() = recycler.visible()

    override fun showTaskDetails(taskId: Long) = TaskDetailsActivity.start(this, taskId)

    override fun showTaskHistory() {
        activity?.run { TaskHistoryActivity.start(this) }
    }

    override fun updateTasks() {
        recycler.adapter?.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        recycler.run {
            adapter = TaskListAdapter(object : TaskListAdapter.Callback {
                override val taskViewList = presenter.getTaskViewList()
                override fun onClickTask(position: Int) = presenter.onClickTask(position)
            })
            layoutManager = LinearLayoutManager(activity)
        }

        val itemTouchHelper = ItemTouchHelper(SwipeTaskLeftRight(activity!!, object : SwipeTaskLeftRight.Callback {
            override fun onSwipeLeft(position: Int) = presenter.onSwipeTaskLeft(position)
            override fun onSwipeRight(position: Int) = presenter.onSwipeTaskRight(position)
        }))

        recycler.hideFabWhenScrolling(fab)

        itemTouchHelper.attachToRecyclerView(recycler)
    }

}