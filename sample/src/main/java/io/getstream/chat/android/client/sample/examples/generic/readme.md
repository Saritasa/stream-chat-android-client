# Highlights
- no android data binding
- SDK api doesn't have third party dependencies like `LideData`, so no version conflicts and easier internal dependencies updates
- one line initialisation of ViewModels or completely custom implementation
- automatic unsubscription when Fragment/Activity is destroyed
- viewmodels are disconnected from views
- reactive api

# Creating channels list fragment
Create layout from SDK view components
```xml
<FrameLayout>
    <ChatChannelsList 
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
</FrameLayout>
```
Add fragment or activity. View will be automatically bound with viewmodel
```kotlin
class ChannelsFragment: Fragment {

    fun onCreateView(){
        LayoutInflater.inflate(R.layout.fragment_channels)
    }

    fun onViewCreated() {
        val chat = Chat.instance
        chat.viewModels.bindChannelsList(
            this,
            User("bender"),
            "token",
            {channelId -> navigator.goToChannel(channelId)}
        )
    }
}
```
# Creating channels list fragment with custom views 
```kotlin
class ChannelsFragment: Fragment {

    fun onViewCreated() {
        val chat = Chat.instance
        val viewModel = chat.viewModels.createChannelsList(
            this,
            User("bender"),
            "token",
            {channelId -> navigator.goToChannel(channelId)}
        )

        viewModel.channels().subscribe { result ->
            when (result) {
                is RepoResult.Success -> {
                    findViewById(R.id.channels_list).setData(result.data)
                }
                is RepoResult.Loading -> {
                    findViewById(R.id.progress_view).show()
                }
                is RepoResult.Error -> {
                    findViewById(R.id.error_view).show()
                }
            }
        }
    }
}
```
# Creating channel fragment
Create layout from SDK view components
```xml
<LinearLayout>
    <ChatMessagesList 
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>

    <ChatInputView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>

</LinearLayout>
```
Add fragment or activity
```kotlin
class ChannelFragment: Fragment {

    fun onCreateView(){
        LayoutInflater.inflate(R.layout.fragment_channel)
    }

    fun onViewCreated() {
        val chat = Chat.instance
        chat.viewModels.bindChannel(
            "channel-id"
            this,
            User("bender"),
            "token"
        )
    }
}
```