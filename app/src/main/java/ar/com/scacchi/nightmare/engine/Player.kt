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
    constructor(thing: ThingLump) : this(
        xPos = thing.xPos.toFloat(),
        yPos = thing.yPos.toFloat(),
        angle = (thing.angle.toFloat() * PI / 180.0f).toFloat(),
        type = thing.type,
        flags = thing.flags,
        height = PLAYER_HEIGHT
    )

    fun advance(): Player = movePlayer(0f, PLAYER_SPEED)

    fun reverse(): Player = movePlayer(PI.toFloat(), PLAYER_SPEED)

    fun moveLeft(): Player = movePlayer(PI.toFloat() / 2f, PLAYER_SPEED)

    fun moveRight(): Player = movePlayer(-PI.toFloat() / 2f, PLAYER_SPEED)

    fun moveLeftForward(): Player = movePlayer(PI.toFloat() / 4f, PLAYER_SPEED)

    fun moveRightForward(): Player = movePlayer(-PI.toFloat() / 4f, PLAYER_SPEED)

    fun moveLeftBackward(): Player = movePlayer(3 * PI.toFloat() / 4f, PLAYER_SPEED)

    fun moveRightBackward(): Player = movePlayer(-3 * PI.toFloat() / 4f, PLAYER_SPEED)

    fun turnLeft(): Player = turnPlayer(PLAYER_ROT_SPEED)

    fun turnRight(): Player = turnPlayer(-PLAYER_ROT_SPEED)

    private fun movePlayer(rotationAngle: Float, speed: Float): Player {
        val newPlayerAngle = angle + rotationAngle

        val dx = speed * cos(newPlayerAngle)
        val dy = speed * sin(newPlayerAngle)

        return this.copy(
            xPos = xPos + dx,
            yPos = yPos + dy,
        )
    }

    private fun turnPlayer(rotSpeed: Float): Player {
        return this.copy(
            angle = angle + rotSpeed
        )
    }

    companion object {
        fun emptyPlayer(): Player = Player(0f, 0f, 0f, 0u, 0u, 0f)
    }
}