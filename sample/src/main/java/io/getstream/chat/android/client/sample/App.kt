package io.getstream.chat.android.client.sample

import android.app.Application
import android.content.Intent
import com.facebook.stetho.Stetho
import com.google.firebase.FirebaseApp
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.events.ErrorEvent
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.logger.ChatLoggerHandler
import io.getstream.chat.android.client.notifications.options.ChatNotificationConfig
import io.getstream.chat.android.client.sample.cache.AppDatabase
import io.getstream.chat.android.client.sample.common.HomeActivity
import io.getstream.chat.android.client.sample.common.KeyValue
import io.getstream.chat.android.client.sample.examples.generic.ChannelsRepository
import io.getstream.chat.android.client.sample.repositories.ChannelsRepositoryRx
import io.getstream.chat.android.client.sample.repositories.ChannelsRepositorySync
import kotlin.time.ExperimentalTime

class App : Application() {

    companion object {
        lateinit var client: ChatClient
        lateinit var channelsRepositorySync: ChannelsRepositorySync
        lateinit var channelsRepositoryRx: ChannelsRepositoryRx
        lateinit var channelsRepositoryLive: ChannelsRepository
        lateinit var db: AppDatabase
        lateinit var cache: ChannelsCache
        lateinit var keyValue: KeyValue

        val CHANNEL_TYPE_KEY = "type"
        val CHANNEL_ID_KEY = "id"

        private const val EXTRA_CHANNEL_TYPE = "io.getstream.chat.example.CHANNEL_TYPE"
        private const val EXTRA_CHANNEL_ID = "io.getstream.chat.example.CHANNEL_ID"
        private const val EXTRA_MESSAGE_ID = "io.getstream.chat.example.MESSAGE_ID"
    }

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        FirebaseApp.initializeApp(this)

        db = AppDatabase.getInstance(this)

        val apiKey = "qk4nn7rpcn75"
        val token =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoiYmVuZGVyIn0.3KYJIoYvSPgTURznP8nWvsA2Yj2-vLqrm-ubqAeOlcQ"

        client = ChatClient.Builder(apiKey, this)
            .notifications(provideNotificationConfig())
            .loggerHandler(object : ChatLoggerHandler {
                override fun logT(throwable: Throwable) {

                }

                override fun logT(tag: Any, throwable: Throwable) {

                }

                override fun logI(tag: Any, message: String) {

                }

                override fun logD(tag: Any, message: String) {

                }

                override fun logW(tag: Any, message: String) {

                }

                override fun logE(tag: Any, message: String) {

                }

                override fun logE(tag: Any, message: String, throwable: Throwable) {

                }

            })
            .logLevel(if (BuildConfig.DEBUG) ChatLogLevel.ALL else ChatLogLevel.NOTHING)
            .build()

        client.events()
            .filter(ErrorEvent::class.java)
            .subscribe {
                println(it)
            }

        client.events()
            .filter {
                it.cid != null && it.cid == "*"
            }
            .subscribe {
                println(it)
            }

        keyValue = KeyValue(this)
        cache = ChannelsCache(db.channels())
        channelsRepositorySync = ChannelsRepositorySync(client, cache)
        channelsRepositoryRx = ChannelsRepositoryRx(client, cache)
        channelsRepositoryLive =
            ChannelsRepository(
                client,
                cache
            )
    }


    @UseExperimental(ExperimentalTime::class)
    private fun provideNotificationConfig() = object : ChatNotificationConfig(this) {

        override fun getNewMessageIntent(messageId: String, channelType: String, channelId: String): Intent {

            val intent = Intent(context, HomeActivity::class.java)
            intent.apply {
                putExtra(EXTRA_CHANNEL_TYPE, channelType)
                putExtra(EXTRA_CHANNEL_ID, channelId)
                putExtra(EXTRA_MESSAGE_ID, messageId)
            }

            return intent
        }

    }
}