package ar.com.scacchi.nightmare.data.asset

import ar.com.scacchi.nightmare.data.readByteArrayAsString
import ar.com.scacchi.nightmare.data.readLittleEndianInt
import ar.com.scacchi.nightmare.data.readLittleEndianUShort
import java.nio.ByteBuffer

class TextureMap(
    val name: String,
    val flags: Int,
    val width: UShort,
    val height: UShort,
    val columnDir: Int,  // unused
    val patchCount: UShort,
    val patchMaps: Array<PatchMap>,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer, offset: Int): TextureMap {

            buffer.position(offset)
            val name = buffer.readByteArrayAsString(8)
            val flags = buffer.readLittleEndianInt()
            val width = buffer.readLittleEndianUShort()
            val height = buffer.readLittleEndianUShort()
            val columnDir = buffer.readLittleEndianInt()
            val patchCount = buffer.readLittleEndianUShort()
            val patchMaps = Array(patchCount.toInt()) { idx ->
                PatchMap.createFrom(buffer, offset + 22 + idx * 10)
            }

            return TextureMap(
                name = name,
                flags = flags,
                width = width,
                height = height,
                columnDir = columnDir,
                patchCount = patchCount,
                patchMaps = patchMaps,
            )
        }
    }
}