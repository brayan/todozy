package br.com.sailboat.todozy.features.tasks.presentation.list

import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.core.extensions.logDebug
import br.com.sailboat.todozy.core.presentation.base.BaseActivity


fun Context.startTaskListActivity() {
    val intent = Intent(this, TaskListActivity::class.java)
    startActivity(intent)
}

class TaskListActivity : BaseActivity() {
    override fun newFragmentInstance() = TaskListFragment()
}