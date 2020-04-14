package io.getstream.chat.android.client.utils.observable

import io.getstream.chat.android.client.errors.ChatError
import io.getstream.chat.android.client.events.*
import io.getstream.chat.android.client.socket.ChatSocketService
import io.getstream.chat.android.client.socket.SocketListener

//TODO hide behind interface
class EventsObservable(private val service: ChatSocketService) : ChatObservableImpl<ChatEvent>() {

    private var ignoreInitState = false
    private var eventsMapper = EventsMapper(this)

    fun ignoreInitState(): EventsObservable {
        this.ignoreInitState = true
        return this
    }

    fun filter(eventType: String): EventsObservable {
        filter { it.type == eventType }
        return this
    }

    override fun subscribe(listener: (ChatEvent) -> Unit): Subscription<ChatEvent> {
        val result = makeSubscription(listener)

        if (!hasSubscribers()) {
            // add listener to socket events only once
            service.addListener(eventsMapper)
        }

        addSubscription(result)

        if (!ignoreInitState) deliverInitState(result)

        return result
    }

    override fun unsubscribe(subscription: Subscription<ChatEvent>) {
        super.unsubscribe(subscription)

        if (!hasSubscribers()) {
            service.removeListener(eventsMapper)
        }
    }

    private fun deliverInitState(subscription: Subscription<ChatEvent>) {

        var firstEvent: ChatEvent? = null

        when (val state = service.state) {
            is ChatSocketService.State.Connected -> firstEvent = state.event
            is ChatSocketService.State.Connecting -> firstEvent = ConnectingEvent()
            is ChatSocketService.State.Disconnected -> firstEvent = DisconnectedEvent()
        }

        if (firstEvent != null) subscription.onNext(firstEvent)
    }

    /**
     * Maps methods of [SocketListener] to events of [ChatObservable]
     */
    private class EventsMapper(val observable: EventsObservable) : SocketListener() {

        override fun onConnecting() {
            observable.onNext(ConnectingEvent())
        }

        override fun onConnected(event: ConnectedEvent) {
            observable.onNext(event)
        }

        override fun onDisconnected() {
            observable.onNext(DisconnectedEvent())
        }

        override fun onEvent(event: ChatEvent) {
            observable.onNext(event)
        }

        override fun onError(error: ChatError) {
            observable.onNext(ErrorEvent(error))
        }
    }
}