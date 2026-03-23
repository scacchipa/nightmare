package ar.com.scacchi.nightmare.data

class PName(
    private val numMapPatches: Int,
    private val pName: Array<String>

) {
    operator fun get(idx: Int) = pName[idx]
}