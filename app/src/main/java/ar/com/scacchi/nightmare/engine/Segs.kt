package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class Segs(
    val content: Array<Seg>,
) {
    operator fun get(idx: Int) = content[idx]

    companion object {
        fun createFrom(
            buffer: ByteBuffer,
            lump: FileLump,
            vertexes: Vertexes,
            lineDefs: LineDefs
        ): Segs {
            buffer.position(lump.filePos)
            return Segs(
                content = Array(lump.size / 12) {
                    Seg.createFrom(buffer, vertexes, lineDefs)
                }
            )
        }
    }
}
