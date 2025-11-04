package ar.com.scacchi.nightmare.data.demo

import ar.com.scacchi.nightmare.data.readBoolean
import ar.com.scacchi.nightmare.data.readLittleEndianUInt
import ar.com.scacchi.nightmare.data.readUByte
import java.nio.ByteBuffer

class Demo(
    private val version: UByte,
    private val skill: Skill,
    private val episode: UInt,
    private val map: UInt,
    private val deathmatch: Boolean,
    private val reSpawnParm: Boolean,
    private val fastParm: Boolean,
    private val noMonsters: Boolean,
    private val consolePlayer: UInt,
    private val playerInGame: BooleanArray
) {


    companion object {
        fun createFrom(buffer: ByteBuffer): Demo? {
            val version =  buffer.readUByte() ?: return null
            val skill = Skill.entries[(buffer.readUByte() ?: return null).toInt() ]
                (buffer.readUByte() ?: return null)
            val episode = buffer.readLittleEndianUInt() ?: return null
            val map = buffer.readLittleEndianUInt() ?: return null
            val deathmatch = buffer.readBoolean() ?: return null
            val reSpawnParm = buffer.readBoolean() ?: return null
            val fastParm = buffer.readBoolean() ?: return null
            val noMonsters = buffer.readBoolean() ?: return null
            val consolePlayer = buffer.readLittleEndianUInt() ?: return null
            val playerInGame = BooleanArray(4) {
                buffer.readBoolean() ?: return null
            }

            return Demo(
                version = version,
                skill = skill,
                episode = episode,
                map = map,
                deathmatch = deathmatch,
                reSpawnParm = reSpawnParm,
                fastParm = fastParm,
                noMonsters = noMonsters,
                consolePlayer = consolePlayer,
                playerInGame = playerInGame
            )
        }
    }
}

enum class PlayerCommand {
    FORWARD,// Bit 0 - Adelante (Forward)
    BACKWARD,// Bit 1 - Atrás (Backward)
    TURN_RIGHT,// Bit 2 - Girar a la derecha (Turn Right)
    TURN_LEFT,// Bit 3 - Girar a la izquierda (Turn Left)
    RUN,// Bit 4 - Correr (Run)
    ATTACK,// Bit 5 - Disparar/Atacar (Attack)
    USE,// Bit 6 - Usar (Use/Open door)
    LATERAL,// Bit 7 - Strafe (Movimiento lateral)
}

class TicCmd( // Tamaño total: 8 bytes
    val forwardMove: Byte,     // 1 byte: Movimiento Adelante/Atrás (-127 a 127)
    val sideMove: Byte,        // 1 byte: Movimiento Lateral (Strafe) (-127 a 127)
    val angleTurn: Short,      // 2 bytes: Giro de ángulo (movimiento del mouse)
    val consistancy: Short,    // 2 bytes: Control de sincronización para red (net game)
    val chatChar: Byte,        // 1 byte: Carácter de chat para juegos en red
    val buttons: Byte,         // 1 byte: Estado de los botones (Attack, Use, Run, etc.)
) {
    companion object {
        fun createFrom(demoCmd: Int, angle: Byte): TicCmd? {
            val x = if ((demoCmd and BT_FORWARD) != 0) {
                if ((demoCmd and BT_RUN) != 0) 0.toByte()
                else 0
            } else if ((demoCmd and BT_BACKWARD) != 0) {
                if ((demoCmd and BT_RUN) != 0) (-FAST_SPEED)
                else (-MOVE_SPEED)
            } else 0
            return TicCmd(
                forwardMove =
                    if ((demoCmd and BT_FORWARD) != 0) {
                        if ((demoCmd and BT_RUN) != 0) FAST_SPEED
                        else MOVE_SPEED
                    } else if ((demoCmd and BT_BACKWARD) != 0) {
                        if ((demoCmd and BT_RUN) != 0) (-FAST_SPEED).toByte()
                        else (-MOVE_SPEED).toByte()
                    } else 0.toByte(),

                sideMove =
                    if ((demoCmd and BT_STRAFE) != 0) {
                        if ((demoCmd and BT_TURN_RIGHT) != 0) MOVE_SPEED
                        else if ((demoCmd and BT_TURN_LEFT) != 0) (-MOVE_SPEED).toByte()
                        else 0
                    } else 0.toByte(),
                angleTurn = angle.toShort(),
                consistancy = 0,
                chatChar = 0,
                buttons = demoCmd.toByte()
            )
        }
    }
}

const val BT_FORWARD =    0b00_000_001
const val BT_BACKWARD =   0b00_000_010
const val BT_TURN_RIGHT = 0b00_000_100
const val BT_TURN_LEFT =   0b00_001_000
const val BT_RUN =        0b00_010_000
const val BT_ATTACK =     0b00_100_000
const val BT_USE =        0b01_000_000
const val BT_STRAFE =    0b10_000_000

const val MOVE_SPEED: Byte = 0x40
const val FAST_SPEED: Byte = 0x7F


