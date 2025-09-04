package com.afkanerd.smswithoutborders_libsmsmms.`data`

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.afkanerd.smswithoutborders_libsmsmms.`data`.dao.ConversationsDao
import com.afkanerd.smswithoutborders_libsmsmms.`data`.dao.ConversationsDao_Impl
import com.afkanerd.smswithoutborders_libsmsmms.`data`.dao.ThreadsDao
import com.afkanerd.smswithoutborders_libsmsmms.`data`.dao.ThreadsDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class DatabaseImpl_Impl : DatabaseImpl() {
  private val _conversationsDao: Lazy<ConversationsDao> = lazy {
    ConversationsDao_Impl(this)
  }

  private val _threadsDao: Lazy<ThreadsDao> = lazy {
    ThreadsDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "3f6952b13cfccd8884e25b0765f3d216", "97a524f2c42be121830f1e5ec24f446b") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `Conversations` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `sms_data` BLOB, `mms_text` TEXT, `mms_content_uri` TEXT, `mms_mimetype` TEXT, `mms_filename` TEXT, `mms_filepath` TEXT, `_id` INTEGER, `thread_id` INTEGER, `address` TEXT, `person` TEXT, `date` INTEGER, `date_sent` INTEGER, `protocol` TEXT, `read` INTEGER, `status` INTEGER, `type` INTEGER, `reply_path_present` TEXT, `subject` TEXT, `body` TEXT, `service_center` TEXT, `locked` INTEGER, `sub_id` INTEGER, `error_code` INTEGER, `creator` TEXT, `seen` INTEGER, `mms__id` INTEGER, `mms_thread_id` INTEGER, `mms_date` INTEGER, `mms_date_sent` INTEGER, `mms_msg_box` INTEGER, `mms_read` INTEGER, `mms_m_id` TEXT, `mms_sub` TEXT, `mms_sub_cs` INTEGER, `mms_ct_t` TEXT, `mms_ct_l` TEXT, `mms_exp` TEXT, `mms_m_cls` TEXT, `mms_m_type` INTEGER, `mms_v` INTEGER, `mms_m_size` INTEGER, `mms_pri` INTEGER, `mms_rr` INTEGER, `mms_rpt_a` TEXT, `mms_resp_st` TEXT, `mms_st` TEXT, `mms_tr_id` TEXT, `mms_retr_st` TEXT, `mms_retr_txt` TEXT, `mms_retr_txt_cs` TEXT, `mms_read_status` TEXT, `mms_ct_cls` TEXT, `mms_resp_txt` TEXT, `mms_d_tm` TEXT, `mms_d_rpt` INTEGER, `mms_locked` INTEGER, `mms_sub_id` INTEGER, `mms_seen` INTEGER, `mms_creator` TEXT, `mms_text_only` INTEGER)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `Threads` (`threadId` INTEGER NOT NULL, `address` TEXT NOT NULL, `snippet` TEXT NOT NULL, `date` INTEGER NOT NULL, `type` INTEGER NOT NULL, `conversationId` INTEGER NOT NULL, `isMms` INTEGER NOT NULL, `isMute` INTEGER NOT NULL, `isArchive` INTEGER NOT NULL, `isBlocked` INTEGER NOT NULL, `unread` INTEGER NOT NULL, `unreadCount` INTEGER NOT NULL, PRIMARY KEY(`threadId`))")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_Threads_address` ON `Threads` (`address`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '3f6952b13cfccd8884e25b0765f3d216')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `Conversations`")
        connection.execSQL("DROP TABLE IF EXISTS `Threads`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsConversations: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsConversations.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("sms_data", TableInfo.Column("sms_data", "BLOB", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_text", TableInfo.Column("mms_text", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_content_uri", TableInfo.Column("mms_content_uri", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_mimetype", TableInfo.Column("mms_mimetype", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_filename", TableInfo.Column("mms_filename", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_filepath", TableInfo.Column("mms_filepath", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("_id", TableInfo.Column("_id", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("thread_id", TableInfo.Column("thread_id", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("address", TableInfo.Column("address", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("person", TableInfo.Column("person", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("date", TableInfo.Column("date", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("date_sent", TableInfo.Column("date_sent", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("protocol", TableInfo.Column("protocol", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("read", TableInfo.Column("read", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("status", TableInfo.Column("status", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("type", TableInfo.Column("type", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("reply_path_present", TableInfo.Column("reply_path_present",
            "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("subject", TableInfo.Column("subject", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("body", TableInfo.Column("body", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("service_center", TableInfo.Column("service_center", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("locked", TableInfo.Column("locked", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("sub_id", TableInfo.Column("sub_id", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("error_code", TableInfo.Column("error_code", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("creator", TableInfo.Column("creator", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("seen", TableInfo.Column("seen", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms__id", TableInfo.Column("mms__id", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_thread_id", TableInfo.Column("mms_thread_id", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_date", TableInfo.Column("mms_date", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_date_sent", TableInfo.Column("mms_date_sent", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_msg_box", TableInfo.Column("mms_msg_box", "INTEGER", false,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_read", TableInfo.Column("mms_read", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_m_id", TableInfo.Column("mms_m_id", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_sub", TableInfo.Column("mms_sub", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_sub_cs", TableInfo.Column("mms_sub_cs", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_ct_t", TableInfo.Column("mms_ct_t", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_ct_l", TableInfo.Column("mms_ct_l", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_exp", TableInfo.Column("mms_exp", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_m_cls", TableInfo.Column("mms_m_cls", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_m_type", TableInfo.Column("mms_m_type", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_v", TableInfo.Column("mms_v", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_m_size", TableInfo.Column("mms_m_size", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_pri", TableInfo.Column("mms_pri", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_rr", TableInfo.Column("mms_rr", "INTEGER", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_rpt_a", TableInfo.Column("mms_rpt_a", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_resp_st", TableInfo.Column("mms_resp_st", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_st", TableInfo.Column("mms_st", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_tr_id", TableInfo.Column("mms_tr_id", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_retr_st", TableInfo.Column("mms_retr_st", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_retr_txt", TableInfo.Column("mms_retr_txt", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_retr_txt_cs", TableInfo.Column("mms_retr_txt_cs", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_read_status", TableInfo.Column("mms_read_status", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_ct_cls", TableInfo.Column("mms_ct_cls", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_resp_txt", TableInfo.Column("mms_resp_txt", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_d_tm", TableInfo.Column("mms_d_tm", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_d_rpt", TableInfo.Column("mms_d_rpt", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_locked", TableInfo.Column("mms_locked", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_sub_id", TableInfo.Column("mms_sub_id", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_seen", TableInfo.Column("mms_seen", "INTEGER", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_creator", TableInfo.Column("mms_creator", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsConversations.put("mms_text_only", TableInfo.Column("mms_text_only", "INTEGER",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysConversations: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesConversations: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoConversations: TableInfo = TableInfo("Conversations", _columnsConversations,
            _foreignKeysConversations, _indicesConversations)
        val _existingConversations: TableInfo = read(connection, "Conversations")
        if (!_infoConversations.equals(_existingConversations)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |Conversations(com.afkanerd.smswithoutborders_libsmsmms.data.entities.Conversations).
              | Expected:
              |""".trimMargin() + _infoConversations + """
              |
              | Found:
              |""".trimMargin() + _existingConversations)
        }
        val _columnsThreads: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsThreads.put("threadId", TableInfo.Column("threadId", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsThreads.put("address", TableInfo.Column("address", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsThreads.put("snippet", TableInfo.Column("snippet", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsThreads.put("date", TableInfo.Column("date", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsThreads.put("type", TableInfo.Column("type", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsThreads.put("conversationId", TableInfo.Column("conversationId", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsThreads.put("isMms", TableInfo.Column("isMms", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsThreads.put("isMute", TableInfo.Column("isMute", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsThreads.put("isArchive", TableInfo.Column("isArchive", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsThreads.put("isBlocked", TableInfo.Column("isBlocked", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsThreads.put("unread", TableInfo.Column("unread", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsThreads.put("unreadCount", TableInfo.Column("unreadCount", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysThreads: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesThreads: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesThreads.add(TableInfo.Index("index_Threads_address", true, listOf("address"),
            listOf("ASC")))
        val _infoThreads: TableInfo = TableInfo("Threads", _columnsThreads, _foreignKeysThreads,
            _indicesThreads)
        val _existingThreads: TableInfo = read(connection, "Threads")
        if (!_infoThreads.equals(_existingThreads)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |Threads(com.afkanerd.smswithoutborders_libsmsmms.data.entities.Threads).
              | Expected:
              |""".trimMargin() + _infoThreads + """
              |
              | Found:
              |""".trimMargin() + _existingThreads)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "Conversations", "Threads")
  }

  public override fun clearAllTables() {
    super.performClear(false, "Conversations", "Threads")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(ConversationsDao::class, ConversationsDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ThreadsDao::class, ThreadsDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun conversationsDao(): ConversationsDao? = _conversationsDao.value

  public override fun threadsDao(): ThreadsDao? = _threadsDao.value
}
