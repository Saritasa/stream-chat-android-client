package io.getstream.chat.android.client.notifications.options

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.google.firebase.messaging.RemoteMessage
import io.getstream.chat.android.client.R
import io.getstream.chat.android.client.events.ChatEvent
import io.getstream.chat.android.client.notifications.DeviceRegisteredListener
import io.getstream.chat.android.client.notifications.FirebaseMessageParser
import io.getstream.chat.android.client.notifications.FirebaseMessageParserImpl
import io.getstream.chat.android.client.notifications.NotificationLoadDataListener
import io.getstream.chat.android.client.receivers.NotificationMessageReceiver


open class ChatNotificationConfig(val context: Context) {

    open fun onChatEvent(event: ChatEvent): Boolean {
        return false
    }

    open fun onFirebaseMessage(message: RemoteMessage): Boolean {
        return false
    }

    open fun getDeviceRegisteredListener(): DeviceRegisteredListener? {
        return null
    }

    open fun getDataLoadListener(): NotificationLoadDataListener? {
        return null
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    open fun createNotificationChannel(): NotificationChannel {

        return NotificationChannel(
            getNotificationChannelId(),
            getNotificationChannelName(),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            setShowBadge(true)
            importance = NotificationManager.IMPORTANCE_HIGH
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
            vibrationPattern = longArrayOf(
                100,
                200,
                300,
                400,
                500,
                400,
                300,
                200,
                400
            )

        }
    }

    open fun getNotificationChannelId() =
        context.getString(R.string.stream_chat_notification_channel_id)

    open fun getNotificationChannelName() =
        context.getString(R.string.stream_chat_notification_channel_name)

    open fun getSmallIcon(): Int {
        return R.drawable.stream_ic_notification
    }

    open fun getFirebaseMessageIdKey(): String {
        return "stream-chat-message-id"
    }

    open fun getFirebaseChannelIdKey(): String {
        return "stream-chat-channel-id"
    }

    open fun getFirebaseChannelTypeKey(): String {
        return "stream-chat-channel-type"
    }

    open fun getErrorCaseNotificationTitle(): String {
        return context.getString(R.string.stream_chat_notification_title)
    }

    open fun getErrorCaseNotificationContent(): String {
        return context.getString(R.string.stream_chat_notification_content)
    }

    open fun buildErrorCaseNotification(): Notification {

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = getNotificationBuilder()
        val intent = PendingIntent.getActivity(
            context,
            getRequestCode(),
            getErrorCaseIntent(),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        return notificationBuilder.setContentTitle(getErrorCaseNotificationTitle())
            .setContentText(getErrorCaseNotificationContent())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setShowWhen(true)
            .setContentIntent(intent)
            .setSound(defaultSoundUri)
            .build()
    }

    open fun buildNotification(
        notificationId: Int,
        channelName: String,
        messageText: String,
        messageId: String,
        channelType: String,
        channelId: String
    ): Notification {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = getNotificationBuilder()

        val intent = PendingIntent.getActivity(
            context,
            getRequestCode(),
            getNewMessageIntent(messageId, channelType, channelId),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationBuilder.setContentTitle(channelName)
            .setContentText(messageText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setShowWhen(true)
            .setContentIntent(intent)
            .setSound(defaultSoundUri)

        notificationBuilder.apply {
            addAction(
                getReadAction(
                    preparePendingIntent(
                        notificationId,
                        messageId,
                        channelId,
                        channelType,
                        NotificationMessageReceiver.ACTION_READ
                    )
                )
            )
            addAction(
                getReplyAction(
                    preparePendingIntent(
                        notificationId,
                        messageId,
                        channelId,
                        channelType,
                        NotificationMessageReceiver.ACTION_REPLY
                    )
                )
            )
        }

        return notificationBuilder.build()
    }

    private fun getRequestCode(): Int {
        return 1220999987
    }

    open fun getNewMessageIntent(
        messageId: String,
        channelType: String,
        channelId: String
    ): Intent {
        return context.packageManager!!.getLaunchIntentForPackage(context.packageName)!!
    }

    open fun getErrorCaseIntent(): Intent {
        return context.packageManager!!.getLaunchIntentForPackage(context.packageName)!!
    }

    open fun getFirebaseMessageParser(): FirebaseMessageParser {
        return FirebaseMessageParserImpl(this)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap =
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, getNotificationChannelId())
            .setAutoCancel(true)
            .setSmallIcon(getSmallIcon())
            .setDefaults(NotificationCompat.DEFAULT_ALL)
    }

    private fun getReadAction(pendingIntent: PendingIntent): NotificationCompat.Action {
        return NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_view,
            context.getString(R.string.stream_chat_notification_read),
            pendingIntent
        ).build()
    }

    private fun getReplyAction(replyPendingIntent: PendingIntent): NotificationCompat.Action {
        val remoteInput =
            RemoteInput.Builder(NotificationMessageReceiver.KEY_TEXT_REPLY)
                .setLabel(context.getString(R.string.stream_chat_notification_type_hint))
                .build()
        return NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_send,
            context.getString(R.string.stream_chat_notification_reply),
            replyPendingIntent
        )
            .addRemoteInput(remoteInput)
            .setAllowGeneratedReplies(true)
            .build()
    }

    private fun preparePendingIntent(
        notificationId: Int,
        messageId: String,
        channelId: String,
        type: String,
        actionType: String
    ): PendingIntent {
        val notifyIntent = Intent(context, NotificationMessageReceiver::class.java)

        notifyIntent.apply {
            putExtra(NotificationMessageReceiver.KEY_NOTIFICATION_ID, notificationId)
            putExtra(NotificationMessageReceiver.KEY_MESSAGE_ID, messageId)
            putExtra(NotificationMessageReceiver.KEY_CHANNEL_ID, channelId)
            putExtra(NotificationMessageReceiver.KEY_CHANNEL_TYPE, type)
            action = actionType
        }

        return PendingIntent.getBroadcast(
            context,
            0,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


}