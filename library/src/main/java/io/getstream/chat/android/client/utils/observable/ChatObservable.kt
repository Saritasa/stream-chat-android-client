package io.getstream.chat.android.client.utils.observable

interface ChatObservable<T> {
    fun filter(predicate: (event: T) -> Boolean): ChatObservable<T>
    fun filter(vararg types: Class<out T>): ChatObservable<T>
    fun first(): ChatObservable<T>
    fun subscribe(listener: (T) -> Unit): Subscription<T>
    fun unsubscribe(subscription: Subscription<T>)
}