package ar.com.scacchi.nightmare.data.asset

import ar.com.scacchi.nightmare.data.readByte
import ar.com.scacchi.nightmare.data.readUByte
import java.nio.ByteBuffer

class Post @OptIn(ExperimentalUnsignedTypes::class) constructor(
    val topDelta: Byte,
    val length: UByte,
    val paddingPre: Byte,
    val data: Array<UByte>,
    val paddingPost: Byte,
    ) {
    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        fun createFrom(buffer: ByteBuffer): Post {

            val topDelta = buffer.readByte()
            if (topDelta != 0xFF.toByte()) {
                val length = buffer.readUByte()
                val paddingPre = buffer.readByte()  // unused
                val data = Array(length.toInt()) { buffer.readUByte() }
                val paddingPost = buffer.readByte()  // unused

                return Post(
                        topDelta = topDelta,
                        length = length,
                        paddingPre = paddingPre,
                        data = data,
                        paddingPost = paddingPost
                    )
            }
            return Post(
                    topDelta = topDelta,
                    length = 0u,
                    paddingPre = 0,
                    data = arrayOf(),
                    paddingPost = 0,
                )
        }
    }
}