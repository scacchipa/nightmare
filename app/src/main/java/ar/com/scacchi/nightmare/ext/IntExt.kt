package ar.com.scacchi.nightmare.ext

fun Int.normalize(div: Int): Int = ((this % div) + div) % div