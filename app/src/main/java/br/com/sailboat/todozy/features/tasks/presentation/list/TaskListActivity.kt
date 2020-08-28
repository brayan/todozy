package br.com.sailboat.todozy.features.tasks.presentation.list

import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.core.extensions.logDebug
import br.com.sailboat.todozy.core.presentation.base.BaseActivity

class TaskListActivity : BaseActivity() {

    companion object {
        fun start(context: Context) = with(context) {
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
            "Start TaskListActivity".logDebug()
        }
    }

    override fun newFragmentInstance() = TaskListFragment()
}