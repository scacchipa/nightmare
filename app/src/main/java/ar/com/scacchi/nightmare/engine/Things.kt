package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class Things(
    val content: Array<Thing>,
) {
    operator fun get(idx: Int) = content[idx]

    fun dropFirstThing() = Things(content.drop(1).toTypedArray())

    companion object {
        fun emptyThings(): Things = Things(emptyArray())
        fun createFrom(buffer: ByteBuffer, lump: FileLump): Things {
            buffer.position(lump.filePos)
            return Things(
                content = Array(lump.size / 10) {
                    Thing.createFrom(buffer)
                }
            )
        }
        fun createWithoutPlayer(things: Things): Things {
            return things.dropFirstThing()
        }
    }
}