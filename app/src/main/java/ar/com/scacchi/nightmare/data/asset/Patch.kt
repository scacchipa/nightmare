package ar.com.scacchi.nightmare.data.asset

import ar.com.scacchi.nightmare.data.FileLump
import ar.com.scacchi.nightmare.data.color.Palette
import java.nio.ByteBuffer

data class Patch(
    val header: PatchHeader,
    val posts: Array<Post>,
) {
    companion object {
        fun createFrom(buffer: ByteBuffer, lump: FileLump): Patch {
            buffer.position(lump.filePos)

            val header = PatchHeader.createFromBuffer(buffer)
            val posts = getPosts(buffer, header.width.toInt())

            return Patch(
                header = header,
                posts = posts,
            )
        }

        @OptIn(ExperimentalUnsignedTypes::class)
        private fun getPosts(buffer: ByteBuffer, width: Int): Array<Post> {
            val posts = ArrayList<Post>()

            repeat(width) {
                var post = Post(0, 0u, 0, arrayOf(), 0)
                while (post.topDelta != 0xFF.toByte()) {
                    post = Post.createFrom(buffer)
                    posts.add(post)
                }
            }
            return posts.toTypedArray()
        }
    }

    fun buildDoomImage(palette: Palette): DoomImage {
        val image = DoomImage(header.width.toInt(), header.height.toInt())

        var ix = 0
        for (post in posts) {
            if (post.topDelta == 0xFF.toByte()) {
                ix += 1
                continue
            }

            for (iy in 0 until post.length.toInt()) {
                image.setAt(
                    x = ix,
                    y = iy,
                    color = palette[post.data[iy].toInt()]
                )
            }
        }
        return image
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Patch

        if (header != other.header) return false
        if (!posts.contentEquals(other.posts)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = header.hashCode()
        result = 31 * result + posts.contentHashCode()
        return result
    }
}