package com.afkanerd.smswithoutborders_libsmsmms.extensions

fun Short.toByteArray(): ByteArray {
    return byteArrayOf(
        (this.toInt() shr 8).toByte(),   // high byte
        (this.toInt() and 0xFF).toByte() // low byte
    )
}
