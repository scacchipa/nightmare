package ar.com.scacchi.nightmare.ext

import ar.com.scacchi.nightmare.source.wad.NmColor

fun NmColor.light(lightLevel: Float): NmColor {
    return NmColor(
        red = (this.red.toFloat() * lightLevel).toUInt().toUByte(),
        green = (this.green.toFloat() * lightLevel).toUInt().toUByte(),
        blue = (this.blue.toFloat() * lightLevel).toUInt().toUByte(),
        alpha = this.alpha,
    )
}