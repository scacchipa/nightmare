package ar.com.scacchi.nightmare.source.wad.lump

import java.nio.ByteBuffer

class ThingsLump(
    val content: Array<ThingLump>,
) {
    operator fun get(idx: Int) = content[idx]

    fun dropFirstThing() = ThingsLump(content.drop(1).toTypedArray())

    companion object {
        fun emptyThings(): ThingsLump = ThingsLump(emptyArray())
        fun createFrom(buffer: ByteBuffer, lump: FileLump): ThingsLump {
            buffer.position(lump.filePos)
            return ThingsLump(
                content = Array(lump.size / 10) {
                    ThingLump.Companion.createFrom(buffer)
                }
            )
        }

        fun createWithoutPlayer(things: ThingsLump): ThingsLump {
            return things.dropFirstThing()
        }
    }
}