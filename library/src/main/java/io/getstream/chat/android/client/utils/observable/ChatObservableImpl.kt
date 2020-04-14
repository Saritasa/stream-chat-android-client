package io.getstream.chat.android.client.utils.observable

open class ChatObservableImpl<T> : ChatObservable<T> {

    private val subscriptions = mutableListOf<Subscription<T>>()
    private val filters = mutableListOf<(event: T) -> Boolean>()

    private var first = false
    private var cache = false
    private val cached = mutableListOf<T>()

    private var onSubListener: (() -> Unit)? = null
    private var onUnSubListener: (() -> Unit)? = null

    fun cache(): ChatObservable<T> {
        cache = true
        return this
    }

    fun onSubscribe(listener: () -> Unit) {
        onSubListener = listener
    }

    fun onUnSubscribe(listener: () -> Unit) {
        onUnSubListener = listener
    }

    fun onNext(event: T) {
        if (cache) cached.add(event)
        subscriptions.forEach { it.onNext(event) }
    }

    override fun filter(predicate: (event: T) -> Boolean): ChatObservable<T> {
        filters.add(predicate)
        return this
    }

    override fun filter(vararg types: Class<out T>): ChatObservable<T> {
        return filter { event ->
            types.any { type ->
                type.isInstance(event)
            }
        }
    }

    override fun first(): ChatObservable<T> {
        first = true
        return this
    }

    override fun subscribe(listener: (T) -> Unit): Subscription<T> {
        val result = Subscription(this, listener, filters, first)
        subscriptions.add(result)

        if (cache) {
            cached.forEach { data ->
                subscriptions.forEach { sub ->
                    sub.onNext(data)
                }
            }
        }

        onSubListener?.invoke()

        return result
    }

    override fun unsubscribe(subscription: Subscription<T>) {
        subscriptions.remove(subscription)

        onUnSubListener?.invoke()
    }

    protected fun hasSubscribers(): Boolean {
        return subscriptions.isNotEmpty()
    }

    protected fun addSubscription(sub: Subscription<T>) {
        subscriptions.add(sub)
    }

    protected fun makeSubscription(listener: (T) -> Unit): Subscription<T> {
        return Subscription(this, listener, filters, first)
    }
}