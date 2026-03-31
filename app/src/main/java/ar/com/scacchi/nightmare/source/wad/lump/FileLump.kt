package ar.com.scacchi.nightmare.source.wad.lump

import ar.com.scacchi.nightmare.data.readByteArrayAsString
import ar.com.scacchi.nightmare.data.readLittleEndianInt
import java.nio.ByteBuffer

class FileLump {
    val filePos: Int
    val size: Int
    val name: String

    constructor(filePos: Int, size: Int, name: String) {
        this.filePos = filePos
        this.size = size
        this.name = name
    }

    companion object {
        fun createFrom(buffer: ByteBuffer): FileLump {
            return FileLump(
                filePos = buffer.readLittleEndianInt(),
                size = buffer.readLittleEndianInt(),
                name = buffer.readByteArrayAsString(8),
            )
        }
    }
}