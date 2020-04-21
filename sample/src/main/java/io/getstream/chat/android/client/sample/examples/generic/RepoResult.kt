package io.getstream.chat.android.client.sample.examples.generic

import io.getstream.chat.android.client.errors.ChatError

sealed class RepoResult<out T> {

    object Idle : RepoResult<Nothing>()

    // Loading
    object CacheLoading : RepoResult<Nothing>()
    object NetworkLoading : RepoResult<Nothing>()

    // Results
    object EmptyCache : RepoResult<Nothing>()
    object NoNetworkUpdate : RepoResult<Nothing>()
    data class StaleCache<T>(val data: T) : RepoResult<T>()
    data class FreshCache<T>(val data: T) : RepoResult<T>()

    // Errors
    data class CacheLoadingError(val error: ChatError) : RepoResult<Nothing>()
    data class NetworkLoadingError(val error: ChatError) : RepoResult<Nothing>()
}