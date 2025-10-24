package com.afkanerd.smswithoutborders_libsmsmms.extensions

import java.nio.ByteBuffer
import java.nio.ByteOrder

fun Short.toLittleEndianBytes(): ByteArray {
    return ByteBuffer.allocate(2)
        .order(ByteOrder.LITTLE_ENDIAN).putShort(this).array()
}
