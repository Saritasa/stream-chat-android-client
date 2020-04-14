//package io.getstream.chat.android.client.sample.common

import android.content.Context
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.*
import io.getstream.chat.android.client.errors.ChatError
import io.getstream.chat.android.client.events.*
import io.getstream.chat.android.client.models.*
import io.getstream.chat.android.client.socket.InitConnectionListener
import io.getstream.chat.android.client.utils.FilterObject
import io.getstream.chat.android.client.utils.ProgressCallback
import java.io.File


val client = ChatClient.instance()
val channelType = ""
val channelId = ""
val cid = ""
val messageId = ""
val message = Message()
val channelController = client.channel(channelType, channelId)
val context: Context = null!!
val parentMessageId = ""
val firstMessageId = ""
val userId = ""

fun getApplicationContext(): Context {
    return null!!
}


fun init() {
    // Typically done in your Application class
    val client = ChatClient.Builder("{{ api_key }}", context).build()

    // Static reference to initialised client
    // val client = ChatClient.instance()
}

fun setUser() {

    val user = User("user-id")
    val token = "{{ chat_user_token }}"

    user.extraData["name"] = "Bender"
    user.extraData["image"] = "https://bit.ly/321RmWb"


    client.setUser(user, token, object : InitConnectionListener() {
        override fun onSuccess(data: ConnectionData) {
            val user = data.user
            val connectionId = data.connectionId
        }

        override fun onError(error: ChatError) {
            error.printStackTrace()
        }
    })
}

fun channel() {

    val channelController = client.channel(channelType, channelId)
    val extraData = mutableMapOf<String, Any>()

    extraData["name"] = "Talking about life"

    // watching a channel"s state
    // note how the withWatch() argument ensures that we are watching the channel for any changes/new messages
    val request = QueryChannelRequest()
        .withData(extraData)
        .withMessages(20)
        .withWatch()

    channelController.query(request).enqueue {
        if (it.isSuccess) {
            val channel = it.data()
        } else {
            it.error().printStackTrace()
        }
    }
}

fun sendMessage() {


    // prepare the message
    val message = Message()
    message.text = "hello world"

    // send the message to the channel
    channelController.sendMessage(message).enqueue {
        if (it.isSuccess) {
            val message = it.data()
        } else {
            it.error().printStackTrace()
        }
    }
}

fun events() {
    val subscription = client.events().subscribe { event ->
        if (event is NewMessageEvent) {
            val message = event.message
        }
    }
    subscription.unsubscribe()
}

fun initClient() {
    // Typically done in your Application class

    val apiKey = "{{ api_key }}"
    val token = "{{ chat_user_token }}"
    val context = getApplicationContext()
    val client = ChatClient.Builder(apiKey, context).build()

    // Set the user to establish the websocket connection
    // Usually done when you open the chat interface
    // extraData allows you to add any custom fields you want to store about your user
    // the UI components will pick up name and image by default

    val user = User("bender")
    user.extraData["image"] = "https://bit.ly/321RmWb"
    user.extraData["name"] = "Bender"

    client.setUser(user, token, object : InitConnectionListener() {

        override fun onSuccess(data: ConnectionData) {
            val user = data.user
            val connectionId = data.connectionId
        }

        override fun onError(error: ChatError) {
            error.printStackTrace()
        }
    })
}

fun setGuestUser() {
    val userId = "user-id"
    val userName = "bender"
    client.getGuestToken(userId, userName).enqueue {
        val token = it.data().token
        val user = it.data().user

        client.setUser(user, token)
    }
}

fun setAnon() {
    client.setAnonymousUser()
}

fun switch() {
    client.disconnect()
    client.setUser(User("bender"), "{{ chat_user_token }}")
}

fun queryUsers1() {

    val filter = Filters.`in`("id", listOf("john", "jack", "jessie"))
    val offset = 0
    val limit = 10
    val request = QueryUsersRequest(filter, offset, limit)

    client.queryUsers(request).enqueue {
        val users = it.data()
    }
}

fun queryUsers2() {
    val filter = Filters.`in`("id", listOf("jessica"))
    val offset = 0
    val limit = 10
    val sort = QuerySort().desc("last_active")

    val request = QueryUsersRequest(filter, offset, limit, sort)

    client.queryUsers(request).enqueue {
        val users = it.data()
    }
}

