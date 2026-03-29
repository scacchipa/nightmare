package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class NodesLump(
    val content: Array<NodeLump>
) {
    operator fun get(idx: Int) = content[idx]

    fun count() = content.size

    fun isEmpty() = content.isEmpty()

    companion object {
        fun emptyNodes(): NodesLump = NodesLump(emptyArray())
        fun createFrom(buffer: ByteBuffer, lump: FileLump): NodesLump {
            buffer.position(lump.filePos)
            return NodesLump(
                content = Array(lump.size / 28) {
                    NodeLump.createFrom(buffer)
                }
            )
        }
    }
}

