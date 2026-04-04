package ar.com.scacchi.nightmare.source.wad.lump.data

import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.data.readUByte
import ar.com.scacchi.nightmare.source.wad.lump.FileLump
import java.nio.ByteBuffer

class FlatLump(
    val content: Array<Array<UByte>>,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer, fileLump: FileLump): FlatLump {
            buffer.position(fileLump.filePos)

            return FlatLump(
                content = Array(64) {
                    Array(64) {
                        buffer.readUByte()
                    }
                },
            )
        }
    }

    fun buildDoomImage(): DoomBitmap =
        DoomBitmap(64, 64).also { image ->
            for (idx in 0 until 64) {
                for (idy in 0 until 64) {
                    image[idx, idy] = this.content[idx][idy]
                }
            }
        }
}