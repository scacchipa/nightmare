package ar.com.scacchi.nightmare.data.asset

import ar.com.scacchi.nightmare.data.readByte
import java.nio.ByteBuffer

class Post(
        val topDelta: Byte,
        val length: Byte,
        val paddingPre: Byte,
        val data: ByteArray,
        val paddingPost: Byte,
    ) {
    companion object {
        fun createFrom(buffer: ByteBuffer): Post {

            val topDelta = buffer.readByte()
            if (topDelta != 0xFF.toByte()) {
                val length = buffer.readByte()
                val paddingPre = buffer.readByte()  // unused
                val data = Array(length.toInt()) { buffer.readByte() }
                val paddingPost = buffer.readByte()  // unused

                return Post(
                        topDelta = topDelta,
                        length = length,
                        paddingPre = paddingPre,
                        data = data.toByteArray(),
                        paddingPost = paddingPost
                    )
            }
            return Post(
                    topDelta = topDelta,
                    length = 0,
                    paddingPre = 0,
                    data = ByteArray(0),
                    paddingPost = 0,
                )
        }
    }
}