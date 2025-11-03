package ar.com.scacchi.nightmare.data

import java.io.InputStream
import java.nio.ByteBuffer

class WadInfo {
    val identification: ByteArray
    val numLumps: UInt
    val infoTableOfs: UInt

    constructor(identification: ByteArray, numLumps: UInt, infoTableOfs: UInt) {
        this.identification = identification
        this.numLumps = numLumps
        this.infoTableOfs = infoTableOfs
    }

    constructor(buffer: ByteBuffer) {
        this.identification = buffer.readByteArray(4) ?: byteArrayOf()
        this.numLumps = buffer.readLittleEndianUInt() ?: 0u
        this.infoTableOfs = buffer.readLittleEndianUInt() ?: 0u
    }
}
