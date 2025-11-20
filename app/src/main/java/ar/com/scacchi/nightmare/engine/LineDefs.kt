package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class LineDefs(
    val content: Array<LineDef>
) {
    companion object {
        fun createFrom(buffer: ByteBuffer, lump: FileLump): LineDefs {
            buffer.position(lump.filePos)
            return LineDefs(
                content = Array(lump.size / 14) {
                    LineDef.createFrom(buffer)
                }
            )
        }
    }
}