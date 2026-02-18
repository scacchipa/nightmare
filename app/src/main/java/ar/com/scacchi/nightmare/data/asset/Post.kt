package ar.com.scacchi.nightmare.data.asset

import ar.com.scacchi.nightmare.data.readByte
import ar.com.scacchi.nightmare.data.readUByte
import java.nio.ByteBuffer

data class Post @OptIn(ExperimentalUnsignedTypes::class) constructor(
    val topDelta: UByte,
    val length: UByte,
    val paddingPre: Byte,
    val data: Array<UByte>,
    val paddingPost: Byte,
    ) {
    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        fun createFrom(buffer: ByteBuffer): Post {

            val topDelta = buffer.readUByte()
            if (topDelta != 0xFF.toUByte()) {
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        if (topDelta != other.topDelta) return false
        if (paddingPre != other.paddingPre) return false
        if (paddingPost != other.paddingPost) return false
        if (length != other.length) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = topDelta.toInt()
        result = 31 * result + paddingPre.toInt()
        result = 31 * result + paddingPost.toInt()
        result = 31 * result + length.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}