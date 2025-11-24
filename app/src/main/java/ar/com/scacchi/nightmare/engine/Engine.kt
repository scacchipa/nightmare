package ar.com.scacchi.nightmare.engine;

import android.content.Context
import androidx.annotation.RawRes
import ar.com.scacchi.nightmare.BSP
import ar.com.scacchi.nightmare.R
import java.nio.ByteBuffer

data class Engine(
    val vertexes: Vertexes,
    val lineDefs: LineDefs,
    val nodes: Nodes,
    val subSectors: SubSectors,
    val segs: Segs,
    val things: Things,
    val player: Player,

) {
    val bsp: BSP = BSP(this)

    constructor(wadData: WadData) : this(
        vertexes = wadData.vertexes,
        lineDefs = wadData.lineDefs,
        nodes = wadData.nodes,
        subSectors = wadData.subSectors,
        segs = wadData.segs,
        things = wadData.things,
        player = wadData.player,
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
