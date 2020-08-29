package br.com.sailboat.todozy.features.tasks.presentation.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.core.presentation.base.BaseActivity
import br.com.sailboat.todozy.core.presentation.helper.*

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