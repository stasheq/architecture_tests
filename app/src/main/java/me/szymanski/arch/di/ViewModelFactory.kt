package me.szymanski.arch.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.szymanski.glue.Case
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory<VM : ViewModel> @Inject constructor(
    private val viewModelProvider: Provider<VM>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = viewModelProvider.get() as T
}

class GenericViewModel<C : Case> @Inject constructor(val case: C) : ViewModel() {
    init {
        case.create()
    }

    override fun onCleared() = case.destroy()
}

class CaseReference<C : Case> @Inject constructor(
    val caseFactory: ViewModelFactory<GenericViewModel<C>>,
    val activity: FragmentActivity
) {
    val case: C by lazy { lazyCreateViewModel { activity }.value.case }

    @Suppress("UNCHECKED_CAST")
    private fun lazyCreateViewModel(getActivity: () -> FragmentActivity) = lazy {
        // TODO use key for multiple instances of same fragment
        ViewModelProvider(getActivity(), caseFactory).get(GenericViewModel::class.java) as GenericViewModel<C>
    }
}
