package ar.com.scacchi.nightmare.data.asset.texture

import ar.com.scacchi.nightmare.data.WadManager
import ar.com.scacchi.nightmare.data.asset.DoomBitmap
import ar.com.scacchi.nightmare.engine.ImageProvider

data class TextureMap(
    val name: String,
    val flags: Int,
    val width: UShort,
    val height: UShort,
    val columnDir: Int,  // unused
    val patchCount: UShort,
    val patchMapList: Array<PatchMap>,
) {
    operator fun get(idx: Int) = patchMapList[idx]

    fun buildDoomImage(imageProvider: ImageProvider, wadManager: WadManager): DoomBitmap = DoomBitmap(width.toInt(), height.toInt()).also {
        patchMapList.forEach { patchMap: PatchMap ->
            val pName: String = wadManager.pName[patchMap.pNameIndex.toInt()]
            it.print(
                xOffset = patchMap.xOffset.toInt(),
                yOffset = patchMap.yOffset.toInt(),
                bitmap = imageProvider.getPatchDoomBitmap(pName)
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextureMap

        if (flags != other.flags) return false
        if (columnDir != other.columnDir) return false
        if (name != other.name) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (patchCount != other.patchCount) return false
        if (!patchMapList.contentEquals(other.patchMapList)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = flags
        result = 31 * result + columnDir
        result = 31 * result + name.hashCode()
        result = 31 * result + width.hashCode()
        result = 31 * result + height.hashCode()
        result = 31 * result + patchCount.hashCode()
        result = 31 * result + patchMapList.contentHashCode()
        return result
    }
}