package br.com.sailboat.todozy.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.sailboat.todozy.features.tasks.presentation.list.startTaskListActivity

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startTaskListActivity()
        finish()
    }

}