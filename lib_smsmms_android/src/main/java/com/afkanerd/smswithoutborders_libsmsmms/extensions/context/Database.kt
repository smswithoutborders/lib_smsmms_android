package com.afkanerd.smswithoutborders_libsmsmms.extensions.context

import android.content.Context
import com.afkanerd.smswithoutborders_libsmsmms.data.DatabaseImpl

fun Context.setDatabaseName(dbName: String) {
    DatabaseImpl.setDatabaseName(dbName)
}

fun Context.getDatabase(): DatabaseImpl {
    return DatabaseImpl.getDatabaseImpl(this)
}