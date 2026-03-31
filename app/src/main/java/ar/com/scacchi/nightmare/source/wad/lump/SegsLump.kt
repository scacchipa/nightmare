package ar.com.scacchi.nightmare.source.wad.lump

import java.nio.ByteBuffer

class SegsLump(
    val content: Array<SegLump>,
) {
    operator fun get(idx: Int) = content[idx]

    companion object {
        fun emptySegs(): SegsLump = SegsLump(emptyArray())
        fun createFrom(buffer: ByteBuffer, lump: FileLump): SegsLump {
            buffer.position(lump.filePos)
            return SegsLump(
                content = Array(lump.size / 12) {
                    SegLump.Companion.createFrom(buffer)
                }
            )
        }
    }
}