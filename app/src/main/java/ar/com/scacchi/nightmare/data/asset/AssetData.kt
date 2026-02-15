package ar.com.scacchi.nightmare.data.asset

import ar.com.scacchi.nightmare.data.FileLump
import ar.com.scacchi.nightmare.data.color.Palette
import java.nio.ByteBuffer

class Patch(
    val header: PatchHeader,
    val posts: Array<Post>,
    val image: DoomImage,
) {

    companion object {
        fun createFrom(buffer: ByteBuffer, lump: FileLump, palette: Palette): Patch {
            buffer.position(lump.filePos)

            val header  = PatchHeader.createFromBuffer(buffer)
            val posts = getPosts(buffer, header.width.toInt())

            return Patch(
                header = header,
                posts = posts,
                image = buildDoomImage(header, posts, palette)
            )
        }

        @OptIn(ExperimentalUnsignedTypes::class)
        private fun getPosts(buffer: ByteBuffer, width: Int): Array<Post> {
            val posts = ArrayList<Post>()

            repeat(width) {
                var post = Post(0, 0, 0, UByteArray(0), 0)
                while (post.topDelta != 0xFF.toByte()) {
                    post = Post.createFrom(buffer)
                    posts.add(post)
                }
            }
            return posts.toTypedArray()
        }

        private fun buildDoomImage(
            header: PatchHeader, posts: Array<Post>, palette: Palette
        ): DoomImage {
            val image = DoomImage(header.width.toInt(), header.height.toInt())

            var ix = 0
            for (post in posts) {
                if (post.topDelta == 0xFF.toByte()) {
                    ix += 1
                    continue
                }

                for (iy in 0 until post.length) {
                    image.setAt(
                        x = ix,
                        y = iy + post.topDelta,
                        color = palette[post.data[iy].toInt()])
                }
            }
            return image
        }
    }
}