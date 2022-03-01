package br.com.sailboat.todozy.feature.task.form.presentation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.uicomponent.helper.*
import br.com.sailboat.todozy.uicomponent.model.RequestCode
import br.com.sailboat.todozy.utility.android.activity.BaseActivity
import br.com.sailboat.todozy.utility.kotlin.extension.isTrue
import br.com.sailboat.todozy.utility.kotlin.extension.orZero

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

        return if (bundle?.hasTaskId().isTrue()) {
            val taskId = bundle?.getTaskId().orZero()
            TaskFormFragment.newInstance(taskId)
        } else {
            TaskFormFragment.newInstance()
        }
    }

}