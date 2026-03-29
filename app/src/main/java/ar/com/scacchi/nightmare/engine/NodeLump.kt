package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.readLittleEndianShort
import ar.com.scacchi.nightmare.data.readLittleEndianUShort
import java.nio.ByteBuffer

class NodeLump(
    val xPartition: Short,
    val yPartition: Short,
    val dxPartition: Short,
    val dyPartition: Short,
    val frontBoundBox: BoundBox = BoundBox(0, 0, 0, 0),
    val backBoundBox: BoundBox = BoundBox(0, 0, 0, 0),
    val frondChildId: UShort,
    val backChildId: UShort,
) {
    class BoundBox(
        val top: Short,
        val bottom: Short,
        val left: Short,
        val right: Short,
    ) {
        companion object {
            fun createFrom(buffer: ByteBuffer): BoundBox {
                return BoundBox(
                    top = buffer.readLittleEndianShort(),
                    bottom = buffer.readLittleEndianShort(),
                    left = buffer.readLittleEndianShort(),
                    right = buffer.readLittleEndianShort(),
                )
            }
        }
    }

    companion object {
        fun createFrom(buffer: ByteBuffer): NodeLump {
            return NodeLump(
                xPartition = buffer.readLittleEndianShort(),
                yPartition = buffer.readLittleEndianShort(),
                dxPartition = buffer.readLittleEndianShort(),
                dyPartition = buffer.readLittleEndianShort(),
                frontBoundBox = BoundBox.createFrom(buffer),
                backBoundBox = BoundBox.createFrom(buffer),
                frondChildId = buffer.readLittleEndianUShort(),
                backChildId = buffer.readLittleEndianUShort(),
            )
        }
    }
}

