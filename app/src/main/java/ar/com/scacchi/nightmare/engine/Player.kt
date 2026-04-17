package ar.com.scacchi.nightmare.engine

import androidx.compose.ui.geometry.Offset
import ar.com.scacchi.nightmare.ext.Vector0Degree
import ar.com.scacchi.nightmare.ext.Vector180Degree
import ar.com.scacchi.nightmare.ext.Vector225Degree
import ar.com.scacchi.nightmare.ext.Vector270Degree
import ar.com.scacchi.nightmare.ext.Vector315Degree
import ar.com.scacchi.nightmare.ext.Vector45Degree
import ar.com.scacchi.nightmare.ext.Vector90Degree
import ar.com.scacchi.nightmare.ext.rotateBy
import ar.com.scacchi.nightmare.ext.rotatedBy
import ar.com.scacchi.nightmare.ext.versorWithAngle
import ar.com.scacchi.nightmare.settings.PLAYER_HEIGHT
import ar.com.scacchi.nightmare.settings.PLAYER_ROT_SPEED
import ar.com.scacchi.nightmare.settings.PLAYER_SPEED
import ar.com.scacchi.nightmare.source.wad.lump.data.map.ThingLump
import kotlin.math.PI

data class Player(
    val pos: Offset,
    val type: UShort,
    val flags: UShort,
    val height: Float,
    val dirVersor: Offset,
) {
    constructor(thing: ThingLump) : this(
        pos = Offset(x = thing.xPos.toFloat(), y = thing.yPos.toFloat()),
        type = thing.type,
        flags = thing.flags,
        height = PLAYER_HEIGHT,
        dirVersor = Offset.versorWithAngle((thing.angle.toFloat() * PI / 180.0f).toFloat())
    )

    fun advance(): Player = movePlayer(Vector0Degree, PLAYER_SPEED)

    fun reverse(): Player = movePlayer(Vector180Degree, PLAYER_SPEED)

    fun moveLeft(): Player = movePlayer(Vector90Degree, PLAYER_SPEED)

    fun moveRight(): Player = movePlayer(Vector270Degree, PLAYER_SPEED)

    fun moveLeftForward(): Player = movePlayer(Vector45Degree, PLAYER_SPEED)

    fun moveRightForward(): Player = movePlayer(Vector315Degree, PLAYER_SPEED)

    fun moveLeftBackward(): Player = movePlayer(Vector225Degree, PLAYER_SPEED)

    fun moveRightBackward(): Player = movePlayer(Vector315Degree, PLAYER_SPEED)

    fun turnLeft(): Player = turnPlayer(PLAYER_ROT_SPEED)

    fun turnRight(): Player = turnPlayer(-PLAYER_ROT_SPEED)

    private fun movePlayer(movVector: Offset, speed: Float): Player {
        val displacement = dirVersor.rotatedBy(movVector) * 3f
        return this.copy(
            pos = pos + displacement
        )
    }

    private fun turnPlayer(rotSpeed: Float): Player {
        return this.copy(
            dirVersor = dirVersor.rotateBy(rotSpeed)
        )
    }

    companion object {
        fun emptyPlayer(): Player = Player(Offset.Zero, 0u, 0u, 0f, Vector0Degree)
    }
}