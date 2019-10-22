package br.com.sailboat.todozy.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.R

abstract class BaseActivity : AppCompatActivity() {

    private val TAG = "TAG_FRAGMENT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frame_layout)

        val fragment: Fragment? = supportFragmentManager.findFragmentByTag(TAG)

        if (fragment == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout, newFragmentInstance(), TAG)
                    .commit()
        }
    }

    protected abstract fun newFragmentInstance(): Fragment

}