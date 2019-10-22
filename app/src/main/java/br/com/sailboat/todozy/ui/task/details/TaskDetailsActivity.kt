package br.com.sailboat.todozy.ui.task.details

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.ui.base.BaseActivity
import br.com.sailboat.todozy.ui.helper.*
import java.lang.NullPointerException

class TaskDetailsActivity : BaseActivity() {

    companion object {
        fun start(fragment: Fragment, taskId: Long) = with(fragment) {
            val intent = Intent(activity, TaskDetailsActivity::class.java)
            val bundle = Bundle()

            bundle.putTaskId(taskId)
            intent.putBundle(bundle)

            startActivityForResult(intent, RequestCode.TASK_DETAILS.ordinal)
        }
    }

    override fun newFragmentInstance(): TaskDetailsFragment {
        val taskId = intent.getBundle()?.getTaskId() ?: throw NullPointerException()
        return TaskDetailsFragment.newInstance(taskId)
    }

}