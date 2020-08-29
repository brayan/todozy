package br.com.sailboat.todozy.features.tasks.presentation.form

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.core.extensions.safe
import br.com.sailboat.todozy.core.presentation.base.BaseActivity
import br.com.sailboat.todozy.core.presentation.helper.*

fun Fragment.startTaskFormActivity() {
    val intent = Intent(activity, TaskFormActivity::class.java)
    startActivityForResult(intent, RequestCode.INSERT_TASK.ordinal)
}

fun Fragment.startTaskFormActivity(taskId: Long) {
    val intent = Intent(activity, TaskFormActivity::class.java)
    val bundle = Bundle()

    bundle.putTaskId(taskId)
    intent.putBundle(bundle)

    startActivityForResult(intent, RequestCode.INSERT_TASK.ordinal)
}

class TaskFormActivity : BaseActivity() {

    override fun newFragmentInstance(): TaskFormFragment {
        val bundle = intent.getBundle()

        return if (bundle?.hasTaskId().safe()) {
            val taskId = bundle?.getTaskId().safe()
            TaskFormFragment.newInstance(taskId)
        } else {
            TaskFormFragment.newInstance()
        }
    }

}