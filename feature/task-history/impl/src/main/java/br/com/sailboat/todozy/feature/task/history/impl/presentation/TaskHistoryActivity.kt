package br.com.sailboat.todozy.feature.task.history.impl.presentation

import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.utility.android.activity.BaseActivity
import br.com.sailboat.uicomponent.impl.helper.getBundle
import br.com.sailboat.uicomponent.impl.helper.getTaskId
import br.com.sailboat.uicomponent.impl.helper.hasTaskId

internal fun Context.startTaskHistoryActivity() {
    val intent = Intent(this, TaskHistoryActivity::class.java)
    startActivity(intent)
}

internal class TaskHistoryActivity : BaseActivity() {
    override fun newFragmentInstance(): TaskHistoryFragment {
        val bundle = intent.getBundle()

        return if (bundle?.hasTaskId() == true) {
            val taskId = bundle.getTaskId()
            TaskHistoryFragment.newInstance(taskId)
        } else {
            TaskHistoryFragment.newInstance()
        }
    }
}
