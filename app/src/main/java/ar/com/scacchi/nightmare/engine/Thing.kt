package ar.com.scacchi.nightmare.engine

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.ext.fromAngle
import ar.com.scacchi.nightmare.settings.PLAYER_HEIGHT
import ar.com.scacchi.nightmare.source.wad.lump.data.map.ThingLump
import kotlin.math.PI

class Thing(
    val pos: Offset,
    val angle: Float,
    val type: UShort,
    val flags: UShort,
) {
    fun toPlayer(): Player = Player(
        pos = pos,
        type = type,
        flags = flags,
        height = PLAYER_HEIGHT,
        dirVector = Offset.fromAngle(angle)
    )
}

fun ThingLump.toThing(): Thing = Thing(
    pos = Offset(x = xPos.toFloat(), y = yPos.toFloat()),
    angle = (angle.toFloat() * PI / 180.0f).toFloat(),
    type = type,
    flags = flags,
)