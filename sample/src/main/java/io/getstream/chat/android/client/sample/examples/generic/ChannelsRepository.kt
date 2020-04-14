package io.getstream.chat.android.client.sample.examples.generic

import androidx.lifecycle.*
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
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

//        cache.dao.getPageLive(0, 10).observe({
//            object : Lifecycle() {
//                override fun addObserver(observer: LifecycleObserver) {
//
//                }
//
//                override fun removeObserver(observer: LifecycleObserver) {
//
//                }
//
//                override fun getCurrentState(): State {
//
//                }
//
//            }
//        }, {
//
//        })

        val resource =
            object : NetworkResource<List<Channel>, List<io.getstream.chat.android.client.models.Channel>>() {
                override fun getRemote(): Call<List<io.getstream.chat.android.client.models.Channel>> {
                    return client.queryChannels(QueryChannelsRequest(FilterObject("type", "messaging"), 0, 10))
                }

                override fun getLocal(): LiveData<List<Channel>> {
                    return cache.dao.getPageLive(0, 10)
                }

                override fun requiresRemoteFetch(): Boolean {
                    return true
                }

                override fun storeRemote(data: List<io.getstream.chat.android.client.models.Channel>) {
                    Thread {
                        cache.dao.upsert(ApiMapper.mapChannels(data))
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
                            result.onNext(RepoResult.Success(data))

                            getRemote().enqueue { response ->
                                if (response.isSuccess) {
                                    storeRemote(response.data())
                                } else {
                                    result.onNext(RepoResult.Error(response.error()))
                                }
                            }

                        } else {
                            result.onNext(RepoResult.Success(data))
                        }
                    }

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
        abstract fun storeRemote(data: RemoteData)
        abstract fun toObservable(): ChatObservable<RepoResult<LocalData>>
    }
}