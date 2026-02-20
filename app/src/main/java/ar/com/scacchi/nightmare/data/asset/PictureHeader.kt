package ar.com.scacchi.nightmare.data.asset

import ar.com.scacchi.nightmare.data.readLittleEndianShort
import ar.com.scacchi.nightmare.data.readLittleEndianUInt
import ar.com.scacchi.nightmare.data.readLittleEndianUShort
import java.nio.ByteBuffer

class PictureHeader(
    val width: UShort, // H
    val height: UShort,  // H
    val leftOffset: Short, // h
    val topOffset: Short,  // h
    val columnOffset: Array<UInt>, // wi = dth x I
) {
    companion object {
        fun createFromBuffer(buffer: ByteBuffer): PictureHeader {
            val width = buffer.readLittleEndianUShort()
            val height = buffer.readLittleEndianUShort()
            val leftOffset = buffer.readLittleEndianShort()
            val topOffset = buffer.readLittleEndianShort()

            val columnOffset = Array(width.toInt()) {
                buffer.readLittleEndianUInt()
            }
            return PictureHeader(
                width = width,
                height = height,
                leftOffset = leftOffset,
                topOffset = topOffset,
                columnOffset = columnOffset
            )
        }
    }
}