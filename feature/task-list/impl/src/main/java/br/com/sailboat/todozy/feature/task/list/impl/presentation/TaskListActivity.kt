package br.com.sailboat.todozy.feature.task.list.impl.presentation

import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.utility.android.activity.BaseActivity

internal fun Context.startTaskListActivity() {
    val intent = Intent(this, TaskListActivity::class.java)
    startActivity(intent)
}

internal class TaskListActivity : BaseActivity() {
    override fun newFragmentInstance() = TaskListFragment()
}
