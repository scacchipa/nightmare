package ar.com.scacchi.nightmare.source.wad

import ar.com.scacchi.nightmare.ext.asString
import ar.com.scacchi.nightmare.source.wad.lump.data.PNamesLump

class PNames {
    private val pNames: Array<String>
    constructor(pNamesLump: PNamesLump) {
        pNames = Array(pNamesLump.numMapPatches) {
            pNamesLump.pName[it].asString().uppercase()
        }
    }

    operator fun get(idx: Int) = pNames[idx]
    fun size() = pNames.size

    fun indexOfPName(name: String): Int = pNames.indexOf(name)
}