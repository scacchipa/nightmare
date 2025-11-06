package ar.com.scacchi.nightmare.engine

import ar.com.scacchi.nightmare.data.FileLump
import ar.com.scacchi.nightmare.data.WadInfo
import java.nio.ByteBuffer

class WadData(
    val buffer: ByteBuffer,
    val wadInfo: WadInfo,
    val lumpDirectory: LumpDirectory,
    val vertexes: Array<Vertex>,
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

            val vertexes = Vertexes.createFrom(buffer, vertexesLump)


            return WadData(
                buffer = buffer,
                wadInfo = WadInfo.Companion.createFrom(buffer),
                lumpDirectory = lumpDirectory,
                vertexes = vertexes
            )
        }
    }
}

class Vertexes(
    val vertexes:  Array<Vertex>
) {
    companion object {
        fun createFrom(buffer: ByteBuffer, lump: FileLump): Array<Vertex> {
            buffer.position(lump.filePos.toInt())
            return Array(lump.size / 4) {
                Vertex.createFrom(buffer)
            }
        }
    }
}