package io.getstream.chat.android.client.sample.examples.generic

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.getstream.chat.android.client.sample.R

class ChannelsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channels)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.root, ChannelsListFragment())
                .commit()
        }
    }
}