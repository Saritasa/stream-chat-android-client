package io.getstream.chat.android.client.sample.examples.generic

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import io.getstream.chat.android.client.errors.ChatError
import io.getstream.chat.android.client.sample.R
import io.getstream.chat.android.client.sample.common.Channel
import io.getstream.chat.android.client.sample.common.ChannelsListAdapter
import kotlinx.android.synthetic.main.chat_channels_list.view.*

class ChatChannelsList : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        LayoutInflater.from(context).inflate(R.layout.chat_channels_list, this, true)
        channelsList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    fun setError(error: ChatError) {
        errorView.text = "Error: ${error.message}"

        progressView.visibility = View.GONE
        errorView.visibility = View.VISIBLE
        channelsList.visibility = View.GONE
    }

    fun setProgress() {
        progressView.visibility = View.VISIBLE
        errorView.visibility = View.GONE
        channelsList.visibility = View.GONE
    }

    fun setData(data: List<Channel>) {
        progressView.visibility = View.GONE
        errorView.visibility = View.GONE
        channelsList.visibility = View.VISIBLE

        channelsList.adapter = ChannelsListAdapter(data)
    }
}