package br.com.sailboat.todozy.feature.task.details.impl.presentation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.uicomponent.helper.getBundle
import br.com.sailboat.todozy.uicomponent.helper.getTaskId
import br.com.sailboat.todozy.uicomponent.helper.putBundle
import br.com.sailboat.todozy.uicomponent.helper.putTaskId
import br.com.sailboat.todozy.uicomponent.model.RequestCode
import br.com.sailboat.todozy.utility.android.activity.BaseActivity

fun Fragment.startTaskDetailsActivity(taskId: Long) {
    val intent = Intent(activity, TaskDetailsActivity::class.java)
    val bundle = Bundle()

    bundle.putTaskId(taskId)
    intent.putBundle(bundle)

    startActivityForResult(intent, RequestCode.TASK_DETAILS.ordinal)
}

class TaskDetailsActivity : BaseActivity() {

    override fun newFragmentInstance(): TaskDetailsFragment {
        val taskId = intent.getBundle()?.getTaskId() ?: throw NullPointerException()
        return TaskDetailsFragment.newInstance(taskId)
    }

}