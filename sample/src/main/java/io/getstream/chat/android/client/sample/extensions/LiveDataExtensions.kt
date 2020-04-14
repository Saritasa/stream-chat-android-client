package io.getstream.chat.android.client.sample.extensions

import androidx.lifecycle.LiveData
import io.getstream.chat.android.client.sample.examples.generic.RepoData

internal fun <T> LiveData<T>.toChat(): RepoData<T> {
    return RepoData(this)
}