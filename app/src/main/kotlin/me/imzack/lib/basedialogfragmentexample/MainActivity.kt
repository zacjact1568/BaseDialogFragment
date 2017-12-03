package me.imzack.lib.basedialogfragmentexample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import me.imzack.lib.basedialogfragment.SimpleDialogFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vDialogButton.setOnClickListener {
            SimpleDialogFragment.newInstance(
                    this,
                    "Hello",
                    "This is a SimpleDialogFragment instance.",
                    { Toast.makeText(this, "You closed this dialog!", Toast.LENGTH_SHORT).show() }
            ).show(supportFragmentManager)
        }
    }
}
