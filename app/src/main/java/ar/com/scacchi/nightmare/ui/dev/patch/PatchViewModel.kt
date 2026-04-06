package ar.com.scacchi.nightmare.ui.dev.patch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.di.DefaultDispatcher
import ar.com.scacchi.nightmare.engine.ImageRepository
import ar.com.scacchi.nightmare.source.wad.NmPalette
import ar.com.scacchi.nightmare.source.wad.WadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatchViewModel @Inject constructor(
    private val wadManager: WadManager,
    private val imageRepository: ImageRepository,
    @param:DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PatchState(NmPalette(emptyArray()), 0, listOf(), DoomBitmap(100, 100))
    )
    val uiState = _uiState as StateFlow<PatchState>

    fun updateBitmap() {
        viewModelScope.launch(defaultDispatcher) {
            val palette = wadManager.playPal[0]
            val pName = wadManager.readPatchNameList()[0]
            val patch = imageRepository.getPatchDoomBitmap(pName)
            _uiState.emit(
                PatchState(
                    palette = palette,
                    spinnerPosition = 0,
                    patchNameList = wadManager.readPatchNameList(),
                    doomBitmap = patch
                )
            )

            println(wadManager.readPatchNameList())
        }
    }

    fun updateSpinnerPosition(position: Int) {
        viewModelScope.launch {
            val palette = wadManager.playPal[0]
            val pName = wadManager.readPatchNameList()[position]
            val patch = wadManager.getPatch(pName)
            _uiState.emit(
                PatchState(
                    palette = palette,
                    spinnerPosition = position,
                    patchNameList = wadManager.readPatchNameList(),
                    doomBitmap = patch.buildDoomImage()
                )
            )
        }
    }
}