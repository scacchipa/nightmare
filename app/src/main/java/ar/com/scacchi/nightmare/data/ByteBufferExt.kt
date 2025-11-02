package ar.com.scacchi.nightmare.data


import java.nio.BufferUnderflowException
import java.nio.ByteBuffer

fun ByteBuffer.readByte(): Byte? {
    return try {
        this.get()
    } catch (e: BufferUnderflowException) {
        null
    }
}

fun ByteBuffer.readUByte(): UByte? {
    return try {
        this.get().toUByte()
    } catch (e: BufferUnderflowException) {
        null
    }
}

fun ByteBuffer.readLittleEndianUShort(): UShort? {
    return try {
        (get().toUByte() +
                get().toUByte().toUShort().rotateLeft(8)
                ).toUShort()
    } catch (e: BufferUnderflowException) {
        null
    }
}

fun ByteBuffer.readLittleEndianShort(): Short? {
    return try {
        (get().toUByte() +
                get().toUByte().toUShort().rotateLeft(8)
                ).toShort()
    } catch (e: BufferUnderflowException) {
        null
    }
}

fun ByteBuffer.readLittleEndianUInt(): UInt? {
    return try {
        get().toUByte().toUInt() +
                get().toUByte().toUInt().rotateLeft(8) +
                get().toUByte().toUInt().rotateLeft(16) +
                get().toUByte().toUInt().rotateLeft(24)
    } catch (e: BufferUnderflowException) {
        null
    }
}

fun ByteBuffer.readLittleEndianInt(): Int? {
    return try {
        (get().toUByte() +
                get().toUByte().toUInt().rotateLeft(8) +
                get().toUByte().toUInt().rotateLeft(16) +
                get().toUByte().toUInt().rotateLeft(24)
                ).toInt()
    } catch (e: BufferUnderflowException) {
        null
    }
}

fun ByteBuffer.readByteArray(len: Int): ByteArray? {
    try {
        val dest = ByteArray(len)
        this.get(dest)
        return dest
    } catch (e: BufferUnderflowException) {
        return null
    }
}

