package br.com.sailboat.todozy.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.sailboat.todozy.ui.task.list.TaskListActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TaskListActivity.start(this)
        finish()
    }

}