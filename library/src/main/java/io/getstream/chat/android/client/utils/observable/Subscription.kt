package io.getstream.chat.android.client.utils.observable

open class Subscription<T>(
    private val observable: ChatObservable<T>,
    private var listener: ((T) -> Unit)?,
    private val filters: MutableList<(event: T) -> Boolean> = mutableListOf(),
    private val firstOnly: Boolean
) {

    private var deliveredCounter = 0

    open fun unsubscribe() {
        listener = null
        filters.clear()
        observable.unsubscribe(this)
    }

    fun onNext(event: T) {

        if (filters.isEmpty()) {
            deliver(event)
        } else {
            filters.forEach { filtered ->
                if (filtered(event)) {
                    deliver(event)
                    return
                }
            }
        }

    }

    private fun deliver(event: T) {
        if (firstOnly) {
            if (deliveredCounter == 0) {
                deliveredCounter = 1
                listener?.invoke(event)
            }
        } else {
            deliveredCounter++
            listener?.invoke(event)
        }

    }
}