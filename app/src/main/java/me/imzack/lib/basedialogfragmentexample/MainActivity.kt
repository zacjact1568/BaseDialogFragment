package me.imzack.lib.basedialogfragmentexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG_EXAMPLE = "example"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vDialogButton.setOnClickListener {
            ExampleDialogFragment.Builder(this)
                    .setTitle("Hello")
                    .setContent("This is a ExampleDialogFragment instance.")
                    .build()
                    .show(supportFragmentManager, TAG_EXAMPLE)
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)

        if (fragment.tag == TAG_EXAMPLE) {
            // 在这里设置监听，才能在 Activity 重建时恢复
            (fragment as ExampleDialogFragment).okButtonClickListener = { Toast.makeText(this, "You touched the \"OK\" button!", Toast.LENGTH_SHORT).show() }
        }
    }
}
