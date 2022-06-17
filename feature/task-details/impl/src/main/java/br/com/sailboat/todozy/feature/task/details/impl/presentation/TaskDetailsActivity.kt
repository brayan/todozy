package br.com.sailboat.todozy.feature.task.details.impl.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import br.com.sailboat.todozy.utility.android.activity.BaseActivity
import br.com.sailboat.uicomponent.impl.helper.getBundle
import br.com.sailboat.uicomponent.impl.helper.getTaskId
import br.com.sailboat.uicomponent.impl.helper.putBundle
import br.com.sailboat.uicomponent.impl.helper.putTaskId

fun Context.startTaskDetailsActivity(
    taskId: Long,
    launcher: ActivityResultLauncher<Intent>,
) {
    val intent = Intent(this, TaskDetailsActivity::class.java)
    val bundle = Bundle()

    bundle.putTaskId(taskId)
    intent.putBundle(bundle)

    launcher.launch(intent)
}

class TaskDetailsActivity : BaseActivity() {

    override fun newFragmentInstance(): TaskDetailsFragment {
        val taskId = intent.getBundle()?.getTaskId() ?: throw NullPointerException()
        return TaskDetailsFragment.newInstance(taskId)
    }
}
