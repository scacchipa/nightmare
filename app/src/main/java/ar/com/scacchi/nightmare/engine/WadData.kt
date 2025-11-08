package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.WadInfo
import java.nio.ByteBuffer

class WadData(
    val buffer: ByteBuffer,
    val wadInfo: WadInfo,
    val lumpDirectory: LumpDirectory,
    val vertexes: Vertexes,
    val lineDefs: LineDefs
) {

    companion object {
        fun create(
            buffer: ByteBuffer,
            nameMap: String = "E1M1"
        ): WadData {

            val wadInfo = WadInfo.Companion.createFrom(buffer)
            val lumpDirectory = LumpDirectory.createFrom(buffer, wadInfo)
            val idx = lumpDirectory.getIdxForName("E1M1")
            val vertexesLump = lumpDirectory[idx + (LUMP_INDICES["VERTEXES"] ?: 0)]
            val lineDefsLump = lumpDirectory[idx + (LUMP_INDICES["LINEDEFS"] ?: 0)]

            val vertexes = Vertexes.createFrom(buffer, vertexesLump)
            val lineDefs = LineDefs.createFrom(buffer, lineDefsLump)

            return WadData(
                buffer = buffer,
                wadInfo = WadInfo.Companion.createFrom(buffer),
                lumpDirectory = lumpDirectory,
                vertexes = vertexes,
                lineDefs = lineDefs
            )
        }
    }
}

