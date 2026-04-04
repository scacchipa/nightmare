package ar.com.scacchi.nightmare.ui.dev.texture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.data.color.Palette
import ar.com.scacchi.nightmare.di.DefaultDispatcher
import ar.com.scacchi.nightmare.engine.ImageRepository
import ar.com.scacchi.nightmare.source.wad.WadManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TextureViewModel @Inject constructor(
    private val wadManager: WadManager,
    private val imageRepository: ImageRepository,
    @param:DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TextureState(Palette(emptyArray()), 0, 0, emptyList(), DoomBitmap(100, 100))
    )
    val uiState = _uiState as StateFlow<TextureState>

    fun updateBitmap() {
        viewModelScope.launch(defaultDispatcher) {
            val playPal = wadManager.playPal
            if (playPal.paletteCount == 0) return@launch
            val palette = playPal[0]
            val textureMap = imageRepository.getTextureDoomBitmap(0)
            _uiState.emit(
                TextureState(
                    palette = palette,
                    spinnerPosition = _uiState.value.spinnerPosition,
                    itemCount = wadManager.textureMapContainer.size,
                    spinnerValues = (0 until wadManager.textureMapContainer.size).map {
                        "$it - ${wadManager.textureMapContainer[it].name}"
                    },
                    doomBitmap = textureMap
                )
            )

            println(wadManager.readSpriteNameList())
        }
    }

    fun updateSpinnerPosition(position: Int) {
        viewModelScope.launch {
            val palette = wadManager.playPal[0]
            val textureMap = wadManager.getTextureMap(position)
            _uiState.emit(
                TextureState(
                    palette = palette,
                    spinnerPosition = position,
                    itemCount = wadManager.textureMapContainer.size,
                    spinnerValues = _uiState.value.spinnerValues,
                    doomBitmap = textureMap.buildDoomImage(imageRepository, wadManager)
                )
            )
        }
    }
}