package ar.com.scacchi.nightmare.data.endoom

import androidx.compose.ui.graphics.Color

class Letter {
    val letter: Char
    val primaryColor: Color
    val backgroundColor: Color
    val isBlink: Boolean

    constructor(letter: Char, primaryColor: Color, backgroundColor: Color, isBlink: Boolean) {
        this.letter = letter
        this.primaryColor = primaryColor
        this.backgroundColor = backgroundColor
        this.isBlink = isBlink
    }

    constructor(firstUByte: UByte, secondUByte: UByte) {
        this.letter = firstUByte.toInt().toChar()
        this.isBlink = secondUByte and 0b10_000_000u.toUByte() > 0u
        this.primaryColor = DOS_COLORS[
            (secondUByte and 0b00_000_111u).toInt() +
                    if ((secondUByte and 0b00_001_000u) > 0u) 8 else 0
        ]
        this.backgroundColor = DOS_COLORS[
            (secondUByte and 0b01_110_000u).toInt().rotateRight(4) +
                    if ((secondUByte and 0b00_001_000u) > 0u) 8 else 0
        ]
    }
}
