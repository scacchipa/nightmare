package ar.com.scacchi.nightmare.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin

data class Transformation(
    val offset: Offset,
    val zoom: Float,
    val rotation: Float,
    val transformOrigin: TransformOrigin,
)