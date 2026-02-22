package ar.com.scacchi.nightmare.ui.dev.colormap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.data.WadManager
import ar.com.scacchi.nightmare.data.color.ColorMap
import ar.com.scacchi.nightmare.data.color.PlayPal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ColorMapViewModel @Inject constructor(
    val wadManager: WadManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ColorMapState(PlayPal(emptyArray()), ColorMap(emptyArray()), 0))
    val uiState = _uiState as StateFlow<ColorMapState>

    fun setPaletteSelected(id: Int) {
        viewModelScope.launch {
            _uiState.emit(
                ColorMapState(
                    playPal = wadManager.playPal,
                    colorMap = wadManager.colorMap,
                    paletteIdx = id
                )
            )
        }
    }
}