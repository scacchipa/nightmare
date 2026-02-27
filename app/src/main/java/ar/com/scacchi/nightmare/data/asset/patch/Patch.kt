package ar.com.scacchi.nightmare.data.asset.patch

import ar.com.scacchi.nightmare.data.FileLump
import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.data.asset.patch.PatchHeader
import ar.com.scacchi.nightmare.data.asset.patch.Post
import java.nio.ByteBuffer

typealias Sprite = Patch
typealias Picture = Patch

data class Patch(
    val header: PatchHeader,
    val posts: Array<Post>,
) {
    val width get() = header.width
    val height get() = header.height

    val bitmap by lazy { buildDoomImage() }

    operator fun get(x: Int, y: Int): UByte? = bitmap[x, y]
    operator fun set(x: Int, y: Int, value: UByte) = bitmap.set(x, y, value)

    companion object {
        fun emptyPicture(): Picture = Picture(PatchHeader.Companion.emptyHeader(), emptyArray())

        fun createFrom(buffer: ByteBuffer, lump: FileLump): Picture {
            buffer.position(lump.filePos)

            val header = PatchHeader.Companion.createFromBuffer(buffer)
            val posts = getPosts(buffer, header.width.toInt())

            return Picture(header, posts)
        }

        @OptIn(ExperimentalUnsignedTypes::class)
        private fun getPosts(buffer: ByteBuffer, width: Int): Array<Post> {
            val posts = ArrayList<Post>()

            repeat(width) {
                var post = Post(0u, 0u, 0, arrayOf(), 0)
                while (post.topDelta != 0xFF.toUByte()) {
                    post = Post.Companion.createFrom(buffer)
                    posts.add(post)
                }
            }
            return posts.toTypedArray()
        }
    }

    fun buildDoomImage(): DoomBitmap =
        DoomBitmap(header.width.toInt(), header.height.toInt()).also { bitmap ->
            var ix = 0
            var iy: Int
            for (post in posts) {
                if (post.topDelta == 0xFF.toUByte()) {
                    ix += 1
                    continue
                }

                iy = post.topDelta.toInt()
                for (postY in 0 until post.length.toInt()) {
                    bitmap[ix, iy] = post.data[postY]
                    iy += 1
                }
            }
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Picture

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