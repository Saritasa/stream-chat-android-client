package io.getstream.chat.android.client.sample.examples.generic

import io.getstream.chat.android.client.errors.ChatError

sealed class RepoResult<out T> {
    object Loading : RepoResult<Nothing>()
    data class Success<T>(val data: T) : RepoResult<T>()
    data class Error(val error: ChatError) : RepoResult<Nothing>()
}