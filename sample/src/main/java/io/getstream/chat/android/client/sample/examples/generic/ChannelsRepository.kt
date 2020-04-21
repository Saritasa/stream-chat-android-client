package io.getstream.chat.android.client.sample.examples.generic

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.*
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.api.models.QuerySort
import io.getstream.chat.android.client.call.Call
import io.getstream.chat.android.client.sample.ChannelsCache
import io.getstream.chat.android.client.sample.common.ApiMapper
import io.getstream.chat.android.client.sample.common.Channel
import io.getstream.chat.android.client.utils.FilterObject
import io.getstream.chat.android.client.utils.observable.ChatObservable
import io.getstream.chat.android.client.utils.observable.ChatObservableImpl

class ChannelsRepository(
    private val client: ChatClient,
    private val cache: ChannelsCache
) {

    fun getChannels(): ChatObservable<RepoResult<List<Channel>>> {

        val resource =
            object : NetworkResource<List<Channel>, List<io.getstream.chat.android.client.models.Channel>>() {
                override fun getRemote(): Call<List<io.getstream.chat.android.client.models.Channel>> {
                    val filter = FilterObject("type", "messaging")
                    val sort = QuerySort()
                    sort.asc("created_at")
                    return client.queryChannels(QueryChannelsRequest(filter, 0, 10, sort))
                }

                override fun getLocal(): LiveData<List<Channel>> {
                    return cache.dao.getPageLive(0, 10)
                }

                override fun requiresRemoteFetch(): Boolean {
                    return true
                }

                override fun storeRemote(
                    data: List<io.getstream.chat.android.client.models.Channel>,
                    onComplete: (Boolean) -> Unit
                ) {
                    Thread {
                        cache.dao.upsert(ApiMapper.mapChannels(data), onComplete)
                    }.start()
                }

                override fun toObservable(): ChatObservable<RepoResult<List<Channel>>> {

                    var firstPassed = false
                    val result = ChatObservableImpl<RepoResult<List<Channel>>>()
                    val local = getLocal()
                    var firstSub = false
                    val lso = LSO()

                    val obs = Observer<List<Channel>> { data ->
                        if (!firstPassed) {
                            firstPassed = true

                            if (data.isEmpty()) {
                                result.onNext(RepoResult.EmptyCache)
                            } else {
                                result.onNext(RepoResult.StaleCache(data))
                            }

                            result.onNext(RepoResult.NetworkLoading)

                            getRemote().enqueue { response ->
                                if (response.isSuccess) {
                                    storeRemote(response.data()) { changed ->
                                        if (!changed) {
                                            Handler(Looper.getMainLooper()).post {
                                                result.onNext(RepoResult.NoNetworkUpdate)
                                            }
                                        }
                                    }
                                } else {
                                    result.onNext(RepoResult.NetworkLoadingError(response.error()))
                                }
                            }

                        } else {
                            result.onNext(RepoResult.FreshCache(data))
                        }
                    }

                    result.cache()

                    result.onNext(RepoResult.CacheLoading)

                    result.onUnSubscribe {
                        local.removeObserver(obs)
                    }

                    result.onSubscribe {
                        if (!firstSub) {
                            firstSub = true
                            local.observe(lso, obs)
                        }
                    }

                    return result
                }

            }
        return resource.toObservable()
    }

    class LSO : LifecycleOwner {

        val reg = LifecycleRegistry(this)

        init {
            reg.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }

        override fun getLifecycle(): Lifecycle {

            return reg
        }
    }

    abstract class NetworkResource<LocalData, RemoteData> {
        abstract fun getRemote(): Call<RemoteData>
        abstract fun getLocal(): LiveData<LocalData>
        abstract fun requiresRemoteFetch(): Boolean
        abstract fun storeRemote(data: RemoteData, onComplete: (Boolean) -> Unit)
        abstract fun toObservable(): ChatObservable<RepoResult<LocalData>>
    }
}