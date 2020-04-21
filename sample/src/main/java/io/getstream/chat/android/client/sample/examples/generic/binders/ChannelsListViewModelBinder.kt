package io.getstream.chat.android.client.sample.examples.generic.binders

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.getstream.chat.android.client.sample.App
import io.getstream.chat.android.client.sample.common.Channel
import io.getstream.chat.android.client.sample.examples.generic.ChannelsViewModelImpl
import io.getstream.chat.android.client.sample.examples.generic.ChatChannelsList
import io.getstream.chat.android.client.sample.examples.generic.RepoResult
import io.getstream.chat.android.client.sample.examples.generic.RepoResult.*
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

            var state: RepoResult<List<Channel>> = Idle

            private fun reduce(current: RepoResult<List<Channel>>, new: RepoResult<List<Channel>>) {
                if (current is Idle && new is CacheLoading) {
                    channelsList.showCacheLoading()
                } else if (current is CacheLoading && new is EmptyCache) {
                    // keep showing progress
                } else if (current is CacheLoading && new is StaleCache) {
                    channelsList.hideCacheLoading()
                    channelsList.showCacheData(new.data)
                } else if (current is EmptyCache && new is NetworkLoading) {
                    // keep showing progress
                } else if (current is StaleCache && new is NetworkLoading) {
                    channelsList.showNetworkLoading()
                } else if (current is NetworkLoading && new is FreshCache) {
                    channelsList.hideCacheLoading()
                    channelsList.hideNetworkLoading()
                    channelsList.showLatestData(new.data)
                } else if (current is NetworkLoading && new is NoNetworkUpdate) {
                    channelsList.hideNetworkLoading()
                } else if (current is NoNetworkUpdate && new is FreshCache) {
                    channelsList.showLatestData(new.data)
                } else if (current is FreshCache && new is FreshCache) {
                    channelsList.showLatestData(new.data)
                }
                state = new
            }

            private fun load() {

                sub.add(vm.channels().subscribe {
                    Log.d("channels-state", it.toString())
                    reduce(state, it)
                })
            }
        })
    }

}