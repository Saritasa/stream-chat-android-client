package io.getstream.chat.android.client.sample.examples.generic

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataWrapper
import androidx.lifecycle.Observer
import io.getstream.chat.android.client.utils.observable.ChatObservable
import io.getstream.chat.android.client.utils.observable.Subscription

class RepoData<T> internal constructor(
    dbLive: LiveData<T>,
    onActiveHandler: () -> Unit = {},
    onInactiveHandler: () -> Unit = {}
){

    private val dbLive = LiveDataWrapper(dbLive, onActiveHandler, onInactiveHandler, {})

    @WorkerThread
    fun postValue(value: T) {
        dbLive.postValue(value)
    }

    @UiThread
    fun setValue(value: T) {
        dbLive.setValue(value)
    }

    fun getValue(): T? {
        return dbLive.getValue()
    }

    fun observe(activity: AppCompatActivity, listener: (T) -> Unit) {
        dbLive.observe(activity, Observer { value -> listener(value) })
    }

    fun observe(fragment: Fragment, listener: (T) -> Unit) {
        dbLive.observe(fragment.viewLifecycleOwner, Observer { value -> listener(value) })
    }
}