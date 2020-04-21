package io.getstream.chat.android.client.sample.examples.generic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.getstream.chat.android.client.sample.App
import io.getstream.chat.android.client.sample.R
import kotlinx.android.synthetic.main.activity_channels.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channels)

        btnClearDb.setOnClickListener {
            Thread {
                App.db.clearAllTables()
            }.start()

        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.root, ChannelsListFragment())
                .commit()
        }
    }

    fun goToChannel(cid: String) {

    }
}