package ar.com.scacchi.nightmare.source.wad.lump.data.texture

import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.data.readByteArrayAsString
import ar.com.scacchi.nightmare.data.readLittleEndianInt
import ar.com.scacchi.nightmare.data.readLittleEndianUShort
import ar.com.scacchi.nightmare.engine.ImageRepository
import ar.com.scacchi.nightmare.source.wad.WadManager
import java.nio.ByteBuffer

class TextureMap(
    val name: String,
    val flags: Int,
    val width: UShort,
    val height: UShort,
    val columnDir: Int,  // unused
    val patchCount: UShort,
    val patchMapList: Array<PatchMap>,
) {
    operator fun get(idx: Int) = patchMapList[idx]

    fun buildDoomImage(imageRepository: ImageRepository, wadManager: WadManager): DoomBitmap = DoomBitmap(width.toInt(), height.toInt()).also {
        patchMapList.forEach { patchMap: PatchMap ->
            val pName: String = wadManager.pNameLump[patchMap.pNameIndex.toInt()]
            it.print(
                xOffset = patchMap.xOffset.toInt(),
                yOffset = patchMap.yOffset.toInt(),
                bitmap = imageRepository.getPatchDoomBitmap(pName)
            )
        }
    }

    companion object {
        fun  readTextureMap(buffer: ByteBuffer, offset: Int): TextureMap {
            buffer.position(offset)

            val name = buffer.readByteArrayAsString(8)
            val flags = buffer.readLittleEndianInt()
            val width = buffer.readLittleEndianUShort()
            val height = buffer.readLittleEndianUShort()
            val columnDir = buffer.readLittleEndianInt()
            val patchCount = buffer.readLittleEndianUShort()
            val patchMapList = Array(patchCount.toInt()) {
                PatchMap.readPatchMap(buffer)
            }

            return TextureMap(
                name = name,
                flags = flags,
                width = width,
                height = height,
                columnDir = columnDir,
                patchCount = patchCount,
                patchMapList = patchMapList,
            )
        }
    }
}