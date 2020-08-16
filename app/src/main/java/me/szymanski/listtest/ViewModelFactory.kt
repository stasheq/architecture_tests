package me.szymanski.listtest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.szymanski.logic.Case
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory<VM : ViewModel> @Inject constructor(
    private val viewModel: Provider<VM>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = viewModel.get() as T
}

class GenericViewModel<C : Case> @Inject constructor(val case: C) : ViewModel() {
    fun start() = case.start()
    override fun onCleared() = case.destroy()
}
