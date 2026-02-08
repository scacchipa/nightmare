package ar.com.scacchi.nightmare.data.color

import ar.com.scacchi.nightmare.data.readUByte
import java.nio.ByteBuffer

class ColorTable {
    val content: Array<UByte>

    constructor(array: Array<UByte>) {
        this.content = array
    }

    operator fun get(idx: Int): UByte = content[idx]

    companion object {
        fun createColorTable(buffer: ByteBuffer): ColorTable {
            return ColorTable(
                array = Array(256) {
                    buffer.readUByte()
                }
            )
        }
    }
}
