package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class LineDefsLump(
    val content: Array<LineDefLump>
) {
    operator fun get(idx: Int): LineDefLump = content[idx]

    companion object {
        fun emptyLineDefs(): LineDefsLump = LineDefsLump(emptyArray())
        fun createFrom(buffer: ByteBuffer, lump: FileLump): LineDefsLump {
            buffer.position(lump.filePos)
            return LineDefsLump(
                content = Array(lump.size / 14) {
                    LineDefLump.createFrom(buffer)
                }
            )
        }
    }
}

