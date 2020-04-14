package io.getstream.chat.android.client.sample.examples.generic.binders

import androidx.lifecycle.Lifecycle

interface ViewModelBinder<View, ViewModel> {
    fun bind(lifecycle: Lifecycle, view: View, viewModel: ViewModel)
}