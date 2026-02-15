package ar.com.scacchi.nightmare.ui.dev.patch

import androidx.compose.foundation.Image
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.data.asset.DoomImage
import ar.com.scacchi.nightmare.data.asset.Patch
import ar.com.scacchi.nightmare.di.DefaultDispatcher
import ar.com.scacchi.nightmare.engine.Engine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun PatchWidget(
    viewModel: PatchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Button(
        onClick = { viewModel.updateBitmap()  }
    ) {
        Text(text = "Update Bitmap")
    }

    Image(
        bitmap = state.doomImage.toBitmap().asImageBitmap(),
        contentDescription = null,
    )

}

@HiltViewModel
class PatchViewModel @Inject constructor(
    val engine: Engine,
    @param:DefaultDispatcher val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PatchModel(doomImage = DoomImage(100, 100))
    )
    val uiState = _uiState as StateFlow<PatchModel>

    fun updateBitmap(pName: String = "WIF") {
        viewModelScope.launch(defaultDispatcher) {
            val lump = engine.gameStateFlow.value.lumpDirectory[pName]

            val buffer = engine.gameStateFlow.value.buffer
            val palette = engine.gameStateFlow.value.playPal[0]
            val patch = Patch.createFrom(buffer, lump, palette)

            _uiState.emit(
                PatchModel(doomImage = patch.image)
            )
        }
    }
}

data class PatchModel(
    val doomImage: DoomImage
)