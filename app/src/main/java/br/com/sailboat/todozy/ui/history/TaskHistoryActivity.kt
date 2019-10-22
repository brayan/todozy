package br.com.sailboat.todozy.ui.history

import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.ui.base.BaseActivity

class TaskHistoryActivity : BaseActivity() {

    companion object {
        fun start(context: Context) = with(context) {
            val intent = Intent(this, TaskHistoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun newFragmentInstance() = TaskHistoryFragment()
}