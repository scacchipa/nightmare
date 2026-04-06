package ar.com.scacchi.nightmare.data.color

import java.nio.ByteBuffer

class PaletteLump(
    val content: Array<PalColor>
) {
    operator fun get(idx: Int): PalColor = content[idx]

    companion object {
        fun createFrom(buffer: ByteBuffer): PaletteLump {
            return PaletteLump(Array(256) {
                PalColor.createFrom(buffer)
            })
        }
    }
}

