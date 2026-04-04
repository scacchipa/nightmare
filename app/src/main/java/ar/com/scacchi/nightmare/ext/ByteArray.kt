package ar.com.scacchi.nightmare.ext


fun ByteArray.asString() = String(this.filter { it != 0.toByte() }.toByteArray())