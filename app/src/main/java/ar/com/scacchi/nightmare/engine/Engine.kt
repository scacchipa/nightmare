package ar.com.scacchi.nightmare.engine;

import android.content.Context
import androidx.annotation.RawRes
import ar.com.scacchi.nightmare.BSP
import ar.com.scacchi.nightmare.R
import java.nio.ByteBuffer

class Engine(
    val wadData: WadData
) {
    val vertexes: Vertexes
        get() = wadData.vertexes

    val lineDefs: LineDefs
        get() = wadData.lineDefs

    val nodes: Nodes
        get() = wadData.nodes

    val subSectors: SubSectors
        get() = wadData.subSectors

    val segs: Segs
        get() = wadData.segs

    val things: Things
        get() = wadData.things

    val player: Player
        get() = wadData.player

    val bsp: BSP = BSP(
        engine = this,
    )

    companion object {
        fun createFrom(context: Context, @RawRes wadPath: Int = R.raw.doom): Engine {

            val file = context.resources.openRawResource(wadPath)
            val buffer = ByteBuffer.wrap(file.readBytes())

            return Engine(
                wadData = WadData.create(buffer, "E1M1")
            )
        }
    }
}
