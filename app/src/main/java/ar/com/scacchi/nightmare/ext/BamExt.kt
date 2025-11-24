package ar.com.scacchi.nightmare.ext

const val ANG_45: UShort = 0x2000u
const val ANG_90: UShort = 0x4000u
const val ANG_180: UShort = 0x8000u
const val ANG_MAX: UShort = 0xFFFFu

fun UShort.bamToRadian(): Float {
    return (this.toFloat() / UShort.MAX_VALUE.toFloat() * 2.0 * Math.PI).toFloat()
}

fun UShort.bamToDegree(): Float {
    return (this.toFloat() / UShort.MAX_VALUE.toFloat() * 360.0).toFloat()
}

fun Double.radianToBam(): UShort {
    return (this * UShort.MAX_VALUE.toDouble() / 2.0 / Math.PI).toUInt().toUShort()
}

fun Double.degreeToBam(): UShort {
    return (this * UShort.MAX_VALUE.toDouble() / 360.0).toUInt().toUShort()
}
