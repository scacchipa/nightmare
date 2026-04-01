package ar.com.scacchi.nightmare.source.wad.lump

import ar.com.scacchi.nightmare.source.wad.WadHeader
import java.nio.ByteBuffer


class Lump {
    companion object {
        const val thingsIndex = 1
        const val lineDefsIndex = 2
        const val sideDefsIndex = 3
        const val vertexesIndex = 4
        const val segsIndex = 5
        const val subsectorsIndex = 6
        const val nodesIndex = 7
        const val sectorsIndex = 8
        const val rejectIndex = 9
        const val blockMapIndex = 1
    }
}

val LINEDEF_FLAGS = mapOf(
    "BLOCKING" to 1.toUShort(),
    "BLOCK_MONSTERS" to 2.toUShort(),
    "TWO_SIDED" to 4.toUShort(),
    "DONT_PEG_TOP" to 8.toUShort(),
    "DONT_PEG_BOTTOM" to 16.toUShort(),
    "SECRET" to 32.toUShort(),
    "SOUND_BLOCK" to 64.toUShort(),
    "DONT_DRAW" to 128.toUShort(),
    "MAPPED" to 256.toUShort(),
)

class LumpDirectory(
    val lumpEntries: Array<FileLump>
) {val x = Lump.thingsIndex
    val patchStartDelimiterRegex = Regex("^P[0-9]?_START$")
    val patchEndDelimiterRegex = Regex("^P[0-9]?_END$")

    val spriteStartDelimiterRegex = Regex("^S[0-9]?_(START)$")
    val spriteEndDelimiterRegex = Regex("^S[0-9]?_(END)$")

    val flatStartDelimiterRegex = Regex("^F[0-9]?_(START)$")
    val flatEndDelimiterRegex = Regex("^F[0-9]?_(END)$")

    operator fun get(idx: Int) = lumpEntries[idx]

    operator fun get(pName: String) = this[getIdxForName(pName)].also {
        println("Search: $pName" )
    }

    fun getIdxForName(name: String): Int = lumpEntries.indexOfFirst { it.name == name }

    val patchListName:  List<String> by lazy {
        getLNameBetweenMarks(patchStartDelimiterRegex, patchEndDelimiterRegex)

    }
    val spriteListName:  List<String> by lazy {
        getLNameBetweenMarks(spriteStartDelimiterRegex, spriteEndDelimiterRegex)
    }

    val flatListName: List<String> by lazy {
        getLNameBetweenMarks(flatStartDelimiterRegex, flatEndDelimiterRegex)
    }

    private fun getLNameBetweenMarks(openRegex: Regex, closeRegex: Regex): List<String> {
        var nestLevel = 0
        return lumpEntries.filter { lEntry ->
            when {
                lEntry.name.matches(openRegex) -> { nestLevel += 1; false }
                lEntry.name.matches(closeRegex) -> { nestLevel -= 1; false }
                nestLevel > 0 -> true
                else -> false
            }
        }.map { it.name }
    }

    companion object {
        fun emptyDirectory(): LumpDirectory = LumpDirectory(emptyArray())
        fun createFrom(
            buffer: ByteBuffer,
            wadHeader: WadHeader
        ): LumpDirectory {
            buffer.position(wadHeader.infoTableOfs.toInt())
            return LumpDirectory(
                Array(wadHeader.numLumps.toInt()) {
                    FileLump.createFrom(buffer)
                }
            )
        }
    }
}
