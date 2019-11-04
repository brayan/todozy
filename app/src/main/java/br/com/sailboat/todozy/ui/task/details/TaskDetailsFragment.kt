package br.com.sailboat.todozy.ui.task.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.ui.base.mpv.BaseMVPFragment
import br.com.sailboat.todozy.ui.dialog.TwoOptionsDialog
import br.com.sailboat.todozy.ui.helper.DialogHelper
import br.com.sailboat.todozy.ui.helper.getTaskId
import br.com.sailboat.todozy.ui.helper.putTaskId
import br.com.sailboat.todozy.ui.history.TaskHistoryActivity
import br.com.sailboat.todozy.ui.task.insert.InsertTaskActivity
import kotlinx.android.synthetic.main.appbar_task_details.*
import kotlinx.android.synthetic.main.appbar_task_details.toolbar
import kotlinx.android.synthetic.main.fab.*
import kotlinx.android.synthetic.main.recycler.*
import kotlinx.android.synthetic.main.task_metrics.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.ext.android.inject


class TaskDetailsFragment : BaseMVPFragment<TaskDetailsContract.Presenter>(), TaskDetailsContract.View, TaskDetailsAdapter.Callback {

    override val presenter: TaskDetailsContract.Presenter by inject()
    override val layoutId = R.layout.frg_task_details
    override val taskId: Long = arguments?.getTaskId() ?: EntityHelper.NO_ID
    override val details = presenter.details

    companion object {
        fun newInstance(taskId: Long): TaskDetailsFragment = with(TaskDetailsFragment()) {
            val bundle = Bundle()
            bundle.putTaskId(taskId)
            arguments = bundle
            return this
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_details, menu)
    }

    override fun initViews() {
        fab.setImageResource(R.drawable.ic_edit_white_24dp)
        fab.setOnClickListener { presenter.onClickEditTask() }

        recycler.adapter = TaskDetailsAdapter(this)

        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { activity!!.onBackPressed() }
    }

    override fun setDoneTasks(amount: String) {
        tvMetricsDone.text = amount
    }

    override fun setNotDoneTasks(amount: String) {
        tvMetricsNotDone.text = amount
    }

    override fun showDialogDeleteTask() {
        DialogHelper().showDeleteDialog(fragmentManager!!, activity!!, object: TwoOptionsDialog.PositiveCallback {
            override fun onClickPositiveOption() {
                presenter.onClickDeleteTask()
            }
        })
    }

    override fun startInsertTaskActivity(taskId: Long) {
        InsertTaskActivity.startToEdit(this, taskId)
    }

    override fun showMetrics() {
        appbar_task_details__fl__metrics.setVisibility(View.VISIBLE)
    }

    override fun hideMetrics() {
        appbar_task_details__fl__metrics.setVisibility(View.GONE)
    }

    override fun startTaskHistoryActivity(taskId: Long) {
        TaskHistoryActivity.start(activity!!)
    }

    override fun setFire(fire: String) {
        tvMetricsFire.setText(fire)
    }

    override fun showFire() {
        task_metrics__ll__fire.setVisibility(View.VISIBLE)
    }

    override fun hideFire() {
        task_metrics__ll__fire.setVisibility(View.GONE)
    }

    override fun updateDetails() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun closeProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showErrorOnDeleteTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun closeWithResultOk() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setTaskDetailsTitle() {
        toolbar.setTitle(R.string.task_details)
    }

    override fun setEmptyTitle() {
        toolbar.title = ""
    }

    override fun closeWithResultNotOk() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                presenter.onClickMenuDelete()
                return true
            }
            R.id.menu_history -> {
                presenter.onClickMenuHistory()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    
}