package com.afkanerd.smswithoutborders_libsmsmms.data

import android.content.Context
import android.widget.Toast
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.afkanerd.lib_smsmms_android.R
import com.afkanerd.smswithoutborders_libsmsmms.data.Cryptography.getDatabasePassword
import com.afkanerd.smswithoutborders_libsmsmms.data.dao.ConversationsDao
import com.afkanerd.smswithoutborders_libsmsmms.data.dao.ThreadsDao
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Threads
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getNativesLoaded
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.setNativesLoaded
import com.afkanerd.smswithoutborders_libsmsmms.ui.viewModels.ThreadsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import kotlin.concurrent.Volatile
import kotlin.jvm.java


@Database(
    entities = [
        Conversations::class,
        Threads::class],
    version = 2,
    exportSchema = true,
    autoMigrations = []
)
abstract class DatabaseImpl : RoomDatabase() {
    abstract fun conversationsDao(): ConversationsDao?
    abstract fun threadsDao(): ThreadsDao?

    init {
        System.loadLibrary("sqlcipher")
    }

    companion object {
        @Volatile
        private var datastore: DatabaseImpl? = null
        private var databaseName: String = "afkanerd.smswithoutborders.libsmsmms.db"
        private var dbKeystoreAlias: String = "afkanerd.smswithoutborders.sms_mms_keystore_alias"

        @Synchronized
        fun setDatabaseName(databaseName: String) {
            this.databaseName = databaseName
        }

        @Synchronized
        fun getDatabaseImpl(context: Context): DatabaseImpl {
            if (datastore == null) {
                create(context)
            }
            return datastore!!
        }

        private fun create(context: Context) {
            getDatabasePassword(context, dbKeystoreAlias).use { password ->
                val databaseFile = context.getDatabasePath(databaseName)

                password.useRaw { rawBytes ->
                    datastore = Room.databaseBuilder(
                        context = context.applicationContext,
                        klass = DatabaseImpl::class.java,
                        databaseFile.absolutePath,
                    )
                        .openHelperFactory(SupportOpenHelperFactory(rawBytes))
                        .fallbackToDestructiveMigration(false)
                        .build()
                }
            }
        }
    }
}
