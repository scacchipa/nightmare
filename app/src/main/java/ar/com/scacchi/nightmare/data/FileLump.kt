package ar.com.scacchi.nightmare.data

import java.nio.ByteBuffer

class FileLump {
    val filepos: UInt
    val size: UInt
    val name: ByteArray

    constructor(filePos: UInt, size: UInt, name: ByteArray) {
        this.filepos = filePos
        this.size = size
        this.name = name
    }

    constructor(buffer: ByteBuffer) {
        this.filepos = buffer.readLittleEndianUInt() ?: 0u
        this.size = buffer.readLittleEndianUInt() ?: 0u
        this.name = buffer.readByteArray(8) ?: byteArrayOf()
    }
}
