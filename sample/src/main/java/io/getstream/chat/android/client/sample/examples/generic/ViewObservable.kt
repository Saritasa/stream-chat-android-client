package io.getstream.chat.android.client.sample.examples.generic

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.getstream.chat.android.client.utils.observable.ChatObservableImpl
import io.getstream.chat.android.client.utils.observable.Subscription

class ViewObservable<T>(val ld: LiveData<T>) : ChatObservableImpl<T>() {

    fun bindView(activity: AppCompatActivity) {
        ld.observe(activity, Observer { data -> onNext(data) })
    }

    fun bindView(fragment: Fragment) {
        ld.observe(fragment.viewLifecycleOwner, Observer { data -> onNext(data) })
    }

    override fun subscribe(listener: (T) -> Unit): Subscription<T> {

        val subscribe = super.subscribe(listener)

        ld.observe({ LO(this) }, {
            subscribe.onNext(it)
        })

        return subscribe
    }

    private class LO<T>(val o: ViewObservable<T>) : Lifecycle() {

        override fun addObserver(observer: LifecycleObserver) {

        }

        override fun removeObserver(observer: LifecycleObserver) {

        }

        override fun getCurrentState(): State {
            return State.STARTED
        }

    }

    private class Obs<T> : Observer<T> {
        override fun onChanged(t: T) {

        }
    }
}