package io.getstream.chat.android.client.sample.examples.generic


import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.Fragment
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.sample.App
import io.getstream.chat.android.client.sample.Chat
import io.getstream.chat.android.client.sample.examples.generic.binders.ChannelsListViewModelBinder
import kotlinx.android.synthetic.main.activity_channels.*

class ViewModels {

    fun createChannelsList(
        fragment: Fragment,
        user: User,
        token: String
    ): ChannelsViewModelImpl {
        return ChannelsViewModelImpl(Chat.instance.client, App.channelsRepositoryLive, user, token)
    }

    fun bindChannelsList(
        activity: AppCompatActivity,
        user: User,
        token: String
    ) {
        val vm = ChannelsViewModelImpl(Chat.instance.client, App.channelsRepositoryLive, user, token)
        val view = find(activity, ChatChannelsList::class.java)!!
        ChannelsListViewModelBinder().bind(activity.lifecycle, view, vm)
    }

    fun bindChannelsList(
        fragment: Fragment,
        user: User,
        token: String
    ) {
        val vm = ChannelsViewModelImpl(Chat.instance.client, App.channelsRepositoryLive, user, token)
        val view = find(fragment, ChatChannelsList::class.java)!!
        ChannelsListViewModelBinder().bind(fragment.lifecycle, view, vm)
    }

    private fun <T> find(fragment: Fragment, clazz: Class<T>): T? {
        return find(fragment.view!!, clazz)
    }

    private fun <T> find(activity: AppCompatActivity, clazz: Class<T>): T? {
        return find(activity.root, clazz)
    }

    private fun <T> find(view: View, clazz: Class<T>): T? {

        if (clazz.isInstance(view)) {
            return view as T
        } else {
            if (view is ViewGroup) {
                view.children.forEach {
                    val result = find(it, clazz)

                    if (result != null) return result
                }
            }
        }

        return null
    }
}