package ar.com.scacchi.nightmare.engine

class Player(
    val thing: Thing
) {
    val xPos: Short = thing.xPos
    val yPos: Short = thing.yPos
    val angle: UShort = thing.angle
    val direction: UShort = thing.angle
    val type: UShort = thing.type
    val flags: UShort = thing.flags

    fun update() {

    }
}
