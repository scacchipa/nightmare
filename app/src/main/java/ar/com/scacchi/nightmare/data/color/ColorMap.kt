package ar.com.scacchi.nightmare.data.color

import ar.com.scacchi.nightmare.data.FileLump
import java.nio.ByteBuffer

class ColorMap {
    val content: Array<ColorTable>

    constructor(array: Array<ColorTable>) {
        this.content = array
    }

    operator fun get(idx: Int): ColorTable = content[idx]

    companion object {
        fun createColorMap(lump: FileLump, buffer: ByteBuffer): ColorMap? {
            buffer.position(lump.filePos.toInt())

            return ColorMap(
                array = Array(lump.size / 256) {
                    ColorTable.createColorTable(buffer) ?: return null
                }
            )
        }
    }
}
