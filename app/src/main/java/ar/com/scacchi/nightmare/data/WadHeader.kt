package ar.com.scacchi.nightmare.data

import java.nio.ByteBuffer

class WadHeader(
    val identification: ByteArray,
    val numLumps: UInt,
    val infoTableOfs: UInt
) {
    companion object {
        fun emptyHeader(): WadHeader = WadHeader(
            identification = byteArrayOf(0),
            numLumps = 0u,
            infoTableOfs = 0u,
        )
        fun createFrom(buffer: ByteBuffer): WadHeader {
            return WadHeader(
                identification = buffer.readByteArray(4),
                numLumps = buffer.readLittleEndianUInt(),
                infoTableOfs = buffer.readLittleEndianUInt(),
            )
        }
    }
}
