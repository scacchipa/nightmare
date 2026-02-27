package ar.com.scacchi.nightmare.data.asset.texture

import ar.com.scacchi.nightmare.data.readLittleEndianUInt
import java.nio.ByteBuffer

class TextureHeader(
    val textureCount: UInt,
    val textureOffset: UInt,
    val textureDataOffset: Array<UInt>,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer): TextureHeader {
            val textureCount = buffer.readLittleEndianUInt()
            val textureOffset = buffer.readLittleEndianUInt()
            val textureDataOffset = Array(textureCount.toInt()) {
                buffer.readLittleEndianUInt()
            }

            return TextureHeader(
                textureCount = textureCount,
                textureOffset = textureOffset,
                textureDataOffset = textureDataOffset
            )
        }
    }
}