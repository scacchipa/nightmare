package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.settings.PLAYER_HEIGHT
import ar.com.scacchi.nightmare.source.wad.lump.ThingLump
import kotlin.math.PI

class Thing(
    val xPos: Int,
    val yPos: Int,
    val angle: Float,
    val type: UShort,
    val flags: UShort,
) {
    fun toPlayer(): Player = Player(
        xPos = xPos.toFloat(),
        yPos = yPos.toFloat(),
        angle = angle,
        type = type,
        flags = flags,
        height = PLAYER_HEIGHT,
    )
}

fun ThingLump.toThing(): Thing = Thing(
    xPos = xPos.toInt(),
    yPos = yPos.toInt(),
    angle = (angle.toFloat() * PI / 180.0f).toFloat(),
    type = type,
    flags = flags,
)