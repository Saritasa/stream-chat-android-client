package androidx.lifecycle

/**
 * Used to override protected [postValue] and [setValue]
 */
internal class LiveDataWrapper<T>(
    ld: LiveData<T>,
    onActiveHandler: () -> Unit,
    onInactiveHandler: () -> Unit,
    setHandler: (T) -> Unit
) {

    private val ld = LD(ld, onActiveHandler, onInactiveHandler)

    fun getValue(): T? {
        return ld.value
    }

    fun setValue(value: T) {
        ld.publicSetValue(value)
    }

    fun postValue(value: T) {
        ld.publicPostValue(value)
    }

    fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        ld.observe(owner, observer)
    }
}

/**
 * Used to override active and inactive handlers
 */
internal class LD<T>(
    val ld: LiveData<T>,
    val onActiveHandler: () -> Unit,
    val onInactiveHandler: () -> Unit
) : LiveData<T>() {

    fun publicSetValue(value: T) {
        ld.value = value
    }

    fun publicPostValue(value: T) {
        ld.postValue(value)
    }

    override fun onInactive() {
        onInactiveHandler()
    }

    override fun onActive() {
        onActiveHandler()
    }

    override fun setValue(value: T) {
        ld.value = value
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        ld.observe(owner, observer)
    }

    override fun hasObservers(): Boolean {
        return ld.hasObservers()
    }

    override fun removeObservers(owner: LifecycleOwner) {
        ld.removeObservers(owner)
    }

    override fun getVersion(): Int {
        return ld.version
    }

    override fun observeForever(observer: Observer<in T>) {
        ld.observeForever(observer)
    }

    override fun removeObserver(observer: Observer<in T>) {
        ld.removeObserver(observer)
    }

    override fun getValue(): T? {
        return ld.value
    }

    override fun hasActiveObservers(): Boolean {
        return ld.hasActiveObservers()
    }

    override fun postValue(value: T) {
        ld.postValue(value)
    }
}