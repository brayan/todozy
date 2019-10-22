package br.com.sailboat.todozy.ui.task.list

import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.ui.base.BaseActivity

class TaskListActivity : BaseActivity() {

    companion object {
        fun start(context: Context) = with(context) {
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun newFragmentInstance() = TaskListFragment()
}