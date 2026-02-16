package ar.com.scacchi.nightmare.ui.dev.patch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.data.asset.DoomImage
import ar.com.scacchi.nightmare.di.DefaultDispatcher
import ar.com.scacchi.nightmare.engine.Engine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatchViewModel @Inject constructor(
    val engine: Engine,
    @param:DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PatchState(doomImage = DoomImage(100, 100))
    )
    val uiState = _uiState as StateFlow<PatchState>

    fun updateBitmap(pName: String = "WIF") {
        viewModelScope.launch(defaultDispatcher) {
            val palette = engine.gameStateFlow.value.playPal[0]
            val patch = engine.getPatch(pName)

            _uiState.emit(
                PatchState(patch.buildDoomImage(palette))
            )
        }
    }
}