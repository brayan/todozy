package br.com.sailboat.todozy.features.tasks.presentation.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.core.presentation.base.BaseActivity
import br.com.sailboat.todozy.core.presentation.helper.*

class TaskHistoryActivity : BaseActivity() {

    companion object {
        fun start(context: Context) = with(context) {
            val intent = Intent(this, TaskHistoryActivity::class.java)
            startActivity(intent)
        }

        fun start(fragment: Fragment, taskId: Long) = with(fragment) {
            val intent = Intent(activity, TaskHistoryActivity::class.java)
            val bundle = Bundle()

            bundle.putTaskId(taskId)
            intent.putBundle(bundle)

            startActivityForResult(intent, RequestCode.HISTORY.ordinal)
        }
    }

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