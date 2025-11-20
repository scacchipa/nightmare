package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.readLittleEndianShort
import java.nio.ByteBuffer

class SubSector(
    val segCount: Short,
    val firstSegId: Short,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer): SubSector {
            return SubSector(
                segCount = buffer.readLittleEndianShort(),
                firstSegId = buffer.readLittleEndianShort(),
            )
        }
    }
}