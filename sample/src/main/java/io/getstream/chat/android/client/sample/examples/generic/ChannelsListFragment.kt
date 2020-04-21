package io.getstream.chat.android.client.sample.examples.generic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.sample.Chat
import io.getstream.chat.android.client.sample.R
import io.getstream.chat.android.client.sample.common.BaseChannelsListFragment
import kotlinx.android.synthetic.main.fragment_channels_generic.*

class ChannelsListFragment : BaseChannelsListFragment() {

    val token =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiYmVuZGVyIn0.3KYJIoYvSPgTURznP8nWvsA2Yj2-vLqrm-ubqAeOlcQ"
    val user = User("bender")

    override fun reload() {
        //load()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_channels_generic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val chat = Chat.instance

        chat.viewModels.bindChannelsList(
            this,
            user,
            token
        )


        chatChannelsList.onChannelClickListener {
            (activity as HomeActivity).goToChannel(it.remoteId)
        }
    }
}