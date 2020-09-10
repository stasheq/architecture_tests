package me.szymanski.listtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.szymanski.listtest.ui.list.ListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ListFragment())
                .commitNow()
        }
    }
}