fun sendMessage2() {
    val message = Message()
    message.text =
        "Josh I told them I was pesca-pescatarian. Which is one who eats solely fish who eat other fish."
    message.extraData["anotherCustomField"] = 234

    // add an image attachment to the message
    val attachment = Attachment()
    attachment.type = "image"
    attachment.imageUrl = "https://bit.ly/2K74TaG"
    attachment.fallback = "test image"
    // add some custom data to the attachment
    attachment.extraData["myCustomField"] = 123

    message.attachments.add(attachment)

    // include the user id of the mentioned user
    message.mentionedUsers.add(User("josh-id"))

    channelController.sendMessage(message).enqueue {
        val message = it.data()
    }
}

fun getMessage() {
    channelController.getMessage("message-id").enqueue {
        val message = it.data()
    }
}

fun updateMessage() {
    // update some field of the message
    message.text = "my updated text"
    // send the message to the channel
    channelController.updateMessage(message).enqueue {
        val message = it.data()
    }
}

fun deleteMessage() {
    channelController.deleteMessage(messageId).enqueue {
        val deletedMessage = it.data()
    }
}

fun sendMessage3() {
    val message = Message()
    message.text = "Check this bear out https://imgur.com/r/bears/4zmGbMN"

    // send the message to the channel
    channelController.sendMessage(message).enqueue {
        val sentMessage = it.data()
    }
}

fun sendFileAndImage() {

    val imageFile = File("path")
    val anyOtherFile = File("path")

    // upload an image
    channelController.sendImage(imageFile, object : ProgressCallback {
        override fun onSuccess(file: String) {

        }

        override fun onError(error: ChatError) {

        }

        override fun onProgress(progress: Long) {

        }

    })

    // upload a file
    channelController.sendFile(anyOtherFile, object : ProgressCallback {
        override fun onSuccess(file: String) {

        }

        override fun onError(error: ChatError) {

        }

        override fun onProgress(progress: Long) {

        }

    })
}

fun sendReaction() {
    val score = 5
    val reaction = Reaction("message-id", "like", score)

    channelController.sendReaction(reaction).enqueue {
        val reaction = it.data()
    }
}

fun deleteReaction() {
    channelController.deleteReaction("message-id", "like").enqueue {
        val message = it.data()
    }
}

fun getReactions() {
    // get the first 10 reactions
    channelController.getReactions(messageId, 0, 10).enqueue {
        val reactions = it.data()
    }

    // get the second 10 reactions
    channelController.getReactions(messageId, 10, 10).enqueue {
        val reactions = it.data()
    }

    // get 10 reactions after particular reaction
    val reactionId = "reaction-id"
    channelController.getReactions(messageId, reactionId, 10).enqueue {
        val reactions = it.data()
    }
}

fun sendParentMessage() {

    // set the parent id to make sure a message shows up in a thread
    val parentMessage = Message()
    val message = Message()
    message.text = "hello world"
    message.parentId = parentMessage.id

    // send the message to the channel
    channelController.sendMessage(message).enqueue {
        val message = it.data()
    }
}

fun getReplies() {
    val limit = 20
    // retrieve the first 20 messages inside the thread
    client.getReplies(parentMessageId, limit).enqueue {
        val replies = it.data()
    }
    // retrieve the 20 more messages before the message with id "42"
    client.getRepliesMore(parentMessageId, "42", limit).enqueue {
        val replies = it.data()
    }
}

fun search() {

    val offset = 0
    val limit = 10
    val query = "supercalifragilisticexpialidocious"

    client.searchMessages(
        SearchMessagesRequest(
            query,
            offset, limit,
            Filters.`in`("members", listOf("john"))
        )
    ).enqueue {
        val messages = it.data()
    }
}

fun listeningSomeEvent() {
    val subscription = channelController
        .events()
        .filter { event -> event.type == "message.deleted" }
        .subscribe { messageDeletedEvent ->

        }
    subscription.unsubscribe()
}

fun listenAllEvents() {
    val subscription = channelController.events().subscribe { event ->
        if (event is NewMessageEvent) {

        }
    }
    subscription.unsubscribe()
}

