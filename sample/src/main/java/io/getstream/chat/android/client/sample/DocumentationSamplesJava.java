package io.getstream.chat.android.client.sample;

import android.content.Context;
import io.getstream.chat.android.client.ChatClient;
import io.getstream.chat.android.client.api.models.*;
import io.getstream.chat.android.client.controllers.ChannelController;
import io.getstream.chat.android.client.errors.ChatError;
import io.getstream.chat.android.client.events.*;
import io.getstream.chat.android.client.models.*;
import io.getstream.chat.android.client.socket.InitConnectionListener;
import io.getstream.chat.android.client.utils.ChatUtils;
import io.getstream.chat.android.client.utils.FilterObject;
import io.getstream.chat.android.client.utils.ProgressCallback;
import io.getstream.chat.android.client.utils.Result;
import io.getstream.chat.android.client.utils.observable.Subscription;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentationSamplesJava {

    static ChatClient client = ChatClient.instance();
    static String channelType = "";
    static String channelId = "";
    static String messageId = "";
    static String parentMessageId = "";
    static String cid = "";
    static String userId;
    static String token;
    static String userName;
    static Message message;
    static ChannelController channelController = client.channel(channelType, channelId);
    static Context context;
    static User user;

    static Function1<Result<Object>, Unit> callback = new Function1<Result<Object>, Unit>() {
        @Override
        public Unit invoke(Result<Object> result) {
            return Unit.INSTANCE;
        }
    };

    static Context getApplicationContext() {
        return null;
    }

    static class QuickStart {

        /**
         * https://getstream.io/nessy/docs/chat_docs/quick_start/introduction
         */
        static class ChatDocsIntroduction {

            static void chatClient() {
                String apiKey = "{{ api_key }}";
                String token = "{{ chat_user_token }}";
                Context context = getApplicationContext();
                ChatClient client = new ChatClient.Builder(apiKey, context).build();

                // Set the user to establish the websocket connection
                // Usually done when you open the chat interface
                // extraData allows you to add any custom fields you want to store about your user
                // the UI components will pick up name and image by default

                User user = new User();
                user.setId("user-id");

                user.getExtraData().put("name", "Bender");
                user.getExtraData().put("image", "https://bit.ly/321RmWb");

                client.setUser(user, token, new InitConnectionListener() {
                    @Override
                    public void onSuccess(@NotNull ConnectionData data) {
                        User user = data.getUser();
                        String connectionId = data.getConnectionId();
                    }

                    @Override
                    public void onError(@NotNull ChatError error) {
                        error.printStackTrace();
                    }
                });
            }

            public static void channels() {
                ChannelController channelController = client.channel(channelType, channelId);
                Map<String, Object> extraData = new HashMap<>();

                extraData.put("name", "Talking about life");

                // watching a channel's state
                // note how the withWatch() argument ensures that we are watching the channel for any changes/new messages
                QueryChannelRequest request = new QueryChannelRequest()
                        .withData(extraData)
                        .withMessages(20)
                        .withWatch();

                channelController.query(request).enqueue(result -> {

                    if (result.isSuccess()) {
                        Channel channel = result.data();
                    } else {
                        result.error().printStackTrace();
                    }

                    return Unit.INSTANCE;
                });
            }

            public static void messages() {

                // prepare the message
                Message message = new Message();
                message.setText("hello world");

                // send the message to the channel
                channelController.sendMessage(message).enqueue(result -> {

                    if (result.isSuccess()) {
                        Message msg = result.data();
                    } else {
                        result.error().printStackTrace();
                    }

                    return Unit.INSTANCE;
                });
            }

            static void events() {
                // Subscribe
                Subscription subscription = client.events().subscribe(event -> {

                    if (event instanceof NewMessageEvent) {
                        Message message = event.message;
                    }

                    return Unit.INSTANCE;
                });
                // Unsubscribe
                subscription.unsubscribe();
            }
        }

        /**
         * https://getstream.io/nessy/docs/chat_docs/quick_start/getting_started_chat
         */
        static class GettingStarted {

            public static void chatClient() {
                User user = new User();
                user.setId("user-id");
                String token = "{{ chat_user_token }}";

                user.getExtraData().put("name", "Bender");
                user.getExtraData().put("image", "https://bit.ly/321RmWb");

                client.setUser(user, token, new InitConnectionListener() {
                    @Override
                    public void onSuccess(@NotNull ConnectionData data) {
                        User user = data.getUser();
                        String connectionId = data.getConnectionId();
                    }

                    @Override
                    public void onError(@NotNull ChatError error) {
                        error.printStackTrace();
                    }
                });
            }

            public static void channels() {
                ChannelController channelController = client.channel(channelType, channelId);
                Map<String, Object> extraData = new HashMap<>();

                extraData.put("name", "Talking about life");

                QueryChannelRequest request = new QueryChannelRequest()
                        .withData(extraData)
                        .withMessages(20)
                        .withWatch();

                channelController.query(request).enqueue(result -> {

                    if (result.isSuccess()) {
                        Channel channel = result.data();
                    } else {
                        result.error().printStackTrace();
                    }

                    return Unit.INSTANCE;
                });
            }

            public static void messages() {
                Message message = new Message();
                message.setText("hello world");

                channelController.sendMessage(message).enqueue(result -> {

                    if (result.isSuccess()) {
                        Message msg = result.data();
                    } else {
                        result.error().printStackTrace();
                    }

                    return Unit.INSTANCE;
                });
            }

            static void events() {
                // Subscribe
                Subscription subscription = client.events().subscribe(event -> {

                    if (event instanceof NewMessageEvent) {
                        Message message = event.message;
                    }

                    return Unit.INSTANCE;
                });
                // Unsubscribe
                subscription.unsubscribe();
            }
        }

    }


    static class ClientAndUsers {

        static class InitialisationAndUsers {

            {
                // Typically done in your Application class
                ChatClient client = new ChatClient.Builder("{{ api_key }}", context).build();

                // Static reference to initialised client
                ChatClient theSameClient = ChatClient.instance();
            }

            static void settingTheUser() {

                User user = new User();
                user.setId("user-id");
                String token = "{{ chat_user_token }}";

                // extraData allows you to add any custom fields you want to store about your user
                user.getExtraData().put("name", "Bender");
                user.getExtraData().put("image", "https://bit.ly/321RmWb");

                client.setUser(user, token, new InitConnectionListener() {
                    @Override
                    public void onSuccess(@NotNull ConnectionData data) {
                        User user = data.getUser();
                        String connectionId = data.getConnectionId();
                    }

                    @Override
                    public void onError(@NotNull ChatError error) {
                        error.printStackTrace();
                    }
                });
            }
        }


        static class TokensAnduthentication {
            static void developmentTokens() {
                String userId = "user-id";
                String token = ChatUtils.devToken(userId);
                User user = new User();
                user.setId(userId);

                client.setUser(user, token, new InitConnectionListener() {
                    @Override
                    public void onSuccess(@NotNull ConnectionData data) {
                        User user = data.getUser();
                        String connectionId = data.getConnectionId();
                    }

                    @Override
                    public void onError(@NotNull ChatError error) {
                        error.printStackTrace();
                    }
                });
            }

            static void tokenExpiration() {
                client.setUser(user, listener -> {
                    String newToken = "fetch a new token from your backend";
                    listener.onSuccess(newToken);
                }, null);
            }

            static void guestUsers() {

                client.getGuestToken(userId, userName).enqueue(
                        new Function1<Result<GuestUser>, Unit>() {
                            @Override
                            public Unit invoke(Result<GuestUser> result) {
                                GuestUser data = result.data();
                                User user = data.component1();
                                String token = data.component2();

                                client.setUser(user, token, null);

                                return Unit.INSTANCE;
                            }
                        }
                );
            }

            static void setAnonymousUsers() {
                client.setAnonymousUser(null);
            }

            static void LoggingOutAndSwitchingUsers() {
                client.disconnect();
                client.setUser(user, token, null);
            }
        }

        static class UpdatingUsers {
            {
                client.updateUser(user).enqueue(result -> {
                    User updatedUser = result.data();
                    return Unit.INSTANCE;
                });
            }
        }

        static class GuestUsers {
            {
                client.getGuestToken(userId, userName).enqueue(result -> {
                    User user = result.data().component1();
                    String token = result.data().component2();

                    client.setUser(user, token, null);

                    return Unit.INSTANCE;
                });
            }
        }

        static class AnonymousUsers {
            {
                client.setAnonymousUser(null);
            }
        }

        static class LoggingOut {
            {
                client.disconnect();
                client.setUser(user, token, null);
            }
        }

        static class QueryingUsers {
            {
                List<String> userIds = new ArrayList();
                userIds.add("john");
                userIds.add("jack");
                userIds.add("jessie");
                FilterObject filter = Filters.in("id", userIds);
                int offset = 0;
                int limit = 10;
                QuerySort sort = new QuerySort();

                QueryUsersRequest request = new QueryUsersRequest(filter, offset, limit, sort, false);

                client.queryUsers(request).enqueue(channelResult -> Unit.INSTANCE);
            }

            {
                List<String> userIds = new ArrayList();
                userIds.add("jessica");
                FilterObject filter = Filters.in("id", userIds);
                int offset = 0;
                int limit = 10;
                QuerySort sort = new QuerySort().desc("last_active");

                QueryUsersRequest request = new QueryUsersRequest(filter, offset, limit, sort, false);

                client.queryUsers(request).enqueue(channelResult -> Unit.INSTANCE);
            }
        }
    }


    static class Messages {
        {
            Message message = new Message();
            message.setText("Josh I told them I was pesca-pescatarian. Which is one who eats solely fish who eat other fish.");
            message.getExtraData().put("anotherCustomField", 234);

            // add an image attachment to the message
            Attachment attachment = new Attachment();
            attachment.setType("image");
            attachment.setImageUrl("https://bit.ly/2K74TaG");
            attachment.setFallback("test image");
            // add some custom data to the attachment
            attachment.getExtraData().put("myCustomField", 123);

            message.getAttachments().add(attachment);

            User user = new User();
            user.setId(userId);
            message.getMentionedUsers().add(user);

            channelController.sendMessage(message).enqueue(result -> null);
        }

        static class MessagesOverview {
            {
                channelController.getMessage(messageId).enqueue(result -> {
                    Message message = result.data();
                    return Unit.INSTANCE;
                });
            }
        }

        static class GetMessage {
            {
                channelController.getMessage(messageId).enqueue(result -> {
                    Message message = result.data();
                    return Unit.INSTANCE;
                });
            }
        }

        static class UpdateMessage {
            {
                message.setText("my updated text");
                channelController.updateMessage(message).enqueue(result -> {
                    Message updatedMessage = result.data();
                    return Unit.INSTANCE;
                });
            }
        }

        static class MessageFormat {
            {
                Message message = new Message();
                message.setText("Check this bear out https://imgur.com/r/bears/4zmGbMN");

                channelController.sendMessage(message).enqueue(result -> Unit.INSTANCE);
            }
        }

        static class FileUploads {
            {
                File imageFile = new File("path");
                File anyOtherFile = new File("path");

                channelController.sendImage(imageFile, new ProgressCallback() {
                    @Override
                    public void onSuccess(@NotNull String file) {

                    }

                    @Override
                    public void onError(@NotNull ChatError error) {

                    }

                    @Override
                    public void onProgress(long progress) {

                    }
                });

                channelController.sendFile(anyOtherFile, new ProgressCallback() {
                    @Override
                    public void onSuccess(@NotNull String file) {

                    }

                    @Override
                    public void onError(@NotNull ChatError error) {

                    }

                    @Override
                    public void onProgress(long progress) {

                    }
                });
            }
        }

        static class Reactions {
            {
                Reaction reaction = new Reaction();
                reaction.setMessageId(messageId);
                reaction.setType("like");
                channelController.sendReaction(reaction).enqueue(result -> Unit.INSTANCE);
            }

            static void removingReaction() {
                channelController.deleteReaction(messageId, "like").enqueue(result -> Unit.INSTANCE);
            }

            static void paginatingReactions() {
                // get the first 10 reactions
                channelController.getReactions(messageId, 0, 10).enqueue(result -> Unit.INSTANCE);

                // get the second 10 reactions
                channelController.getReactions(messageId, 10, 10).enqueue(result -> Unit.INSTANCE);

                // get 10 reactions after particular reaction
                String reactionId = "reaction-id";
                channelController.getReactions(messageId, reactionId, 10).enqueue(result -> Unit.INSTANCE);
            }

            static void cumulativeReactions() {
                Reaction reaction = new Reaction();
                reaction.setMessageId(messageId);
                reaction.setType("like");
                reaction.setScore(5);

                channelController.sendReaction(reaction).enqueue(result -> Unit.INSTANCE);
            }
        }

        static class ThreadsAndReplies {
            {
                // set the parent id to make sure a message shows up in a thread
                Message parentMessage = new Message();
                Message Message = new Message();

                message.setText("hello world");
                message.setParentId(parentMessage.getId());

                channelController.sendMessage(message).enqueue(result -> Unit.INSTANCE);
            }

            static void threadPagination() {
                int limit = 20;
                // retrieve the first 20 messages inside the thread
                client.getReplies(parentMessageId, limit).enqueue(result -> Unit.INSTANCE);

                // retrieve the 20 more messages before the message with id "42"
                client.getRepliesMore(parentMessageId, "42", limit).enqueue(result -> Unit.INSTANCE);
            }
        }

        static class Search {
            {
                int offset = 0;
                int limit = 10;
                String query = "supercalifragilisticexpialidocious";
                ArrayList<String> members = new ArrayList<>();
                members.add("john");

                client.searchMessages(new SearchMessagesRequest(
                        query,
                        offset,
                        limit,
                        Filters.in("members", members)
                )).enqueue(result -> {
                    List<Message> messages = result.data();
                    return Unit.INSTANCE;
                });
            }
        }
    }

    static class Events {


        static class ListeningForEvents {
            {
                Subscription<ChatEvent> subscription = channelController
                        .events()
                        .filter(new Function1<ChatEvent, Boolean>() {
                            @Override
                            public Boolean invoke(ChatEvent chatEvent) {
                                return chatEvent.getType() == "message.deleted";
                            }
                        })
                        .subscribe(messageDeletedEvent -> {
                            return Unit.INSTANCE;
                        });

                subscription.unsubscribe();
            }

            {
                Subscription subscription = channelController
                        .events()
                        .subscribe(chatEvent -> Unit.INSTANCE);

                subscription.unsubscribe();
            }

            static void clientEvents() {
                client.events().subscribe(event -> {

                    if (event.getTotalUnreadCount() != null) {
                        int totalUnreadCount = event.getTotalUnreadCount();
                    }

                    if (event.getUnreadChannels() != null) {
                        int unreadChannels = event.getUnreadChannels();
                    }

                    if (event instanceof ConnectedEvent) {
                        // the initial count of unread messages is returned by client.setUser
                        int totalUnreadCount = event.getUser().getTotalUnreadCount();
                        int unreadChannels = event.getUser().getUnreadChannels();
                    }

                    return Unit.INSTANCE;
                });
            }

            static void connectionEvents() {
                client.events().subscribe(new Function1<ChatEvent, Unit>() {
                    @Override
                    public Unit invoke(ChatEvent event) {

                        if (event instanceof ConnectedEvent) {
                            //socket is connected
                        } else if (event instanceof ConnectingEvent) {
                            //socket is connecting
                        } else if (event instanceof DisconnectedEvent) {
                            //socket is disconnected
                        }

                        return Unit.INSTANCE;
                    }
                });
            }

            static void stopListeningForEvents() {
                Subscription subscription = channelController.events().subscribe(chatEvent -> Unit.INSTANCE);
                subscription.unsubscribe();

            }
        }


        /**
         * https://getstream.io/nessy/docs/chat_docs/events/event_typing
         */
        static class TypingEvents {
            {
                // sends a typing.start event if it's been more than 3000 ms since the last event
                channelController.keystroke().enqueue(result -> Unit.INSTANCE);
                // sends an event typing.stop to all channel participants
                channelController.stopTyping().enqueue(result -> Unit.INSTANCE);
            }
        }

        static class NotificationEvents {
            {
                channelController.events()
                        .filter(new Function1<ChatEvent, Boolean>() {
                            @Override
                            public Boolean invoke(ChatEvent chatEvent) {
                                return chatEvent.getType().equals("notification.added_to_channel");
                            }
                        })
                        .subscribe(addToChannel -> Unit.INSTANCE);
            }
        }


    }

    static class Channels {

        /**
         * https://getstream.io/nessy/docs/chat_docs/channels/initialize_channel
         */
        static class ChannelInitilization {
            {
                ChannelController channelController = client.channel(channelType, channelId);
            }
        }

        /**
         * https://getstream.io/nessy/docs/chat_docs/channels/watch_channel
         */
        static class WatchingChannel {
            {
                channelController.watch().enqueue(result -> {
                    Channel channel = result.data();
                    return Unit.INSTANCE;
                });
            }

            static void unwatch() {
                channelController.stopTyping().enqueue(result -> Unit.INSTANCE);
            }
        }

        /**
         * https://getstream.io/nessy/docs/chat_docs/channels/query_channels
         */
        static class QueryingChannels {
            {
                List<String> members = new ArrayList<>();
                members.add("thierry");
                FilterObject filter = Filters.in("members", members).put("type", "messaging");
                int offset = 0;
                int limit = 10;
                int messageLimit = 10;
                QuerySort sort = new QuerySort().desc("last_message_at");
                QueryChannelsRequest request = new QueryChannelsRequest(filter, offset, limit, sort, messageLimit);
                request.setWatch(true);
                request.setState(true);

                client.queryChannels(request).enqueue(result -> {
                    List<Channel> channels = result.data();
                    return Unit.INSTANCE;
                });
            }

            static void commonFiltersByUseCase() {

                List<String> members = new ArrayList<>();
                members.add("thierry");

                FilterObject messagingAndTeam = Filters
                        .in("members", members)
                        .put("type", "messaging");

                FilterObject support = Filters
                        .in("status", "pending", "open", "new")
                        .put("type", "messaging");
            }

            static void paginatingChannelLists() {
                List<String> members = new ArrayList<>();
                members.add("thierry");
                FilterObject filter = Filters.in("members", members);
                int offset = 0;
                int limit = 10;
                int messageLimit = 10;
                QuerySort sort = new QuerySort();

                QueryChannelsRequest request = new QueryChannelsRequest(filter, offset, limit, sort, messageLimit);

                client.queryChannels(request).enqueue(result -> {
                    List<Channel> channels = result.data();
                    return Unit.INSTANCE;
                });
            }
        }

        static class ChannelPagination {
            {
                FilterObject filter = Filters.lessThanEquals("cid", cid);
                int offset = 0;
                int limit = 10;
                int messageLimit = 10;
                QuerySort sort = new QuerySort();

                QueryChannelsRequest request = new QueryChannelsRequest(filter, offset, limit, sort, messageLimit);

                client.queryChannels(request).enqueue(result -> {
                    List<Channel> channels = result.data();
                    return Unit.INSTANCE;
                });
            }
        }

        /**
         * https://getstream.io/nessy/docs/chat_docs/channels/channel_update
         */
        static class UpdatingChannel {
            {
                Message updateMessage = new Message();
                updateMessage.setText("Thierry changed the channel color to green");
                channelController.updateMessage(updateMessage).enqueue(result -> {
                    Message message = result.data();
                    return Unit.INSTANCE;
                });
            }
        }

        /**
         * https://getstream.io/nessy/docs/chat_docs/channels/channel_members
         */
        static class ChagningChannelMembers {
            static void addingAndRemovingChannelMembers() {
                channelController.addMembers("thierry", "josh").enqueue(result -> {
                    Channel updatedChannel = result.data();
                    return Unit.INSTANCE;
                });

                channelController.removeMembers("thierry", "josh").enqueue(result -> {
                    Channel updatedChannel = result.data();
                    return Unit.INSTANCE;
                });
            }
        }

        /**
         * https://getstream.io/nessy/docs/chat_docs/channels/channel_conversations
         */
        static class OneToOneConversations {
            static void creatingConversations() {
                List<String> members = new ArrayList<>();
                members.add("thierry");
                members.add("tomasso");

                client.createChannel("message", members).enqueue(result -> {
                    Channel newChannel = result.data();
                    return Unit.INSTANCE;
                });
            }
        }

        /**
         * https://getstream.io/nessy/docs/chat_docs/channels/channel_invites
         */
        static class Invites {
            static void invitingUsers() {
                List<String> members = new ArrayList<>();
                members.add("thierry");
                members.add("tomasso");

                List<String> invites = new ArrayList<>();
                invites.add("nick");

                Map<String, Object> data = new HashMap<>();
                data.put("members", members);
                data.put("invites", invites);

                client.createChannel(channelType, channelId, data).enqueue(result -> {
                    Channel newChannel = result.data();
                    return Unit.INSTANCE;
                });
            }

            static void acceptingAndInvite() {
                channelController.acceptInvite("Nick joined this channel!").enqueue(result -> Unit.INSTANCE);
            }

            static void rejectingInvite() {
                channelController.rejectInvite().enqueue(result -> Unit.INSTANCE);
            }

            static void queryForAcceptedInvites() {
                FilterObject filter = new FilterObject("invite", "accepted");
                int offset = 0;
                int limit = 10;
                int messageLimit = 10;
                QuerySort sort = new QuerySort();

                QueryChannelsRequest request = new QueryChannelsRequest(filter, offset, limit, sort, messageLimit);

                client.queryChannels(request).enqueue(result -> {
                    List<Channel> channels = result.data();
                    return Unit.INSTANCE;
                });
            }

            static void queryForRejectedInvites() {
                FilterObject filter = new FilterObject("invite", "rejected");
                int offset = 0;
                int limit = 10;
                int messageLimit = 10;
                QuerySort sort = new QuerySort();

                QueryChannelsRequest request = new QueryChannelsRequest(filter, offset, limit, sort, messageLimit);

                client.queryChannels(request).enqueue(result -> {
                    List<Channel> channels = result.data();
                    return Unit.INSTANCE;
                });
            }
        }

        static class DeletingAndHidingChannel {
            {
                channelController.delete().enqueue(result -> Unit.INSTANCE);
            }

            static void hidingChannel() {
                // hides the channel until a new message is added there
                channelController.hide(false).enqueue(result -> Unit.INSTANCE);
                // shows a previously hidden channel
                channelController.show().enqueue(result -> Unit.INSTANCE);
                // hide the channel and clear the message history
                channelController.hide(true).enqueue(result -> Unit.INSTANCE);
            }
        }

        /**
         * https://getstream.io/nessy/docs/chat_docs/channels/muting_channels
         */
        static class MutingChannels {
            static void channelMute() {
                client.setUser(user, token, new InitConnectionListener() {
                    @Override
                    public void onSuccess(@NotNull ConnectionData data) {
                        User user = data.component1();
                        // mutes contains the list of channel mutes
                        List<Mute> mutes = user.getMutes();
                    }
                });

                channelController.unmuteCurrentUser().enqueue(result -> Unit.INSTANCE);

                channelController.muteUser(userId);

                channelController.unmuteUser(userId);
            }

            static void queryMutedChannels() {
                // retrieve channels excluding muted ones
                int offset = 0;
                int limit = 10;
                int messageLimit = 0;
                QuerySort sort = new QuerySort();

                FilterObject mutedFiler = Filters.eq("muted", false);

                client.queryChannels(
                        new QueryChannelsRequest(mutedFiler, offset, limit, sort, messageLimit)
                ).enqueue(result -> Unit.INSTANCE);

                // retrieve muted channels

                FilterObject unmutedFilter = Filters.eq("muted", true);

                client.queryChannels(
                        new QueryChannelsRequest(unmutedFilter, offset, limit, sort, messageLimit)
                ).enqueue(result -> Unit.INSTANCE);
            }

            static void unmuteCurrentUser() {
                // unmute channel for current user
                channelController.unmuteCurrentUser().enqueue(result -> Unit.INSTANCE);
            }
        }

        static class UserPresence {
            /**
             * https://getstream.io/nessy/docs/chat_docs/user_presence/presence_format
             */
            static class UserPresenceFormat {
                static void invisible() {
                    User user = new User();
                    user.setId(userId);
                    user.setInvisible(false);
                    client.setUser(user, token, null);
                }

                static void listeningToPresenceChanges() {
                    // If you pass presence: true to channel.watch it will watch the list of user presence changes.
                    // Note that you can listen to at most 10 users using this API call

                    List<String> members = new ArrayList<>();
                    members.add("john");
                    members.add("jack");
                    Map<String, Object> data = new HashMap<>();
                    data.put("members", members);

                    WatchChannelRequest request = new WatchChannelRequest();
                    request.withData(data);

                    channelController.watch(request).enqueue(result -> Unit.INSTANCE);

                    // queryChannels allows you to listen to the members of the channels that are returned
                    // so this does the same thing as above and listens to online status changes for john and jack

                    WatchChannelRequest watchRequestWithPresence = new WatchChannelRequest();
                    watchRequestWithPresence.setPresence(true);
                    watchRequestWithPresence.withData(data);

                    channelController.watch(watchRequestWithPresence).enqueue(result -> Unit.INSTANCE);

                    // queryUsers allows you to listen to user presence changes for john and jack

                    int offset = 0;
                    int limit = 10;
                    boolean withPresence = true;
                    FilterObject filter = Filters.in("id", members);
                    QueryUsersRequest usersQuery = new QueryUsersRequest(filter, offset, limit, new QuerySort(), withPresence);

                    client.queryUsers(usersQuery).enqueue(result -> {
                        List<User> users = result.data();
                        return Unit.INSTANCE;
                    });
                }
            }

            static class Invisible {
                {
                    User user = new User();
                    user.setId(userId);
                    user.setInvisible(true);
                    client.setUser(user, token, null);
                }
            }

            static class ListeningToPresenceChanges {
                {
                    // If you pass presence: true to channel.watch it will watch the list of user presence changes.
                    // Note that you can listen to at most 10 users using this API call

                    List<String> members = new ArrayList<>();
                    members.add("john");
                    members.add("jack");
                    Map<String, Object> data = new HashMap<>();
                    data.put("members", members);

                    WatchChannelRequest request = new WatchChannelRequest();
                    request.withData(data);

                    channelController.watch(request).enqueue(result -> Unit.INSTANCE);

                    // queryChannels allows you to listen to the members of the channels that are returned
                    // so this does the same thing as above and listens to online status changes for john and jack

                    WatchChannelRequest watchRequestWithPresence = new WatchChannelRequest();
                    watchRequestWithPresence.setPresence(true);
                    watchRequestWithPresence.withData(data);

                    channelController.watch(watchRequestWithPresence).enqueue(result -> Unit.INSTANCE);

                    // queryUsers allows you to listen to user presence changes for john and jack

                    int offset = 0;
                    int limit = 10;
                    boolean withPresence = true;
                    FilterObject filter = Filters.in("id", members);
                    QueryUsersRequest usersQuery = new QueryUsersRequest(filter, offset, limit, new QuerySort(), withPresence);

                    client.queryUsers(usersQuery).enqueue(result -> {
                        List<User> users = result.data();
                        return Unit.INSTANCE;
                    });
                }
            }
        }

        static class TypingIndicators {
            static class TypingIndicatorsInternal {
                {
                    // sends a typing.start event at most once every two seconds
                    channelController.keystroke();
                    // sends the typing.stop event
                    channelController.stopTyping();
                }

                static void receivingTypingInvicatorEvents() {
                    // add typing start event handling
                    channelController.events().filter(new Function1<ChatEvent, Boolean>() {
                        @Override
                        public Boolean invoke(ChatEvent chatEvent) {
                            return chatEvent.getType().equals(EventType.INSTANCE.getTYPING_START());
                        }
                    }).subscribe(startedTyping -> Unit.INSTANCE);

                    // add typing top event handling
                    channelController.events().filter(new Function1<ChatEvent, Boolean>() {
                        @Override
                        public Boolean invoke(ChatEvent chatEvent) {
                            return chatEvent.getType().equals(EventType.INSTANCE.getTYPING_STOP());
                        }
                    }).subscribe(startedTyping -> Unit.INSTANCE);
                }
            }
        }

        static class UnreadCounts {

            /**
             * https://getstream.io/nessy/docs/chat_docs/unread_counts/unread
             */
            static class Unread {
                {
                    client.setUser(user, token, new InitConnectionListener() {
                        @Override
                        public void onSuccess(@NotNull ConnectionData data) {
                            User user = data.component1();
                            int unreadChannels = user.getUnreadChannels();
                            int totalUnreadCount = user.getTotalUnreadCount();
                        }
                    });
                }

                {
                    channelController.markRead().enqueue(result -> Unit.INSTANCE);
                }

                {

                }

                {
                    channelController
                            .events()
                            .filter(event -> event.getUnreadChannels() != null && event.getTotalUnreadCount() != null)
                            .subscribe(event -> {

                                int unreadChannels = event.getUnreadChannels();
                                int totalUnreadCount = event.getTotalUnreadCount();

                                return Unit.INSTANCE;
                            });
                }
            }

        }
    }
}
