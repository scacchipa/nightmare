package ar.com.scacchi.nightmare.ui.dev.patch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.data.WadManager
import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.data.color.Palette
import ar.com.scacchi.nightmare.di.DefaultDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatchViewModel @Inject constructor(
    val wadManager: WadManager,
    @param:DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PatchState(Palette(emptyArray()), 0, listOf(), DoomBitmap(100, 100))
    )
    val uiState = _uiState as StateFlow<PatchState>

    fun updateBitmap() {
        viewModelScope.launch(defaultDispatcher) {
            val palette = wadManager.playPal[0]
            val pName = wadManager.getPatchNameList()[0]
            val patch = wadManager.getPatch(pName)
            _uiState.emit(
                PatchState(
                    palette = palette,
                    spinnerPosition = 0,
                    patchNameList = wadManager.getPatchNameList(),
                    doomBitmap = patch.buildDoomImage()
                )
            )

            println(wadManager.getPatchNameList())
        }
    }

    fun updateSpinnerPosition(position: Int) {
        viewModelScope.launch {
            val palette = wadManager.playPal[0]
            val pName = wadManager.getPatchNameList()[position]
            val patch = wadManager.getPatch(pName)
            _uiState.emit(
                PatchState(
                    palette = palette,
                    spinnerPosition = position,
                    patchNameList = wadManager.getPatchNameList(),
                    doomBitmap = patch.buildDoomImage()
                )
            )
        }
    }
}