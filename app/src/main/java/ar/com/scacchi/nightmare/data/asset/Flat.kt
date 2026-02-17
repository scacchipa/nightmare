package ar.com.scacchi.nightmare.data.asset

import ar.com.scacchi.nightmare.data.FileLump
import ar.com.scacchi.nightmare.data.color.Palette
import ar.com.scacchi.nightmare.data.readUByte
import java.nio.ByteBuffer

data class Flat(
    val content: Array<Array<UByte>>,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer, lump: FileLump): Flat {
            buffer.position(lump.filePos)

            return Flat(
                content = Array(64) {
                    Array(64) {
                        buffer.readUByte()
                    }
                },
            )
        }
    }

    fun buildDoomImage(palette: Palette): DoomImage {
        val image = DoomImage(64, 64)

        for (idx in 0 until 64) {
            for (idy in 0 until 64) {

                image.setAt(
                    x = idx,
                    y = idy,
                    color = palette[this.content[idx][idy].toInt()]
                )
            }
        }
        return image
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Flat

        return content.contentDeepEquals(other.content)
    }

    override fun hashCode(): Int {
        return content.contentDeepHashCode()
    }

}