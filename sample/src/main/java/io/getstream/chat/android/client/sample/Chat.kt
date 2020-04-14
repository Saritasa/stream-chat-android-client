package io.getstream.chat.android.client.sample

import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.sample.examples.generic.ViewModels

interface Chat {

    val viewModels: ViewModels
    val client: ChatClient

    companion object {
        val instance: Chat by lazy {
            object : Chat {
                override val viewModels: ViewModels
                    get() = ViewModels()

                override val client: ChatClient
                    get() = App.client
            }
        }
    }
}