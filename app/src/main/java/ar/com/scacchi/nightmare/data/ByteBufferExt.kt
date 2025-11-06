package ar.com.scacchi.nightmare.data


import java.nio.ByteBuffer

fun ByteBuffer.readByte(): Byte {
    return this.get()
}

fun ByteBuffer.readUByte(): UByte {
    return this.get().toUByte()
}

fun ByteBuffer.readLittleEndianUShort(): UShort {
    return (get().toUByte() +
            get().toUByte().toUShort().rotateLeft(8)
            ).toUShort()
}

fun ByteBuffer.readLittleEndianShort(): Short {
    return (get().toUByte() +
            get().toUByte().toUShort().rotateLeft(8)
            ).toShort()
}

fun ByteBuffer.readLittleEndianUInt(): UInt {
    return get().toUByte().toUInt() +
            get().toUByte().toUInt().rotateLeft(8) +
            get().toUByte().toUInt().rotateLeft(16) +
            get().toUByte().toUInt().rotateLeft(24)
}

fun ByteBuffer.readLittleEndianInt(): Int {
    return (get().toUByte() +
            get().toUByte().toUInt().rotateLeft(8) +
            get().toUByte().toUInt().rotateLeft(16) +
            get().toUByte().toUInt().rotateLeft(24)
            ).toInt()
}

fun ByteBuffer.readByteArray(len: Int): ByteArray {
    val dest = ByteArray(len)
    this.get(dest)
    return dest
}

fun ByteBuffer.readByteArrayAsString(len: Int): String {
    return this.readByteArray(8)
        .filter { it != 0.toByte() }
        .joinToString("") {
        it.toInt().toChar().toString()
    }
}

fun ByteBuffer.readBoolean(): Boolean {
    return this.readByte().toInt() != 0
}

