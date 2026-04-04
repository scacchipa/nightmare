package ar.com.scacchi.nightmare.source.wad.lump.data.patch

import java.nio.ByteBuffer

class Posts(
    val content: Array<Post>
) {
    companion object {
        fun createFrom(buffer: ByteBuffer, width: Int): Posts {
            val posts = ArrayList<Post>()

            repeat(width) {
                var post = Post(0u, 0u, 0, arrayOf(), 0)
                while (post.topDelta != 0xFF.toUByte()) {
                    post = Post.createFrom(buffer)
                    posts.add(post)
                }
            }
            return Posts(posts.toTypedArray())
        }
    }
}