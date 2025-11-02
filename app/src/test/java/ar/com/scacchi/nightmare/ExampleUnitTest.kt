package ar.com.scacchi.nightmare

import ar.com.scacchi.nightmare.data.readByte
import ar.com.scacchi.nightmare.data.readByteArray
import ar.com.scacchi.nightmare.data.readLittleEndianInt
import ar.com.scacchi.nightmare.data.readLittleEndianShort
import ar.com.scacchi.nightmare.data.readLittleEndianUInt
import ar.com.scacchi.nightmare.data.readLittleEndianUShort
import ar.com.scacchi.nightmare.data.readUByte
import org.junit.Test

import org.junit.Assert.*
import java.nio.ByteBuffer

class ByteBufferExtensionsTest {

    @Test
    fun `debe leer UByte y combinarlos correctamente`() {

        val reader = ByteBuffer.wrap(
            byteArrayOf(0x34, 0xFF.toByte(),0x00, 0x7F, 0x81.toByte())
        )

        assertEquals(0x34u.toUByte(), reader.readUByte()!!)
        assertEquals(255.toUByte(), reader.readUByte()!!)
        assertEquals(0x00u.toUByte(), reader.readUByte()!!)
        assertEquals(127u.toByte(), reader.readByte()!!)
        assertEquals((129).toByte(), reader.readByte()!!)

    }

    @Test
    fun `debe lee un UByte retornar null si el flujo esta vacio (EOF)`() {

        val reader1 = ByteBuffer.wrap(byteArrayOf())

        assertNull(reader1.readUByte())
    }

    @Test
    fun `debe leer Byte y combinarlos correctamente`() {

        val reader = ByteBuffer.wrap(
            byteArrayOf(0x34, 0xFF.toByte(),0x00, 0x7F, 0x81.toByte())
        )

        assertEquals(0x34.toByte(), reader.readByte()!!)
        assertEquals((-1).toByte(), reader.readByte()!!)
        assertEquals(0x00u.toByte(), reader.readByte()!!)
        assertEquals(127u.toByte(), reader.readByte()!!)
        assertEquals((-127).toByte(), reader.readByte()!!)
    }

    @Test
    fun `debe leer USHORTs y combinarlos correctamente en Little-Endian`() {

        val reader = ByteBuffer.wrap(
            byteArrayOf(
                0x34, 0x12, 0xFF.toByte(), 0xFF.toByte(),0x00, 0x00,
                0xFF.toByte(), 0x7F, 0x01.toByte(), 0x80.toByte())
        )

        assertEquals(0x1234u.toUShort(), reader.readLittleEndianUShort()!!)
        assertEquals(65535.toUShort(), reader.readLittleEndianUShort()!!)
        assertEquals(0x0000u.toUShort(), reader.readLittleEndianUShort()!!)
        assertEquals(32767u.toUShort(), reader.readLittleEndianUShort()!!)
        assertEquals(32769u.toUShort(), reader.readLittleEndianUShort()!!)
    }

    @Test
    fun `debe lee un USHORT retornar null si el flujo esta vacio (EOF)`() {

        val reader1 = ByteBuffer.wrap(byteArrayOf())

        assertNull(reader1.readLittleEndianUShort())

        val reader2 = ByteBuffer.wrap(byteArrayOf(0x34))
        assertNull(reader2.readLittleEndianUShort())
    }

    @Test
    fun `debe leer SHORTs y combinarlos correctamente en Little-Endian`() {

        val reader = ByteBuffer.wrap(
            byteArrayOf(
                0x34, 0x12, 0xFF.toByte(), 0xFF.toByte(),0x00, 0x00,
                0xFF.toByte(), 0x7F, 0x01.toByte(), 0x80.toByte())
        )

        assertEquals(0x1234u.toShort(), reader.readLittleEndianShort()!!)
        assertEquals(65535.toShort(), reader.readLittleEndianShort()!!)
        assertEquals(0x0000u.toShort(), reader.readLittleEndianShort()!!)
        assertEquals(32767u.toShort(), reader.readLittleEndianShort()!!)
        assertEquals((-32767).toShort(), reader.readLittleEndianShort()!!)
    }

