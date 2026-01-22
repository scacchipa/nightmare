package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.settings.PLAYER_HEIGHT
import ar.com.scacchi.nightmare.settings.PLAYER_ROT_SPEED
import ar.com.scacchi.nightmare.settings.PLAYER_SPEED
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class Player(
    val xPos: Float,
    val yPos: Float,
    val angle: Float,
    val type: UShort,
    val flags: UShort,
    val height: Float,
) {
    constructor(thing: Thing) : this(
        xPos = thing.xPos.toFloat(),
        yPos = thing.yPos.toFloat(),
        angle = (thing.angle.toFloat() * PI / 180.0f).toFloat(),
        type = thing.type,
        flags = thing.flags,
        height = PLAYER_HEIGHT
    )

    fun advance(): Player {

        val dx = PLAYER_SPEED * cos(angle)
        val dy = PLAYER_SPEED * sin(angle)

        return this.copy(
            xPos = xPos + dx,
            yPos = yPos + dy,
        )
    }

    fun reverse(): Player {
        val dx = PLAYER_SPEED * cos(angle)
        val dy = PLAYER_SPEED * sin(angle)

        return this.copy(
            xPos = xPos - dx,
            yPos = yPos - dy,
        )
    }

    fun moveLeft(): Player {
        val leftAngle = (angle + PI / 2).toFloat()

        val dx = PLAYER_SPEED * cos(leftAngle)
        val dy = PLAYER_SPEED * sin(leftAngle)

        return this.copy(
            xPos = xPos + dx,
            yPos = yPos + dy,
        )
    }

    fun moveRight(): Player {
        val rightAngle = (angle - PI / 2).toFloat()

        val dx = PLAYER_SPEED * cos(rightAngle)
        val dy = PLAYER_SPEED * sin(rightAngle)

        return this.copy(
            xPos = xPos + dx,
            yPos = yPos + dy,
        )
    }

    fun turnLeft(): Player {
        return this.copy(
            angle = angle + PLAYER_ROT_SPEED
        )
    }

    fun turnRight(): Player {
        return this.copy(
            angle = angle - PLAYER_ROT_SPEED
        )
    }
}
