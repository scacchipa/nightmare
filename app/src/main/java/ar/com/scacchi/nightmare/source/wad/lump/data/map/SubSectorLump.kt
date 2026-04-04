package ar.com.scacchi.nightmare.source.wad.lump.data.map

import ar.com.scacchi.nightmare.data.readLittleEndianShort
import java.nio.ByteBuffer

class SubSectorLump(
    val segCount: Short,
    val firstSegId: Short,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer): SubSectorLump {
            return SubSectorLump(
                segCount = buffer.readLittleEndianShort(),
                firstSegId = buffer.readLittleEndianShort(),
            )
        }
    }
}