fun clientEvents() {
    // subscribe to all client events and log the unread_count field
    client.events().subscribe { event ->
        if (event.totalUnreadCount != null) {
            println("unread messages count is now: ${event.totalUnreadCount}");
        }
        if (event.unreadChannels != null) {
            println("unread channels count is now: ${event.unreadChannels}");
        }

        if (event is ConnectedEvent) {
            // the initial count of unread messages is returned by client.setUser
            val unreadCount = event.user!!.totalUnreadCount
            val unreadChannels = event.user!!.unreadChannels
        }
    }

}

fun connectionEvents() {
    client.events().subscribe { event ->
        when (event) {
            is ConnectedEvent -> {
                //socket is connected
            }
            is ConnectingEvent -> {
                //socket is connecting
            }
            is DisconnectedEvent -> {
                //socket is disconnected
            }
        }
    }
}

fun stopListening() {
    val subscription = channelController.events().subscribe { event -> }
    subscription.unsubscribe()
}

fun notificationEvents() {
    channelController
        .events()
        .filter{it.type == "notification.added_to_channel"}
        .subscribe { notificationEvent ->
            notificationEvent
        }
}

fun createChannelController() {
    val channelController = client.channel(channelType, channelId)
}

fun watch() {
    channelController.watch().enqueue {
        val channel = it.data()
    }
}

fun stopWathing() {
    channelController.stopWatching().enqueue {
        val channel = it.data()
    }
}

fun queryChannels() {
    val filter = Filters
        .`in`("members", "thierry")
        .put("type", "messaging")
    val offset = 0
    val limit = 10
    val sort = QuerySort().desc("last_message_at")
    val request = QueryChannelsRequest(filter, offset, limit, sort)
    request.watch = true
    request.state = true
    client.queryChannels(request).enqueue {
        val channels = it.data()
    }
}

fun filter1() {
    val filter = Filters
        .`in`("members", "thierry")
        .put("type", "messaging")
}

fun filter2() {
    val filter = Filters
        .`in`("status", "pending", "open", "new")
        .put("agent_id", userId)
}

fun queryChannelsPaginating() {
    val filter = Filters.`in`("members", "thierry")
    val offset = 0
    val limit = 10
    val request = QueryChannelsRequest(filter, offset, limit)

    client.queryChannels(request).enqueue {
        val channels = it.data()
    }
}

fun updateChannel() {
    val updateMessage = Message()
    updateMessage.text = "Thierry changed the channel color to green"
    channelController.update(updateMessage).enqueue {
        val channel = it.data()
    }
}

fun addMembersAndRemoveMembers() {
    channelController.addMembers("thierry", "josh").enqueue {
        val channel = it.data()
    }
    channelController.removeMembers("thierry", "josh").enqueue {
        val channel = it.data()
    }
}

fun createChannel() {
    val members = listOf("thierry", "tommaso")
    client.createChannel("messaging", members).enqueue {
        val channel = it.data()
    }
}

fun inviteMembers() {
    val members = listOf("thierry", "tommaso")
    val invites = listOf("nick")
    val data = mutableMapOf<String, Any>()

    data["members"] = members
    data["invites"] = invites

    client.createChannel(channelType, channelId, data).enqueue {
        val channel = it.data()
    }
}

fun acceptInvite() {
    channelController.acceptInvite("Nick joined this channel!").enqueue {
        val channel = it.data()
    }
}

fun rejectInvite() {
    channelController.rejectInvite().enqueue {
        val channel = it.data()
    }
}

fun queryInvited() {
    val offset = 0
    val limit = 10
    val request = QueryChannelsRequest(FilterObject("invite", "accepted"), offset, limit)
    client.queryChannels(request).enqueue {
        val channels = it.data()
    }
}

fun queryRejected() {
    val offset = 0
    val limit = 10
    val request = QueryChannelsRequest(FilterObject("invite", "rejected"), offset, limit)
    client.queryChannels(request).enqueue {
        val channels = it.data()
    }
}

fun delete() {
    channelController.delete().enqueue {
        val channel = it.data()
    }
}

