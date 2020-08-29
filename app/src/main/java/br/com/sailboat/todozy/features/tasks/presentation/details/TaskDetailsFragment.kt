package br.com.sailboat.todozy.features.tasks.presentation.details

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.core.presentation.base.mvp.BaseMVPFragment
import br.com.sailboat.todozy.core.presentation.dialog.TwoOptionsDialog
import br.com.sailboat.todozy.core.presentation.helper.*
import br.com.sailboat.todozy.features.tasks.presentation.form.TaskFormActivity
import br.com.sailboat.todozy.features.tasks.presentation.form.startTaskFormActivity
import br.com.sailboat.todozy.features.tasks.presentation.history.TaskHistoryActivity
import br.com.sailboat.todozy.features.tasks.presentation.history.startTaskHistoryActivity
import kotlinx.android.synthetic.main.appbar_task_details.*
import kotlinx.android.synthetic.main.fab.*
import kotlinx.android.synthetic.main.recycler.*
import kotlinx.android.synthetic.main.task_metrics.*
import org.koin.android.ext.android.inject


class TaskDetailsFragment : BaseMVPFragment<TaskDetailsContract.Presenter>(), TaskDetailsContract.View {

    override val presenter: TaskDetailsContract.Presenter by inject()
    override val layoutId = R.layout.frg_task_details

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

        recycler.run {
            adapter = TaskDetailsAdapter(object : TaskDetailsAdapter.Callback {
                override val details = presenter.details
            })
            layoutManager = LinearLayoutManager(activity)
        }

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
        DialogHelper().showDeleteDialog(fragmentManager!!, activity!!, object : TwoOptionsDialog.PositiveCallback {
            override fun onClickPositiveOption() {
                presenter.onClickDeleteTask()
            }
        })
    }

    override fun startInsertTaskActivity(taskId: Long) {
        startTaskFormActivity(taskId)
    }

    override fun showMetrics() {
        appbar_task_details__fl__metrics.setVisibility(View.VISIBLE)
    }

    override fun hideMetrics() {
        appbar_task_details__fl__metrics.setVisibility(View.GONE)
    }

    override fun startTaskHistoryActivity(taskId: Long) {
        activity?.startTaskHistoryActivity()
    }

    override fun setFire(fire: String) {
        tvMetricsFire.text = fire
    }

    override fun showFire() {
        task_metrics__ll__fire.visible()
    }

    override fun hideFire() {
        task_metrics__ll__fire.gone()
    }

    override fun updateDetails() {
        recycler.adapter?.notifyDataSetChanged()
    }

    override fun showErrorOnDeleteTask() {
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

    override fun getTaskId(): Long {
        return arguments?.getTaskId() ?: Entity.NO_ID
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