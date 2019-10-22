package br.com.sailboat.todozy.ui.task.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.data.helper.AlarmManagerHelper
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.ui.base.mpv.BaseMVPFragment
import br.com.sailboat.todozy.ui.helper.*
import br.com.sailboat.todozy.ui.helper.SwipeTaskLeftRight
import br.com.sailboat.todozy.ui.history.TaskHistoryActivity
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

    private var searchViewOpen = false

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_list, menu)
        initSearchViewMenu(menu)
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

        ept_view__tv__message1.setText(R.string.no_tasks)
        ept_view__tv__message2.setText(R.string.ept_click_to_add)

        recycler.run {
            adapter = TaskListAdapter(object : TaskListAdapter.Callback {
                override val taskViewList = presenter.getTaskViewList()
                override fun onClickTask(position: Int) = presenter.onClickTask(position)
            })
            layoutManager = LinearLayoutManager(activity)
        }

        val itemTouchHelper = ItemTouchHelper(SwipeTaskLeftRight(activity!!, object: SwipeTaskLeftRight.Callback {
            override fun onSwiped(position: Int, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    presenter.onTaskSwipedLeft(position)
                } else {
                    presenter.onTaskSwipedRight(position)
                }
            }
        }))

        itemTouchHelper.attachToRecyclerView(recycler)

        fab.setOnClickListener { presenter.onClickNewTask() }

        recycler.hideFabWhenScrolling(fab)
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

    override fun setTitle(title: String) {
        toolbar.title = title
    }

    override fun showEmptyView() = ept_view.visible()

    override fun showErrorOnSwipeTask() {
        // TODO: IMPROVE THIS
        Toast.makeText(activity, getString(R.string.msg_error), Toast.LENGTH_LONG).show()
    }

    override fun showMetrics(taskMetrics: TaskMetrics) {
        task_metrics__tv__fire.text = taskMetrics.consecutiveDone.toString()
        task_metrics__tv__done.text = taskMetrics.doneTasks.toString()
        task_metrics__tv__not_done.text = taskMetrics.notDoneTasks.toString()

        if (taskMetrics.consecutiveDone == 0) {
            task_metrics__tv__fire.gone()
        } else {
            task_metrics__tv__fire.visible()
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

    private fun initSearchViewMenu(menu: Menu) {
        val menuSearchView = menu.findItem(R.id.menu_search)

        if (menuSearchView != null) {
            val searchView = menuSearchView.actionView as SearchView
            initListenerSearchView(searchView)
            updateSearchView(searchView, menuSearchView)
        }
    }

    private fun initListenerSearchView(searchView: SearchView) {
        searchView.setOnSearchClickListener { searchViewOpen = true }

        searchView.setOnCloseListener {
            searchViewOpen = false
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            private var timer = Timer()
            private val DELAY: Long = 500

            override fun onQueryTextChange(text: String): Boolean {
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        presenter.submitTextForSearch(text)
                    }
                }, DELAY)

                return true

            }

            override fun onQueryTextSubmit(text: String): Boolean {
                return true
            }
        })
    }

    private fun updateSearchView(searchView: SearchView, menuSearchView: MenuItem) {

        menuSearchView.isVisible = searchViewOpen

        if (searchViewOpen) {
            menuSearchView.expandActionView()
            searchView.setQuery(presenter.getTextForSearch(), true)
            searchView.clearFocus()
            searchView.isFocusable = true
            searchView.isIconified = false
            searchView.requestFocusFromTouch()
        }

    }

}