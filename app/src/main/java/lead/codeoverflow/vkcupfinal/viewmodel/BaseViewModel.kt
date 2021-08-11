package lead.codeoverflow.vkcupfinal.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.KoinComponent

open class BaseViewModel : ViewModel(), KoinComponent {
    private val exHandler = CoroutineExceptionHandler { _, exception ->
    }

    val errorEx: MutableLiveData<String> = MutableLiveData()
    private val job = SupervisorJob()

    private val coroutineContext = Dispatchers.IO + exHandler + job
    protected val coroutineScope = CoroutineScope(coroutineContext)
}