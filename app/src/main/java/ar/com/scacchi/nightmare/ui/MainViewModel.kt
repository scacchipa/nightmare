package ar.com.scacchi.nightmare.ui

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.com.scacchi.nightmare.R
import ar.com.scacchi.nightmare.engine.Engine
import ar.com.scacchi.nightmare.ext.rotateBy
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
                offset = Offset(0f, 1300f),
                zoom = 0.4f,
                rotation = 0f,
                transformOrigin = TransformOrigin(0f, 0f)
            )
        )
    )
    val uiState: StateFlow<MainState> = _uiState

    fun updateTransformation(
        centroid: Offset,
        pan: Offset,
        gestureZoom: Float,
        gestureRotate: Float,
        transformationOrigin: TransformOrigin) {
        viewModelScope.launch {

            val oldScale = uiState.value.transformation.zoom
            val oldAngle = uiState.value.transformation.rotation
            val oldOffset = uiState.value.transformation.offset

            val newScale = oldScale * gestureZoom
            val newAngle = oldAngle + gestureRotate
            val newOffset =
                (oldOffset + pan - centroid * oldScale).rotateBy(-oldAngle).rotateBy(newAngle) +
                        centroid * newScale
            _uiState.emit(
                _uiState.value.copy(
                    transformation = Transformation(
                        offset = newOffset,
                        zoom = newScale,
                        rotation = newAngle,
                        transformOrigin = transformationOrigin,
                    )
                )
            )
        }
    }
}
