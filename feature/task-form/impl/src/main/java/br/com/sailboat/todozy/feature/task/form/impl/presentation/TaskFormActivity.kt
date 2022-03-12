package br.com.sailboat.todozy.feature.task.form.impl.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.uicomponent.helper.*
import br.com.sailboat.todozy.uicomponent.model.RequestCode
import br.com.sailboat.todozy.utility.android.activity.BaseActivity
import br.com.sailboat.todozy.utility.kotlin.extension.isTrue
import br.com.sailboat.todozy.utility.kotlin.extension.orZero

fun Context.startTaskFormActivity(launcher: ActivityResultLauncher<Intent>) {
    val intent = Intent(this, TaskFormActivity::class.java)
    launcher.launch(intent)
}

fun Context.startTaskFormActivity(
    taskId: Long,
    launcher: ActivityResultLauncher<Intent>
) {
    val intent = Intent(this, TaskFormActivity::class.java)
    val bundle = Bundle()

    bundle.putTaskId(taskId)
    intent.putBundle(bundle)

    launcher.launch(intent)
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