    @Test
    fun `debe lee un SHORT retornar null si el flujo esta vacio (EOF)`() {

        val reader1 = ByteBuffer.wrap(byteArrayOf())
        assertNull(reader1.readLittleEndianShort())

        val reader2 = ByteBuffer.wrap(byteArrayOf(0x34))
        assertNull(reader2.readLittleEndianShort())
    }

    @Test
    fun `debe leer UINTs y combinarlos correctamente en Little-Endian`() {

        val reader = ByteBuffer.wrap(
            byteArrayOf(0x4D, 0x3C, 0x2B, 0x1A,
                0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(),
                0x00, 0x00, 0x00, 0x00,
                0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F,
                0x01.toByte(), 0x00, 0x00, 0x80.toByte())
        )

        assertEquals(439041101.toUInt(), reader.readLittleEndianUInt()!!)
        assertEquals(0xFFFFFFFF.toUInt(), reader.readLittleEndianUInt()!!)
        assertEquals(0x00000000u, reader.readLittleEndianUInt()!!)
        assertEquals(2_147_483_647u.toUInt(), reader.readLittleEndianUInt()!!)
        assertEquals(2_147_483_649u.toUInt(), reader.readLittleEndianUInt()!!)
    }

    @Test
    fun `debe lee un UINTs retornar null si el flujo esta vacio (EOF)`() {

        val reader1 = ByteBuffer.wrap(byteArrayOf())
        assertNull(reader1.readLittleEndianInt())

        val reader2 = ByteBuffer.wrap(byteArrayOf(0x34))
        assertNull(reader2.readLittleEndianInt())

        val reader3 = ByteBuffer.wrap(byteArrayOf(0x34, 0x12))
        assertNull(reader3.readLittleEndianInt())

        val reader4 = ByteBuffer.wrap(byteArrayOf(0x34, 0x12, 0xFF.toByte()))
        assertNull(reader4.readLittleEndianInt())
    }

    @Test
    fun `debe leer INTs y combinarlos correctamente en Little-Endian`() {

        val reader = ByteBuffer.wrap(
            byteArrayOf(0x4D, 0x3C, 0x2B, 0x1A,
                0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(),
                0x00, 0x00, 0x00, 0x00,
                0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0x7F,
                0x01.toByte(), 0x00, 0x00, 0x80.toByte())
        )

        assertEquals(439041101, reader.readLittleEndianInt()!!)
        assertEquals(-1, reader.readLittleEndianInt()!!)
        assertEquals(0x0000u.toInt(), reader.readLittleEndianInt()!!)
        assertEquals(2_147_483_647u.toInt(), reader.readLittleEndianInt()!!)
        assertEquals((-2_147_483_647), reader.readLittleEndianInt()!!)
    }

    @Test
    fun `debe lee un INTs retornar null si el flujo esta vacio (EOF)`() {

        val reader1 = ByteBuffer.wrap(byteArrayOf())
        assertNull(reader1.readLittleEndianInt())

        val reader2 = ByteBuffer.wrap(byteArrayOf(0x34))
        assertNull(reader2.readLittleEndianInt())

        val reader3 = ByteBuffer.wrap(byteArrayOf(0x34, 0x12))
        assertNull(reader3.readLittleEndianInt())

        val reader4 = ByteBuffer.wrap(byteArrayOf(0x34, 0x12, 0xFF.toByte()))
        assertNull(reader4.readLittleEndianInt())
    }

    @Test
    fun `debe lee un array de bytes correctamente`() {
        val reader = ByteBuffer.wrap(
            byteArrayOf(0x50, 0x61, 0x62, 0x6C, 0x6F)
        )

        val bytes = reader
            .readByteArray(5)
            ?.joinToString("") { it.toInt().toChar().toString() }
        assertEquals(bytes, "Pablo")
    }
}
