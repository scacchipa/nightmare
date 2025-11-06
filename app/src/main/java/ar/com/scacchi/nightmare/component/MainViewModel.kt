package ar.com.scacchi.nightmare.component

import androidx.lifecycle.ViewModel
import ar.com.scacchi.nightmare.engine.WadData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MainViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow<WadData?>(null)
    val uiState: StateFlow<WadData?> = _uiState
}