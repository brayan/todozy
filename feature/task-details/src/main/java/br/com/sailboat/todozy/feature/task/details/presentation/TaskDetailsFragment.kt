package br.com.sailboat.todozy.feature.task.details.presentation

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.feature.task.details.R
import br.com.sailboat.todozy.feature.task.details.databinding.FrgTaskDetailsBinding
import br.com.sailboat.todozy.feature.task.form.presentation.startTaskFormActivity
import br.com.sailboat.todozy.feature.task.history.presentation.startTaskHistoryActivity
import br.com.sailboat.todozy.uicomponent.dialog.TwoOptionsDialog
import br.com.sailboat.todozy.uicomponent.helper.DialogHelper
import br.com.sailboat.todozy.uicomponent.helper.getTaskId
import br.com.sailboat.todozy.uicomponent.helper.putTaskId
import br.com.sailboat.todozy.utility.android.mvp.BaseMVPFragment
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import org.koin.android.ext.android.inject


class TaskDetailsFragment : BaseMVPFragment<TaskDetailsContract.Presenter>(),
    TaskDetailsContract.View {

    override val presenter: TaskDetailsContract.Presenter by inject()

    companion object {
        fun newInstance(taskId: Long): TaskDetailsFragment = with(TaskDetailsFragment()) {
            val bundle = Bundle()
            bundle.putTaskId(taskId)
            arguments = bundle
            return this
        }
    }

    private lateinit var binding: FrgTaskDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FrgTaskDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_task_details, menu)
    }

    override fun initViews() {
        binding.fab.root.setImageResource(R.drawable.ic_edit_white_24dp)
        binding.fab.root.setOnClickListener { presenter.onClickEditTask() }

        binding.recycler.recycler.run {
            adapter = TaskDetailsAdapter(object : TaskDetailsAdapter.Callback {
                override val details = presenter.details
            })
            layoutManager = LinearLayoutManager(activity)
        }

        (activity as? AppCompatActivity)?.setSupportActionBar(binding.appbarTaskDetails.toolbar)
        binding.appbarTaskDetails.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        binding.appbarTaskDetails.toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
    }

    override fun setDoneTasks(amount: String) {
        binding.appbarTaskDetails.taskMetrics.tvMetricsDone.text = amount
    }

    override fun setNotDoneTasks(amount: String) {
        binding.appbarTaskDetails.taskMetrics.tvMetricsNotDone.text = amount
    }

    override fun showDialogDeleteTask() {
        activity?.run {
            DialogHelper().showDeleteDialog(
                childFragmentManager,
                this,
                object : TwoOptionsDialog.PositiveCallback {
                    override fun onClickPositiveOption() {
                        presenter.onClickDeleteTask()
                    }
                })
        }
    }

    override fun startInsertTaskActivity(taskId: Long) {
        startTaskFormActivity(taskId)
    }

    override fun showMetrics() {
        binding.appbarTaskDetails.root.visible()
    }

    override fun hideMetrics() {
        binding.appbarTaskDetails.root.gone()
    }

    override fun startTaskHistoryActivity(taskId: Long) {
        activity?.startTaskHistoryActivity()
    }

    override fun setFire(fire: String) {
        binding.appbarTaskDetails.taskMetrics.tvMetricsFire.text = fire
    }

    override fun showFire() {
        binding.appbarTaskDetails.taskMetrics.tvMetricsFire.visible()
    }

    override fun hideFire() {
        binding.appbarTaskDetails.taskMetrics.tvMetricsFire.gone()
    }

    override fun updateDetails() {
        binding.recycler.recycler.adapter?.notifyDataSetChanged()
    }

    override fun showErrorOnDeleteTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setTaskDetailsTitle() {
        binding.appbarTaskDetails.toolbar.setTitle(R.string.task_details)
    }

    override fun setEmptyTitle() {
        binding.appbarTaskDetails.toolbar.title = ""
    }

    override fun closeWithResultNotOk() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTaskId(): Long {
        return arguments?.getTaskId() ?: Entity.NO_ID
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> presenter.onClickMenuDelete()
            R.id.menu_history -> presenter.onClickMenuHistory()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

}