package ar.com.scacchi.nightmare.data

import java.io.InputStream

class WadInfo {
    val identification: ByteArray
    val numLumps: Int
    val infoTableOfs: Int

    constructor(identification: ByteArray, numLumps: Int, infoTableOfs: Int) {
        this.identification = identification
        this.numLumps = numLumps
        this.infoTableOfs = infoTableOfs
    }

//    constructor(file: InputStream) {
//        val identArray = ByteArray(4)
//        file.read(identArray)
//        this.identification = identArray
//    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WadInfo

        if (numLumps != other.numLumps) return false
        if (infoTableOfs != other.infoTableOfs) return false
        if (!identification.contentEquals(other.identification)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = numLumps
        result = 31 * result + infoTableOfs
        result = 31 * result + identification.contentHashCode()
        return result
    }


}