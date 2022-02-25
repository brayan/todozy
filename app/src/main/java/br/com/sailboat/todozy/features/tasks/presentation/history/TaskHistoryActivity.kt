package br.com.sailboat.todozy.features.tasks.presentation.history

import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.core.presentation.helper.getBundle
import br.com.sailboat.todozy.core.presentation.helper.getTaskId
import br.com.sailboat.todozy.core.presentation.helper.hasTaskId
import br.com.sailboat.todozy.utility.android.activity.BaseActivity

fun Context.startTaskHistoryActivity() {
    val intent = Intent(this, TaskHistoryActivity::class.java)
    startActivity(intent)
}

class TaskHistoryActivity : BaseActivity() {

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