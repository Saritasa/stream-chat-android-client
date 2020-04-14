package io.getstream.chat.android.client.sample.common

object ApiMapper {

    fun mapChannel(channel: io.getstream.chat.android.client.models.Channel): Channel {
        return Channel().apply {
            remoteId = channel.id
            name = "name: " + channel.id
            if (channel.updatedAt != null) updatedAt = channel.updatedAt!!.time.toInt()
        }
    }

    fun mapChannels(channels: List<io.getstream.chat.android.client.models.Channel>): List<Channel> {
        return channels.map {
            mapChannel(it)
        }
    }
}