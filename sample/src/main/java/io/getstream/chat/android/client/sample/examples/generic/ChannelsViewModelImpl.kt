package io.getstream.chat.android.client.sample.examples.generic

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.events.ConnectedEvent
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.sample.common.Channel
import io.getstream.chat.android.client.utils.observable.ChatObservable
import io.getstream.chat.android.client.utils.observable.Subscription


class ChannelsViewModelImpl(
    val client: ChatClient,
    val channelsRepository: ChannelsRepository,
    val user: User,
    val token: String
) : LifecycleObserver {

    var subs = mutableListOf<Subscription<*>>()

    fun init(initListener: () -> Unit) {
        client.setUser(user, token)

        subs.add(client.events()
            .filter { event -> event is ConnectedEvent }
            .first()
            .subscribe { initListener() })
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        client.disconnect()
    }

    fun channels(): ChatObservable<RepoResult<List<Channel>>> {
        return channelsRepository.getChannels()
    }
}