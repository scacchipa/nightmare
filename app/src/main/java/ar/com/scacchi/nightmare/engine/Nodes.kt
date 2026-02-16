package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class Nodes(
    val content: Array<Node>
) {
    operator fun get(idx: Int) = content[idx]

    fun count() = content.size

    fun isEmpty() = content.isEmpty()

    companion object {
        fun createFrom(buffer: ByteBuffer, lump: FileLump): Nodes {
            buffer.position(lump.filePos)
            return Nodes(
                content = Array(lump.size / 28) {
                    Node.createFrom(buffer)
                }
            )
        }
    }
}