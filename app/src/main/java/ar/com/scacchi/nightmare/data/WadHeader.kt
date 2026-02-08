package ar.com.scacchi.nightmare.data

import java.nio.ByteBuffer

class WadHeader(
    val identification: ByteArray,
    val numLumps: UInt,
    val infoTableOfs: UInt
) {

    companion object {
        fun createFrom(buffer: ByteBuffer): WadHeader {
            return WadHeader(
                identification = buffer.readByteArray(4),
                numLumps = buffer.readLittleEndianUInt(),
                infoTableOfs = buffer.readLittleEndianUInt(),
            )
        }
    }
}