fun hide() {

    // hides the channel until a new message is added there
    channelController.hide().enqueue {
        val channel = it.data()
    }

    // shows a previously hidden channel
    channelController.show().enqueue {
        val channel = it.data()
    }

    // hide the channel and clear the message history
    channelController.hide(true).enqueue {
        val channel = it.data()
    }
}

fun muting() {
    client.setUser(User(userId), "{{ chat_user_token }}", object : InitConnectionListener() {
        override fun onSuccess(data: ConnectionData) {
            // mutes contains the list of channel mutes
            val mutes = data.user.mutes
        }
    })

    val channelController = client.channel(channelType, channelId)

    channelController.muteCurrentUser().enqueue {
        val mute = it.data()
    }

    channelController.muteUser(userId).enqueue {
        val mute = it.data()
    }

    channelController.unmuteUser(userId).enqueue {
        val mute = it.data()
    }
}

fun queryMuted() {

    // retrieve channels excluding muted ones

    val offset = 0
    val limit = 10
    val notMutedFilter = Filters.eq("muted", false)
    client.queryChannels(QueryChannelsRequest(notMutedFilter, offset, limit))

    // retrieve muted channels

    val mutedFilter = Filters.eq("muted", true)
    client.queryChannels(QueryChannelsRequest(mutedFilter, offset, limit))
}

fun unmute() {
    // unmute channel for current user
    channelController.unmuteCurrentUser().enqueue {
        val mute = it.data()
    }
}

fun setInvisbleUser() {
    val user = User(userId)
    user.invisible = true
    client.setUser(user, "{{ chat_user_token }}")
}

fun queryUsers() {

    // If you pass presence: true to channel.watch it will watch the list of user presence changes.
    // Note that you can listen to at most 10 users using this API call

    val watchRequest = WatchChannelRequest()
    watchRequest.data["members"] = listOf("john", "jack")

    channelController.watch(watchRequest).enqueue {
        val channel = it.data()
    }

    // queryChannels allows you to listen to the members of the channels that are returned
    // so this does the same thing as above and listens to online status changes for john and jack

    val wathRequestWithPresence = WatchChannelRequest()
    wathRequestWithPresence.presence = true
    wathRequestWithPresence.data["members"] = listOf("john", "jack")

    channelController.watch(wathRequestWithPresence).enqueue {
        val channel = it.data()
    }

    // queryUsers allows you to listen to user presence changes for john and jack

    val offset = 0
    val limit = 2
    val usersFilter = Filters.`in`("id", listOf("john", "jack"))
    val usersQuery = QueryUsersRequest(usersFilter, offset, limit)
    usersQuery.presence = true
    client.queryUsers(usersQuery).enqueue {
        val users = it.data()
    }
}

fun startAndStopTyping() {
    // sends a typing.start event at most once every two seconds
    channelController.keystroke()
    // sends the typing.stop event
    channelController.stopTyping()
}

fun reveivingTypingEvents() {
    // add typing start event handling
    channelController.events().filter{it.type == EventType.TYPING_STOP}.subscribe { startTyping ->

    }
    // add typing top event handling
    channelController.events().filter{it.type == EventType.TYPING_STOP}.subscribe { startTyping ->

    }
}

fun unreadSetUser() {
    client.setUser(User(userId), "{{ chat_user_token }}", object : InitConnectionListener() {
        override fun onSuccess(data: ConnectionData) {
            val user = data.user
            val unreadChannels = user.unreadChannels
            val totalUnreadCount = user.totalUnreadCount
        }
    })
}

fun markRead() {
    channelController.markRead().enqueue {
        val readEvent = it.data()
    }
}

fun unreadEvents() {
    channelController.events()
        .filter { event ->
            event.unreadChannels != null && event.totalUnreadCount != null
        }.subscribe { event ->
            val unreadChannels = event.unreadChannels
            val totalUnreadCount = event.totalUnreadCount
        }
}

fun updateUsers() {
    val user = User(userId)
    client.updateUser(user).enqueue {
        val user = it.data()
    }
}

fun channelPagination() {

    val filter = Filters.lessThanEquals("cid", cid)
    val offset = 0
    val limit = 10

    val request = QueryChannelsRequest(filter, offset, limit)
    client.queryChannels(request).enqueue {
        val channels = it.data()
    }
}