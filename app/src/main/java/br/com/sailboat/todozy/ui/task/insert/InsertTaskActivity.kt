package br.com.sailboat.todozy.ui.task.insert

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.ui.base.BaseActivity
import br.com.sailboat.todozy.ui.helper.*

class InsertTaskActivity : BaseActivity() {


    companion object {

        fun start(fragment: Fragment) = with(fragment) {
            val intent = Intent(activity, InsertTaskActivity::class.java)
            startActivityForResult(intent, RequestCode.INSERT_TASK.ordinal)
        }

        fun startToEdit(fragment: Fragment, taskId: Long) = with(fragment) {
            val intent = Intent(activity, InsertTaskActivity::class.java)
            val bundle = Bundle()

            bundle.putTaskId(taskId)
            intent.putBundle(bundle)

            startActivityForResult(intent, RequestCode.INSERT_TASK.ordinal)
        }

    }

    override fun newFragmentInstance(): InsertTaskFragment {

        val bundle = intent.getBundle()

        return if (bundle?.hasTaskId() == true) {
            val taskId = bundle.getTaskId()
            InsertTaskFragment.newInstance(taskId)

        } else {
            InsertTaskFragment.newInstance()
        }
    }

}