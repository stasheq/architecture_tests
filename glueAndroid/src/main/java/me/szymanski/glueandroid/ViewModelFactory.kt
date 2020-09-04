package me.szymanski.glueandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.szymanski.gluekotlin.Case
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory<VM : ViewModel> @Inject constructor(
    private val viewModel: Provider<VM>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = viewModel.get() as T
}

class GenericViewModel<C : Case> @Inject constructor(val case: C) : ViewModel() {
    init {
        case.create()
    }

    override fun onCleared() = case.destroy()
}
