package io.getstream.chat.android.client.sample.examples.generic

import android.os.Bundle
import androidx.fragment.app.Fragment

class ChannelFragment : Fragment() {

    lateinit var id: String
    lateinit var type: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        type = arguments!!.getString("type")!!
        id = arguments!!.getString("id")!!
    }

    companion object {
        fun create(channelType: String, channelId: String): ChannelFragment {
            return ChannelFragment().apply {
                arguments = Bundle().apply {
                    putString("type", channelType)
                    putString("id", channelId)
                }
            }
        }
    }
}