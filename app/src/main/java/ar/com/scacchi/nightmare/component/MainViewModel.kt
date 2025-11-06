package ar.com.scacchi.nightmare.component

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.R
import ar.com.scacchi.nightmare.engine.Engine
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MainState(
            engine = Engine.createFrom(context, R.raw.doom),
            transformation = Transformation(
                pan = Offset(0f, 400f),
                zoom = 1f,
                rotation = 0f
            )
        )
    )
    val uiState: StateFlow<MainState> = _uiState

    fun updateTransformation(pan: Offset, zoom: Float, rotation: Float) {
        viewModelScope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    transformation = Transformation(
                        pan = Offset(
                            x = uiState.value.transformation.pan.x + pan.x,
                            y = uiState.value.transformation.pan.y + pan.y,
                        ),
                        zoom = uiState.value.transformation.zoom * zoom,
                        rotation = uiState.value.transformation.rotation + rotation
                    )
                )
            )
        }
    }
}
