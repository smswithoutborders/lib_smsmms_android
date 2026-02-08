package com.afkanerd.smswithoutborders_libsmsmms.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.afkanerd.smswithoutborders_libsmsmms.data.dao.ConversationsDao
import com.afkanerd.smswithoutborders_libsmsmms.data.dao.ThreadsDao
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations
import com.afkanerd.smswithoutborders_libsmsmms.data.entities.Threads
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.setNativesLoaded
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import kotlin.concurrent.Volatile


@Database(
    entities = [
        Conversations::class,
        Threads::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
    ]
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
                datastore = create(context)
            }
            return datastore!!
        }

        private fun create(context: Context): DatabaseImpl {
            val password = Cryptography.getDatabasePassword(context, dbKeystoreAlias)

            val databaseFile = context.getDatabasePath(this.databaseName)
            val factory = SupportOpenHelperFactory(password)
            return Room.databaseBuilder(
                context, DatabaseImpl::class.java,
                databaseFile.absolutePath,
            )
                .addMigrations(Migrate1To2(context))
                .enableMultiInstanceInvalidation()
                .openHelperFactory(factory)
                .build()
        }
    }

    class Migrate1To2(private val context: Context) : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            super.migrate(db)
            context.setNativesLoaded(false)
        }
    }
}
