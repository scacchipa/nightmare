package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class SideDefsLump(
    val content: Array<SideDefLump>,
) {
    operator fun get(idx: Int) = content[idx]

    companion object {
        fun emptySideDefs(): SideDefsLump = SideDefsLump(emptyArray())
        fun createFrom(
            buffer: ByteBuffer,
            lump: FileLump,
        ): SideDefsLump {
            buffer.position(lump.filePos)
            return SideDefsLump(
                content = Array(lump.size / 30) {
                    SideDefLump.createFrom(buffer)
                }
            )
        }
    }
}

