package ar.com.scacchi.nightmare.component

import androidx.compose.ui.geometry.Offset

data class Transformation(
    val pan: Offset,
    val zoom: Float,
    val rotation: Float,
)
