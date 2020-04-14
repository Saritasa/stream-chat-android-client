package io.getstream.chat.android.client.sample.examples.generic.binders

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.getstream.chat.android.client.sample.App
import io.getstream.chat.android.client.sample.examples.generic.ChannelsViewModelImpl
import io.getstream.chat.android.client.sample.examples.generic.ChatChannelsList
import io.getstream.chat.android.client.sample.examples.generic.RepoResult
import io.getstream.chat.android.client.utils.observable.Subscription

class ChannelsListViewModelBinder : ViewModelBinder<ChatChannelsList, ChannelsViewModelImpl> {

    override fun bind(lifecycle: Lifecycle, channelsList: ChatChannelsList, vm: ChannelsViewModelImpl) {

        lifecycle.addObserver(vm)

        lifecycle.addObserver(object : LifecycleObserver {

            var sub = mutableListOf<Subscription<*>>()
            val client = App.client

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                vm.init {
                    load()
                }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                sub.forEach { it.unsubscribe() }
                client.disconnect()
            }

            private fun load() {

                sub.add(vm.channels().subscribe {
                    when (it) {
                        is RepoResult.Success -> {
                            channelsList.setData(it.data)
                        }
                        is RepoResult.Loading -> {
                            channelsList.setProgress()
                        }
                        is RepoResult.Error -> {
                            channelsList.setError(it.error)
                        }
                    }
                })
            }
        })
    }

}