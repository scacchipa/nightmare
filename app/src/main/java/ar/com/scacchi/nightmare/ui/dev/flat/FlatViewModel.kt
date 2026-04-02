package ar.com.scacchi.nightmare.ui.dev.flat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.data.color.Palette
import ar.com.scacchi.nightmare.di.DefaultDispatcher
import ar.com.scacchi.nightmare.source.wad.WadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlatViewModel @Inject constructor(
    val wadManager: WadManager,
    @param:DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        FlatState(Palette(emptyArray()), 0, listOf(), DoomBitmap(100, 100))
    )
    val uiState = _uiState as StateFlow<FlatState>

    fun updateBitmap() {
        viewModelScope.launch(defaultDispatcher) {
            val palette = wadManager.playPal[0]
            val fName = wadManager.readFlatNameList()[0]
            val flat = wadManager.getFlat(fName)
            _uiState.emit(
                FlatState(
                    palette = palette,
                    spinnerPosition = 0,
                    flatNameList = wadManager.readFlatNameList() ,
                    doomBitmap = flat.buildDoomImage(),
                )
            )
            println(wadManager.readFlatNameList())
        }
    }

    fun updateSpinnerPosition(position: Int) {
        viewModelScope.launch {
            val palette = wadManager.playPal[0]
            val pName = wadManager.readFlatNameList()[position]
            val patch = wadManager.getFlat(pName)
            _uiState.emit(
                FlatState(
                    palette = palette,
                    spinnerPosition = position,
                    flatNameList = wadManager.readFlatNameList(),
                    doomBitmap = patch.buildDoomImage()
                )
            )
        }
    }
}