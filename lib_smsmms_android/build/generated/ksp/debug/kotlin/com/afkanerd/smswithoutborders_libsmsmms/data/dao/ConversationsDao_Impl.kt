package com.afkanerd.smswithoutborders_libsmsmms.`data`.dao

import androidx.paging.PagingSource
import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.RoomRawQuery
import androidx.room.paging.LimitOffsetPagingSource
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performBlocking
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.afkanerd.smswithoutborders_libsmsmms.`data`.`data`.models.SmsMmsNatives
import com.afkanerd.smswithoutborders_libsmsmms.`data`.entities.Conversations
import com.afkanerd.smswithoutborders_libsmsmms.`data`.entities.Threads
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ConversationsDao_Impl(
  __db: RoomDatabase,
) : ConversationsDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfConversations: EntityInsertAdapter<Conversations>

  private val __insertAdapterOfThreads: EntityInsertAdapter<Threads>

  private val __deleteAdapterOfConversations: EntityDeleteOrUpdateAdapter<Conversations>

  private val __updateAdapterOfConversations: EntityDeleteOrUpdateAdapter<Conversations>

  private val __updateAdapterOfThreads: EntityDeleteOrUpdateAdapter<Threads>
  init {
    this.__db = __db
    this.__insertAdapterOfConversations = object : EntityInsertAdapter<Conversations>() {
      protected override fun createQuery(): String =
          "INSERT OR ABORT INTO `Conversations` (`id`,`sms_data`,`mms_text`,`mms_content_uri`,`mms_mimetype`,`mms_filename`,`mms_filepath`,`_id`,`thread_id`,`address`,`person`,`date`,`date_sent`,`protocol`,`read`,`status`,`type`,`reply_path_present`,`subject`,`body`,`service_center`,`locked`,`sub_id`,`error_code`,`creator`,`seen`,`mms__id`,`mms_thread_id`,`mms_date`,`mms_date_sent`,`mms_msg_box`,`mms_read`,`mms_m_id`,`mms_sub`,`mms_sub_cs`,`mms_ct_t`,`mms_ct_l`,`mms_exp`,`mms_m_cls`,`mms_m_type`,`mms_v`,`mms_m_size`,`mms_pri`,`mms_rr`,`mms_rpt_a`,`mms_resp_st`,`mms_st`,`mms_tr_id`,`mms_retr_st`,`mms_retr_txt`,`mms_retr_txt_cs`,`mms_read_status`,`mms_ct_cls`,`mms_resp_txt`,`mms_d_tm`,`mms_d_rpt`,`mms_locked`,`mms_sub_id`,`mms_seen`,`mms_creator`,`mms_text_only`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Conversations) {
        statement.bindLong(1, entity.id)
        val _tmpSms_data: ByteArray? = entity.sms_data
        if (_tmpSms_data == null) {
          statement.bindNull(2)
        } else {
          statement.bindBlob(2, _tmpSms_data)
        }
        val _tmpMms_text: String? = entity.mms_text
        if (_tmpMms_text == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpMms_text)
        }
        val _tmpMms_content_uri: String? = entity.mms_content_uri
        if (_tmpMms_content_uri == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpMms_content_uri)
        }
        val _tmpMms_mimetype: String? = entity.mms_mimetype
        if (_tmpMms_mimetype == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpMms_mimetype)
        }
        val _tmpMms_filename: String? = entity.mms_filename
        if (_tmpMms_filename == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpMms_filename)
        }
        val _tmpMms_filepath: String? = entity.mms_filepath
        if (_tmpMms_filepath == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpMms_filepath)
        }
        val _tmpSms: SmsMmsNatives.Sms? = entity.sms
        if (_tmpSms != null) {
          val _tmp_id: Long? = _tmpSms._id
          if (_tmp_id == null) {
            statement.bindNull(8)
          } else {
            statement.bindLong(8, _tmp_id)
          }
          statement.bindLong(9, _tmpSms.thread_id.toLong())
          val _tmpAddress: String? = _tmpSms.address
          if (_tmpAddress == null) {
            statement.bindNull(10)
          } else {
            statement.bindText(10, _tmpAddress)
          }
          val _tmpPerson: String? = _tmpSms.person
          if (_tmpPerson == null) {
            statement.bindNull(11)
          } else {
            statement.bindText(11, _tmpPerson)
          }
          statement.bindLong(12, _tmpSms.date)
          statement.bindLong(13, _tmpSms.date_sent)
          val _tmpProtocol: String? = _tmpSms.protocol
          if (_tmpProtocol == null) {
            statement.bindNull(14)
          } else {
            statement.bindText(14, _tmpProtocol)
          }
          statement.bindLong(15, _tmpSms.read.toLong())
          statement.bindLong(16, _tmpSms.status.toLong())
          statement.bindLong(17, _tmpSms.type.toLong())
          val _tmpReply_path_present: String? = _tmpSms.reply_path_present
          if (_tmpReply_path_present == null) {
            statement.bindNull(18)
          } else {
            statement.bindText(18, _tmpReply_path_present)
          }
          val _tmpSubject: String? = _tmpSms.subject
          if (_tmpSubject == null) {
            statement.bindNull(19)
          } else {
            statement.bindText(19, _tmpSubject)
          }
          val _tmpBody: String? = _tmpSms.body
          if (_tmpBody == null) {
            statement.bindNull(20)
          } else {
            statement.bindText(20, _tmpBody)
          }
          val _tmpService_center: String? = _tmpSms.service_center
          if (_tmpService_center == null) {
            statement.bindNull(21)
          } else {
            statement.bindText(21, _tmpService_center)
          }
          val _tmpLocked: Int? = _tmpSms.locked
          if (_tmpLocked == null) {
            statement.bindNull(22)
          } else {
            statement.bindLong(22, _tmpLocked.toLong())
          }
          statement.bindLong(23, _tmpSms.sub_id)
          val _tmpError_code: Int? = _tmpSms.error_code
          if (_tmpError_code == null) {
            statement.bindNull(24)
          } else {
            statement.bindLong(24, _tmpError_code.toLong())
          }
          val _tmpCreator: String? = _tmpSms.creator
          if (_tmpCreator == null) {
            statement.bindNull(25)
          } else {
            statement.bindText(25, _tmpCreator)
          }
          val _tmpSeen: Int? = _tmpSms.seen
          if (_tmpSeen == null) {
            statement.bindNull(26)
          } else {
            statement.bindLong(26, _tmpSeen.toLong())
          }
        } else {
          statement.bindNull(8)
          statement.bindNull(9)
          statement.bindNull(10)
          statement.bindNull(11)
          statement.bindNull(12)
          statement.bindNull(13)
          statement.bindNull(14)
          statement.bindNull(15)
          statement.bindNull(16)
          statement.bindNull(17)
          statement.bindNull(18)
          statement.bindNull(19)
          statement.bindNull(20)
          statement.bindNull(21)
          statement.bindNull(22)
          statement.bindNull(23)
          statement.bindNull(24)
          statement.bindNull(25)
          statement.bindNull(26)
        }
        val _tmpMms: SmsMmsNatives.Mms? = entity.mms
        if (_tmpMms != null) {
          statement.bindLong(27, _tmpMms._id)
          statement.bindLong(28, _tmpMms.thread_id.toLong())
          statement.bindLong(29, _tmpMms.date)
          statement.bindLong(30, _tmpMms.date_sent)
          statement.bindLong(31, _tmpMms.msg_box.toLong())
          val _tmpRead: Int? = _tmpMms.read
          if (_tmpRead == null) {
            statement.bindNull(32)
          } else {
            statement.bindLong(32, _tmpRead.toLong())
          }
          val _tmpM_id: String? = _tmpMms.m_id
          if (_tmpM_id == null) {
            statement.bindNull(33)
          } else {
            statement.bindText(33, _tmpM_id)
          }
          val _tmpSub: String? = _tmpMms.sub
          if (_tmpSub == null) {
            statement.bindNull(34)
          } else {
            statement.bindText(34, _tmpSub)
          }
          val _tmpSub_cs: Int? = _tmpMms.sub_cs
          if (_tmpSub_cs == null) {
            statement.bindNull(35)
          } else {
            statement.bindLong(35, _tmpSub_cs.toLong())
          }
          val _tmpCt_t: String? = _tmpMms.ct_t
          if (_tmpCt_t == null) {
            statement.bindNull(36)
          } else {
            statement.bindText(36, _tmpCt_t)
          }
          val _tmpCt_l: String? = _tmpMms.ct_l
          if (_tmpCt_l == null) {
            statement.bindNull(37)
          } else {
            statement.bindText(37, _tmpCt_l)
          }
          val _tmpExp: String? = _tmpMms.exp
          if (_tmpExp == null) {
            statement.bindNull(38)
          } else {
            statement.bindText(38, _tmpExp)
          }
          val _tmpM_cls: String? = _tmpMms.m_cls
          if (_tmpM_cls == null) {
            statement.bindNull(39)
          } else {
            statement.bindText(39, _tmpM_cls)
          }
          val _tmpM_type: Int? = _tmpMms.m_type
          if (_tmpM_type == null) {
            statement.bindNull(40)
          } else {
            statement.bindLong(40, _tmpM_type.toLong())
          }
          val _tmpV: Int? = _tmpMms.v
          if (_tmpV == null) {
            statement.bindNull(41)
          } else {
            statement.bindLong(41, _tmpV.toLong())
          }
          val _tmpM_size: Int? = _tmpMms.m_size
          if (_tmpM_size == null) {
            statement.bindNull(42)
          } else {
            statement.bindLong(42, _tmpM_size.toLong())
          }
          val _tmpPri: Int? = _tmpMms.pri
          if (_tmpPri == null) {
            statement.bindNull(43)
          } else {
            statement.bindLong(43, _tmpPri.toLong())
          }
          val _tmpRr: Int? = _tmpMms.rr
          if (_tmpRr == null) {
            statement.bindNull(44)
          } else {
            statement.bindLong(44, _tmpRr.toLong())
          }
          val _tmpRpt_a: String? = _tmpMms.rpt_a
          if (_tmpRpt_a == null) {
            statement.bindNull(45)
          } else {
            statement.bindText(45, _tmpRpt_a)
          }
          val _tmpResp_st: String? = _tmpMms.resp_st
          if (_tmpResp_st == null) {
            statement.bindNull(46)
          } else {
            statement.bindText(46, _tmpResp_st)
          }
          val _tmpSt: String? = _tmpMms.st
          if (_tmpSt == null) {
            statement.bindNull(47)
          } else {
            statement.bindText(47, _tmpSt)
          }
          val _tmpTr_id: String? = _tmpMms.tr_id
          if (_tmpTr_id == null) {
            statement.bindNull(48)
          } else {
            statement.bindText(48, _tmpTr_id)
          }
          val _tmpRetr_st: String? = _tmpMms.retr_st
          if (_tmpRetr_st == null) {
            statement.bindNull(49)
          } else {
            statement.bindText(49, _tmpRetr_st)
          }
          val _tmpRetr_txt: String? = _tmpMms.retr_txt
          if (_tmpRetr_txt == null) {
            statement.bindNull(50)
          } else {
            statement.bindText(50, _tmpRetr_txt)
          }
          val _tmpRetr_txt_cs: String? = _tmpMms.retr_txt_cs
          if (_tmpRetr_txt_cs == null) {
            statement.bindNull(51)
          } else {
            statement.bindText(51, _tmpRetr_txt_cs)
          }
          val _tmpRead_status: String? = _tmpMms.read_status
          if (_tmpRead_status == null) {
            statement.bindNull(52)
          } else {
            statement.bindText(52, _tmpRead_status)
          }
          val _tmpCt_cls: String? = _tmpMms.ct_cls
          if (_tmpCt_cls == null) {
            statement.bindNull(53)
          } else {
            statement.bindText(53, _tmpCt_cls)
          }
          val _tmpResp_txt: String? = _tmpMms.resp_txt
          if (_tmpResp_txt == null) {
            statement.bindNull(54)
          } else {
            statement.bindText(54, _tmpResp_txt)
          }
          val _tmpD_tm: String? = _tmpMms.d_tm
          if (_tmpD_tm == null) {
            statement.bindNull(55)
          } else {
            statement.bindText(55, _tmpD_tm)
          }
          val _tmpD_rpt: Int? = _tmpMms.d_rpt
          if (_tmpD_rpt == null) {
            statement.bindNull(56)
          } else {
            statement.bindLong(56, _tmpD_rpt.toLong())
          }
          val _tmpLocked_1: Int? = _tmpMms.locked
          if (_tmpLocked_1 == null) {
            statement.bindNull(57)
          } else {
            statement.bindLong(57, _tmpLocked_1.toLong())
          }
          val _tmpSub_id: Long? = _tmpMms.sub_id
          if (_tmpSub_id == null) {
            statement.bindNull(58)
          } else {
            statement.bindLong(58, _tmpSub_id)
          }
          val _tmpSeen_1: Int? = _tmpMms.seen
          if (_tmpSeen_1 == null) {
            statement.bindNull(59)
          } else {
            statement.bindLong(59, _tmpSeen_1.toLong())
          }
          val _tmpCreator_1: String? = _tmpMms.creator
          if (_tmpCreator_1 == null) {
            statement.bindNull(60)
          } else {
            statement.bindText(60, _tmpCreator_1)
          }
          val _tmpText_only: Int? = _tmpMms.text_only
          if (_tmpText_only == null) {
            statement.bindNull(61)
          } else {
            statement.bindLong(61, _tmpText_only.toLong())
          }
        } else {
          statement.bindNull(27)
          statement.bindNull(28)
          statement.bindNull(29)
          statement.bindNull(30)
          statement.bindNull(31)
          statement.bindNull(32)
          statement.bindNull(33)
          statement.bindNull(34)
          statement.bindNull(35)
          statement.bindNull(36)
          statement.bindNull(37)
          statement.bindNull(38)
          statement.bindNull(39)
          statement.bindNull(40)
          statement.bindNull(41)
          statement.bindNull(42)
          statement.bindNull(43)
          statement.bindNull(44)
          statement.bindNull(45)
          statement.bindNull(46)
          statement.bindNull(47)
          statement.bindNull(48)
          statement.bindNull(49)
          statement.bindNull(50)
          statement.bindNull(51)
          statement.bindNull(52)
          statement.bindNull(53)
          statement.bindNull(54)
          statement.bindNull(55)
          statement.bindNull(56)
          statement.bindNull(57)
          statement.bindNull(58)
          statement.bindNull(59)
          statement.bindNull(60)
          statement.bindNull(61)
        }
      }
    }
    this.__insertAdapterOfThreads = object : EntityInsertAdapter<Threads>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `Threads` (`threadId`,`address`,`snippet`,`date`,`type`,`conversationId`,`isMms`,`isMute`,`isArchive`,`isBlocked`,`unread`,`unreadCount`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Threads) {
        statement.bindLong(1, entity.threadId.toLong())
        statement.bindText(2, entity.address)
        statement.bindText(3, entity.snippet)
        statement.bindLong(4, entity.date)
        statement.bindLong(5, entity.type.toLong())
        statement.bindLong(6, entity.conversationId)
        val _tmp: Int = if (entity.isMms) 1 else 0
        statement.bindLong(7, _tmp.toLong())
        val _tmp_1: Int = if (entity.isMute) 1 else 0
        statement.bindLong(8, _tmp_1.toLong())
        val _tmp_2: Int = if (entity.isArchive) 1 else 0
        statement.bindLong(9, _tmp_2.toLong())
        val _tmp_3: Int = if (entity.isBlocked) 1 else 0
        statement.bindLong(10, _tmp_3.toLong())
        val _tmp_4: Int = if (entity.unread) 1 else 0
        statement.bindLong(11, _tmp_4.toLong())
        statement.bindLong(12, entity.unreadCount.toLong())
      }
    }
    this.__deleteAdapterOfConversations = object : EntityDeleteOrUpdateAdapter<Conversations>() {
      protected override fun createQuery(): String = "DELETE FROM `Conversations` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Conversations) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfConversations = object : EntityDeleteOrUpdateAdapter<Conversations>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `Conversations` SET `id` = ?,`sms_data` = ?,`mms_text` = ?,`mms_content_uri` = ?,`mms_mimetype` = ?,`mms_filename` = ?,`mms_filepath` = ?,`_id` = ?,`thread_id` = ?,`address` = ?,`person` = ?,`date` = ?,`date_sent` = ?,`protocol` = ?,`read` = ?,`status` = ?,`type` = ?,`reply_path_present` = ?,`subject` = ?,`body` = ?,`service_center` = ?,`locked` = ?,`sub_id` = ?,`error_code` = ?,`creator` = ?,`seen` = ?,`mms__id` = ?,`mms_thread_id` = ?,`mms_date` = ?,`mms_date_sent` = ?,`mms_msg_box` = ?,`mms_read` = ?,`mms_m_id` = ?,`mms_sub` = ?,`mms_sub_cs` = ?,`mms_ct_t` = ?,`mms_ct_l` = ?,`mms_exp` = ?,`mms_m_cls` = ?,`mms_m_type` = ?,`mms_v` = ?,`mms_m_size` = ?,`mms_pri` = ?,`mms_rr` = ?,`mms_rpt_a` = ?,`mms_resp_st` = ?,`mms_st` = ?,`mms_tr_id` = ?,`mms_retr_st` = ?,`mms_retr_txt` = ?,`mms_retr_txt_cs` = ?,`mms_read_status` = ?,`mms_ct_cls` = ?,`mms_resp_txt` = ?,`mms_d_tm` = ?,`mms_d_rpt` = ?,`mms_locked` = ?,`mms_sub_id` = ?,`mms_seen` = ?,`mms_creator` = ?,`mms_text_only` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Conversations) {
        statement.bindLong(1, entity.id)
        val _tmpSms_data: ByteArray? = entity.sms_data
        if (_tmpSms_data == null) {
          statement.bindNull(2)
        } else {
          statement.bindBlob(2, _tmpSms_data)
        }
        val _tmpMms_text: String? = entity.mms_text
        if (_tmpMms_text == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpMms_text)
        }
        val _tmpMms_content_uri: String? = entity.mms_content_uri
        if (_tmpMms_content_uri == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpMms_content_uri)
        }
        val _tmpMms_mimetype: String? = entity.mms_mimetype
        if (_tmpMms_mimetype == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpMms_mimetype)
        }
        val _tmpMms_filename: String? = entity.mms_filename
        if (_tmpMms_filename == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpMms_filename)
        }
        val _tmpMms_filepath: String? = entity.mms_filepath
        if (_tmpMms_filepath == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpMms_filepath)
        }
        val _tmpSms: SmsMmsNatives.Sms? = entity.sms
        if (_tmpSms != null) {
          val _tmp_id: Long? = _tmpSms._id
          if (_tmp_id == null) {
            statement.bindNull(8)
          } else {
            statement.bindLong(8, _tmp_id)
          }
          statement.bindLong(9, _tmpSms.thread_id.toLong())
          val _tmpAddress: String? = _tmpSms.address
          if (_tmpAddress == null) {
            statement.bindNull(10)
          } else {
            statement.bindText(10, _tmpAddress)
          }
          val _tmpPerson: String? = _tmpSms.person
          if (_tmpPerson == null) {
            statement.bindNull(11)
          } else {
            statement.bindText(11, _tmpPerson)
          }
          statement.bindLong(12, _tmpSms.date)
          statement.bindLong(13, _tmpSms.date_sent)
          val _tmpProtocol: String? = _tmpSms.protocol
          if (_tmpProtocol == null) {
            statement.bindNull(14)
          } else {
            statement.bindText(14, _tmpProtocol)
          }
          statement.bindLong(15, _tmpSms.read.toLong())
          statement.bindLong(16, _tmpSms.status.toLong())
          statement.bindLong(17, _tmpSms.type.toLong())
          val _tmpReply_path_present: String? = _tmpSms.reply_path_present
          if (_tmpReply_path_present == null) {
            statement.bindNull(18)
          } else {
            statement.bindText(18, _tmpReply_path_present)
          }
          val _tmpSubject: String? = _tmpSms.subject
          if (_tmpSubject == null) {
            statement.bindNull(19)
          } else {
            statement.bindText(19, _tmpSubject)
          }
          val _tmpBody: String? = _tmpSms.body
          if (_tmpBody == null) {
            statement.bindNull(20)
          } else {
            statement.bindText(20, _tmpBody)
          }
          val _tmpService_center: String? = _tmpSms.service_center
          if (_tmpService_center == null) {
            statement.bindNull(21)
          } else {
            statement.bindText(21, _tmpService_center)
          }
          val _tmpLocked: Int? = _tmpSms.locked
          if (_tmpLocked == null) {
            statement.bindNull(22)
          } else {
            statement.bindLong(22, _tmpLocked.toLong())
          }
          statement.bindLong(23, _tmpSms.sub_id)
          val _tmpError_code: Int? = _tmpSms.error_code
          if (_tmpError_code == null) {
            statement.bindNull(24)
          } else {
            statement.bindLong(24, _tmpError_code.toLong())
          }
          val _tmpCreator: String? = _tmpSms.creator
          if (_tmpCreator == null) {
            statement.bindNull(25)
          } else {
            statement.bindText(25, _tmpCreator)
          }
          val _tmpSeen: Int? = _tmpSms.seen
          if (_tmpSeen == null) {
            statement.bindNull(26)
          } else {
            statement.bindLong(26, _tmpSeen.toLong())
          }
        } else {
          statement.bindNull(8)
          statement.bindNull(9)
          statement.bindNull(10)
          statement.bindNull(11)
          statement.bindNull(12)
          statement.bindNull(13)
          statement.bindNull(14)
          statement.bindNull(15)
          statement.bindNull(16)
          statement.bindNull(17)
          statement.bindNull(18)
          statement.bindNull(19)
          statement.bindNull(20)
          statement.bindNull(21)
          statement.bindNull(22)
          statement.bindNull(23)
          statement.bindNull(24)
          statement.bindNull(25)
          statement.bindNull(26)
        }
        val _tmpMms: SmsMmsNatives.Mms? = entity.mms
        if (_tmpMms != null) {
          statement.bindLong(27, _tmpMms._id)
          statement.bindLong(28, _tmpMms.thread_id.toLong())
          statement.bindLong(29, _tmpMms.date)
          statement.bindLong(30, _tmpMms.date_sent)
          statement.bindLong(31, _tmpMms.msg_box.toLong())
          val _tmpRead: Int? = _tmpMms.read
          if (_tmpRead == null) {
            statement.bindNull(32)
          } else {
            statement.bindLong(32, _tmpRead.toLong())
          }
          val _tmpM_id: String? = _tmpMms.m_id
          if (_tmpM_id == null) {
            statement.bindNull(33)
          } else {
            statement.bindText(33, _tmpM_id)
          }
          val _tmpSub: String? = _tmpMms.sub
          if (_tmpSub == null) {
            statement.bindNull(34)
          } else {
            statement.bindText(34, _tmpSub)
          }
          val _tmpSub_cs: Int? = _tmpMms.sub_cs
          if (_tmpSub_cs == null) {
            statement.bindNull(35)
          } else {
            statement.bindLong(35, _tmpSub_cs.toLong())
          }
          val _tmpCt_t: String? = _tmpMms.ct_t
          if (_tmpCt_t == null) {
            statement.bindNull(36)
          } else {
            statement.bindText(36, _tmpCt_t)
          }
          val _tmpCt_l: String? = _tmpMms.ct_l
          if (_tmpCt_l == null) {
            statement.bindNull(37)
          } else {
            statement.bindText(37, _tmpCt_l)
          }
          val _tmpExp: String? = _tmpMms.exp
          if (_tmpExp == null) {
            statement.bindNull(38)
          } else {
            statement.bindText(38, _tmpExp)
          }
          val _tmpM_cls: String? = _tmpMms.m_cls
          if (_tmpM_cls == null) {
            statement.bindNull(39)
          } else {
            statement.bindText(39, _tmpM_cls)
          }
          val _tmpM_type: Int? = _tmpMms.m_type
          if (_tmpM_type == null) {
            statement.bindNull(40)
          } else {
            statement.bindLong(40, _tmpM_type.toLong())
          }
          val _tmpV: Int? = _tmpMms.v
          if (_tmpV == null) {
            statement.bindNull(41)
          } else {
            statement.bindLong(41, _tmpV.toLong())
          }
          val _tmpM_size: Int? = _tmpMms.m_size
          if (_tmpM_size == null) {
            statement.bindNull(42)
          } else {
            statement.bindLong(42, _tmpM_size.toLong())
          }
          val _tmpPri: Int? = _tmpMms.pri
          if (_tmpPri == null) {
            statement.bindNull(43)
          } else {
            statement.bindLong(43, _tmpPri.toLong())
          }
          val _tmpRr: Int? = _tmpMms.rr
          if (_tmpRr == null) {
            statement.bindNull(44)
          } else {
            statement.bindLong(44, _tmpRr.toLong())
          }
          val _tmpRpt_a: String? = _tmpMms.rpt_a
          if (_tmpRpt_a == null) {
            statement.bindNull(45)
          } else {
            statement.bindText(45, _tmpRpt_a)
          }
          val _tmpResp_st: String? = _tmpMms.resp_st
          if (_tmpResp_st == null) {
            statement.bindNull(46)
          } else {
            statement.bindText(46, _tmpResp_st)
          }
          val _tmpSt: String? = _tmpMms.st
          if (_tmpSt == null) {
            statement.bindNull(47)
          } else {
            statement.bindText(47, _tmpSt)
          }
          val _tmpTr_id: String? = _tmpMms.tr_id
          if (_tmpTr_id == null) {
            statement.bindNull(48)
          } else {
            statement.bindText(48, _tmpTr_id)
          }
          val _tmpRetr_st: String? = _tmpMms.retr_st
          if (_tmpRetr_st == null) {
            statement.bindNull(49)
          } else {
            statement.bindText(49, _tmpRetr_st)
          }
          val _tmpRetr_txt: String? = _tmpMms.retr_txt
          if (_tmpRetr_txt == null) {
            statement.bindNull(50)
          } else {
            statement.bindText(50, _tmpRetr_txt)
          }
          val _tmpRetr_txt_cs: String? = _tmpMms.retr_txt_cs
          if (_tmpRetr_txt_cs == null) {
            statement.bindNull(51)
          } else {
            statement.bindText(51, _tmpRetr_txt_cs)
          }
          val _tmpRead_status: String? = _tmpMms.read_status
          if (_tmpRead_status == null) {
            statement.bindNull(52)
          } else {
            statement.bindText(52, _tmpRead_status)
          }
          val _tmpCt_cls: String? = _tmpMms.ct_cls
          if (_tmpCt_cls == null) {
            statement.bindNull(53)
          } else {
            statement.bindText(53, _tmpCt_cls)
          }
          val _tmpResp_txt: String? = _tmpMms.resp_txt
          if (_tmpResp_txt == null) {
            statement.bindNull(54)
          } else {
            statement.bindText(54, _tmpResp_txt)
          }
          val _tmpD_tm: String? = _tmpMms.d_tm
          if (_tmpD_tm == null) {
            statement.bindNull(55)
          } else {
            statement.bindText(55, _tmpD_tm)
          }
          val _tmpD_rpt: Int? = _tmpMms.d_rpt
          if (_tmpD_rpt == null) {
            statement.bindNull(56)
          } else {
            statement.bindLong(56, _tmpD_rpt.toLong())
          }
          val _tmpLocked_1: Int? = _tmpMms.locked
          if (_tmpLocked_1 == null) {
            statement.bindNull(57)
          } else {
            statement.bindLong(57, _tmpLocked_1.toLong())
          }
          val _tmpSub_id: Long? = _tmpMms.sub_id
          if (_tmpSub_id == null) {
            statement.bindNull(58)
          } else {
            statement.bindLong(58, _tmpSub_id)
          }
          val _tmpSeen_1: Int? = _tmpMms.seen
          if (_tmpSeen_1 == null) {
            statement.bindNull(59)
          } else {
            statement.bindLong(59, _tmpSeen_1.toLong())
          }
          val _tmpCreator_1: String? = _tmpMms.creator
          if (_tmpCreator_1 == null) {
            statement.bindNull(60)
          } else {
            statement.bindText(60, _tmpCreator_1)
          }
          val _tmpText_only: Int? = _tmpMms.text_only
          if (_tmpText_only == null) {
            statement.bindNull(61)
          } else {
            statement.bindLong(61, _tmpText_only.toLong())
          }
        } else {
          statement.bindNull(27)
          statement.bindNull(28)
          statement.bindNull(29)
          statement.bindNull(30)
          statement.bindNull(31)
          statement.bindNull(32)
          statement.bindNull(33)
          statement.bindNull(34)
          statement.bindNull(35)
          statement.bindNull(36)
          statement.bindNull(37)
          statement.bindNull(38)
          statement.bindNull(39)
          statement.bindNull(40)
          statement.bindNull(41)
          statement.bindNull(42)
          statement.bindNull(43)
          statement.bindNull(44)
          statement.bindNull(45)
          statement.bindNull(46)
          statement.bindNull(47)
          statement.bindNull(48)
          statement.bindNull(49)
          statement.bindNull(50)
          statement.bindNull(51)
          statement.bindNull(52)
          statement.bindNull(53)
          statement.bindNull(54)
          statement.bindNull(55)
          statement.bindNull(56)
          statement.bindNull(57)
          statement.bindNull(58)
          statement.bindNull(59)
          statement.bindNull(60)
          statement.bindNull(61)
        }
        statement.bindLong(62, entity.id)
      }
    }
    this.__updateAdapterOfThreads = object : EntityDeleteOrUpdateAdapter<Threads>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `Threads` SET `threadId` = ?,`address` = ?,`snippet` = ?,`date` = ?,`type` = ?,`conversationId` = ?,`isMms` = ?,`isMute` = ?,`isArchive` = ?,`isBlocked` = ?,`unread` = ?,`unreadCount` = ? WHERE `threadId` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Threads) {
        statement.bindLong(1, entity.threadId.toLong())
        statement.bindText(2, entity.address)
        statement.bindText(3, entity.snippet)
        statement.bindLong(4, entity.date)
        statement.bindLong(5, entity.type.toLong())
        statement.bindLong(6, entity.conversationId)
        val _tmp: Int = if (entity.isMms) 1 else 0
        statement.bindLong(7, _tmp.toLong())
        val _tmp_1: Int = if (entity.isMute) 1 else 0
        statement.bindLong(8, _tmp_1.toLong())
        val _tmp_2: Int = if (entity.isArchive) 1 else 0
        statement.bindLong(9, _tmp_2.toLong())
        val _tmp_3: Int = if (entity.isBlocked) 1 else 0
        statement.bindLong(10, _tmp_3.toLong())
        val _tmp_4: Int = if (entity.unread) 1 else 0
        statement.bindLong(11, _tmp_4.toLong())
        statement.bindLong(12, entity.unreadCount.toLong())
        statement.bindLong(13, entity.threadId.toLong())
      }
    }
  }

  public override fun insertConversation(conversation: Conversations): Long = performBlocking(__db,
      false, true) { _connection ->
    val _result: Long = __insertAdapterOfConversations.insertAndReturnId(_connection, conversation)
    _result
  }

  public override fun insertConversations(conversation: List<Conversations>): Unit =
      performBlocking(__db, false, true) { _connection ->
    __insertAdapterOfConversations.insert(_connection, conversation)
  }

  public override fun insertThread(thread: Threads): Unit = performBlocking(__db, false, true) {
      _connection ->
    __insertAdapterOfThreads.insert(_connection, thread)
  }

  public override fun insertThreads(thread: List<Threads>): Unit = performBlocking(__db, false,
      true) { _connection ->
    __insertAdapterOfThreads.insert(_connection, thread)
  }

  public override fun deleteConversation(conversations: Conversations): Unit = performBlocking(__db,
      false, true) { _connection ->
    __deleteAdapterOfConversations.handle(_connection, conversations)
  }

  public override fun deleteConversations(conversations: List<Conversations>): Unit =
      performBlocking(__db, false, true) { _connection ->
    __deleteAdapterOfConversations.handleMultiple(_connection, conversations)
  }

  public override fun updateConversation(conversations: Conversations): Unit = performBlocking(__db,
      false, true) { _connection ->
    __updateAdapterOfConversations.handle(_connection, conversations)
  }

  public override fun updateThread(thread: Threads): Unit = performBlocking(__db, false, true) {
      _connection ->
    __updateAdapterOfThreads.handle(_connection, thread)
  }

  public override fun update(conversations: MutableList<Conversations>): Int = performBlocking(__db,
      false, true) { _connection ->
    var _result: Int = 0
    _result += __updateAdapterOfConversations.handleMultiple(_connection, conversations)
    _result
  }

  public override fun update(conversation: Conversations): Unit = performBlocking(__db, false, true)
      { _ ->
    super@ConversationsDao_Impl.update(conversation)
  }

  public override fun insert(conversation: Conversations, removeArchive: Boolean): Long =
      performBlocking(__db, false, true) { _ ->
    super@ConversationsDao_Impl.insert(conversation, removeArchive)
  }

  public override fun insertAll(conversationsList: List<Conversations>, deleteDb: Boolean): Unit =
      performBlocking(__db, false, true) { _ ->
    super@ConversationsDao_Impl.insertAll(conversationsList, deleteDb)
  }

  public override fun delete(conversation: Conversations, removeArchive: Boolean): Unit =
      performBlocking(__db, false, true) { _ ->
    super@ConversationsDao_Impl.delete(conversation, removeArchive)
  }

  public override fun delete(conversations: List<Conversations>, removeArchive: Boolean): Unit =
      performBlocking(__db, false, true) { _ ->
    super@ConversationsDao_Impl.delete(conversations, removeArchive)
  }

  public override fun getConversation(id: Long): Conversations? {
    val _sql: String = "SELECT * FROM Conversations WHERE Conversations.id = ?"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfSmsData: Int = getColumnIndexOrThrow(_stmt, "sms_data")
        val _columnIndexOfMmsText: Int = getColumnIndexOrThrow(_stmt, "mms_text")
        val _columnIndexOfMmsContentUri: Int = getColumnIndexOrThrow(_stmt, "mms_content_uri")
        val _columnIndexOfMmsMimetype: Int = getColumnIndexOrThrow(_stmt, "mms_mimetype")
        val _columnIndexOfMmsFilename: Int = getColumnIndexOrThrow(_stmt, "mms_filename")
        val _columnIndexOfMmsFilepath: Int = getColumnIndexOrThrow(_stmt, "mms_filepath")
        val _columnIndexOfId_1: Int = getColumnIndexOrThrow(_stmt, "_id")
        val _columnIndexOfThreadId: Int = getColumnIndexOrThrow(_stmt, "thread_id")
        val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
        val _columnIndexOfPerson: Int = getColumnIndexOrThrow(_stmt, "person")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfDateSent: Int = getColumnIndexOrThrow(_stmt, "date_sent")
        val _columnIndexOfProtocol: Int = getColumnIndexOrThrow(_stmt, "protocol")
        val _columnIndexOfRead: Int = getColumnIndexOrThrow(_stmt, "read")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfReplyPathPresent: Int = getColumnIndexOrThrow(_stmt, "reply_path_present")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfServiceCenter: Int = getColumnIndexOrThrow(_stmt, "service_center")
        val _columnIndexOfLocked: Int = getColumnIndexOrThrow(_stmt, "locked")
        val _columnIndexOfSubId: Int = getColumnIndexOrThrow(_stmt, "sub_id")
        val _columnIndexOfErrorCode: Int = getColumnIndexOrThrow(_stmt, "error_code")
        val _columnIndexOfCreator: Int = getColumnIndexOrThrow(_stmt, "creator")
        val _columnIndexOfSeen: Int = getColumnIndexOrThrow(_stmt, "seen")
        val _columnIndexOfId_2: Int = getColumnIndexOrThrow(_stmt, "mms__id")
        val _columnIndexOfThreadId_1: Int = getColumnIndexOrThrow(_stmt, "mms_thread_id")
        val _columnIndexOfDate_1: Int = getColumnIndexOrThrow(_stmt, "mms_date")
        val _columnIndexOfDateSent_1: Int = getColumnIndexOrThrow(_stmt, "mms_date_sent")
        val _columnIndexOfMsgBox: Int = getColumnIndexOrThrow(_stmt, "mms_msg_box")
        val _columnIndexOfRead_1: Int = getColumnIndexOrThrow(_stmt, "mms_read")
        val _columnIndexOfMId: Int = getColumnIndexOrThrow(_stmt, "mms_m_id")
        val _columnIndexOfSub: Int = getColumnIndexOrThrow(_stmt, "mms_sub")
        val _columnIndexOfSubCs: Int = getColumnIndexOrThrow(_stmt, "mms_sub_cs")
        val _columnIndexOfCtT: Int = getColumnIndexOrThrow(_stmt, "mms_ct_t")
        val _columnIndexOfCtL: Int = getColumnIndexOrThrow(_stmt, "mms_ct_l")
        val _columnIndexOfExp: Int = getColumnIndexOrThrow(_stmt, "mms_exp")
        val _columnIndexOfMCls: Int = getColumnIndexOrThrow(_stmt, "mms_m_cls")
        val _columnIndexOfMType: Int = getColumnIndexOrThrow(_stmt, "mms_m_type")
        val _columnIndexOfV: Int = getColumnIndexOrThrow(_stmt, "mms_v")
        val _columnIndexOfMSize: Int = getColumnIndexOrThrow(_stmt, "mms_m_size")
        val _columnIndexOfPri: Int = getColumnIndexOrThrow(_stmt, "mms_pri")
        val _columnIndexOfRr: Int = getColumnIndexOrThrow(_stmt, "mms_rr")
        val _columnIndexOfRptA: Int = getColumnIndexOrThrow(_stmt, "mms_rpt_a")
        val _columnIndexOfRespSt: Int = getColumnIndexOrThrow(_stmt, "mms_resp_st")
        val _columnIndexOfSt: Int = getColumnIndexOrThrow(_stmt, "mms_st")
        val _columnIndexOfTrId: Int = getColumnIndexOrThrow(_stmt, "mms_tr_id")
        val _columnIndexOfRetrSt: Int = getColumnIndexOrThrow(_stmt, "mms_retr_st")
        val _columnIndexOfRetrTxt: Int = getColumnIndexOrThrow(_stmt, "mms_retr_txt")
        val _columnIndexOfRetrTxtCs: Int = getColumnIndexOrThrow(_stmt, "mms_retr_txt_cs")
        val _columnIndexOfReadStatus: Int = getColumnIndexOrThrow(_stmt, "mms_read_status")
        val _columnIndexOfCtCls: Int = getColumnIndexOrThrow(_stmt, "mms_ct_cls")
        val _columnIndexOfRespTxt: Int = getColumnIndexOrThrow(_stmt, "mms_resp_txt")
        val _columnIndexOfDTm: Int = getColumnIndexOrThrow(_stmt, "mms_d_tm")
        val _columnIndexOfDRpt: Int = getColumnIndexOrThrow(_stmt, "mms_d_rpt")
        val _columnIndexOfLocked_1: Int = getColumnIndexOrThrow(_stmt, "mms_locked")
        val _columnIndexOfSubId_1: Int = getColumnIndexOrThrow(_stmt, "mms_sub_id")
        val _columnIndexOfSeen_1: Int = getColumnIndexOrThrow(_stmt, "mms_seen")
        val _columnIndexOfCreator_1: Int = getColumnIndexOrThrow(_stmt, "mms_creator")
        val _columnIndexOfTextOnly: Int = getColumnIndexOrThrow(_stmt, "mms_text_only")
        val _result: Conversations?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpSms_data: ByteArray?
          if (_stmt.isNull(_columnIndexOfSmsData)) {
            _tmpSms_data = null
          } else {
            _tmpSms_data = _stmt.getBlob(_columnIndexOfSmsData)
          }
          val _tmpMms_text: String?
          if (_stmt.isNull(_columnIndexOfMmsText)) {
            _tmpMms_text = null
          } else {
            _tmpMms_text = _stmt.getText(_columnIndexOfMmsText)
          }
          val _tmpMms_content_uri: String?
          if (_stmt.isNull(_columnIndexOfMmsContentUri)) {
            _tmpMms_content_uri = null
          } else {
            _tmpMms_content_uri = _stmt.getText(_columnIndexOfMmsContentUri)
          }
          val _tmpMms_mimetype: String?
          if (_stmt.isNull(_columnIndexOfMmsMimetype)) {
            _tmpMms_mimetype = null
          } else {
            _tmpMms_mimetype = _stmt.getText(_columnIndexOfMmsMimetype)
          }
          val _tmpMms_filename: String?
          if (_stmt.isNull(_columnIndexOfMmsFilename)) {
            _tmpMms_filename = null
          } else {
            _tmpMms_filename = _stmt.getText(_columnIndexOfMmsFilename)
          }
          val _tmpMms_filepath: String?
          if (_stmt.isNull(_columnIndexOfMmsFilepath)) {
            _tmpMms_filepath = null
          } else {
            _tmpMms_filepath = _stmt.getText(_columnIndexOfMmsFilepath)
          }
          val _tmpSms: SmsMmsNatives.Sms?
          if (!(_stmt.isNull(_columnIndexOfId_1) && _stmt.isNull(_columnIndexOfThreadId) &&
              _stmt.isNull(_columnIndexOfAddress) && _stmt.isNull(_columnIndexOfPerson) &&
              _stmt.isNull(_columnIndexOfDate) && _stmt.isNull(_columnIndexOfDateSent) &&
              _stmt.isNull(_columnIndexOfProtocol) && _stmt.isNull(_columnIndexOfRead) &&
              _stmt.isNull(_columnIndexOfStatus) && _stmt.isNull(_columnIndexOfType) &&
              _stmt.isNull(_columnIndexOfReplyPathPresent) && _stmt.isNull(_columnIndexOfSubject) &&
              _stmt.isNull(_columnIndexOfBody) && _stmt.isNull(_columnIndexOfServiceCenter) &&
              _stmt.isNull(_columnIndexOfLocked) && _stmt.isNull(_columnIndexOfSubId) &&
              _stmt.isNull(_columnIndexOfErrorCode) && _stmt.isNull(_columnIndexOfCreator) &&
              _stmt.isNull(_columnIndexOfSeen))) {
            val _tmp_id: Long?
            if (_stmt.isNull(_columnIndexOfId_1)) {
              _tmp_id = null
            } else {
              _tmp_id = _stmt.getLong(_columnIndexOfId_1)
            }
            val _tmpThread_id: Int
            _tmpThread_id = _stmt.getLong(_columnIndexOfThreadId).toInt()
            val _tmpAddress: String?
            if (_stmt.isNull(_columnIndexOfAddress)) {
              _tmpAddress = null
            } else {
              _tmpAddress = _stmt.getText(_columnIndexOfAddress)
            }
            val _tmpPerson: String?
            if (_stmt.isNull(_columnIndexOfPerson)) {
              _tmpPerson = null
            } else {
              _tmpPerson = _stmt.getText(_columnIndexOfPerson)
            }
            val _tmpDate: Long
            _tmpDate = _stmt.getLong(_columnIndexOfDate)
            val _tmpDate_sent: Long
            _tmpDate_sent = _stmt.getLong(_columnIndexOfDateSent)
            val _tmpProtocol: String?
            if (_stmt.isNull(_columnIndexOfProtocol)) {
              _tmpProtocol = null
            } else {
              _tmpProtocol = _stmt.getText(_columnIndexOfProtocol)
            }
            val _tmpRead: Int
            _tmpRead = _stmt.getLong(_columnIndexOfRead).toInt()
            val _tmpStatus: Int
            _tmpStatus = _stmt.getLong(_columnIndexOfStatus).toInt()
            val _tmpType: Int
            _tmpType = _stmt.getLong(_columnIndexOfType).toInt()
            val _tmpReply_path_present: String?
            if (_stmt.isNull(_columnIndexOfReplyPathPresent)) {
              _tmpReply_path_present = null
            } else {
              _tmpReply_path_present = _stmt.getText(_columnIndexOfReplyPathPresent)
            }
            val _tmpSubject: String?
            if (_stmt.isNull(_columnIndexOfSubject)) {
              _tmpSubject = null
            } else {
              _tmpSubject = _stmt.getText(_columnIndexOfSubject)
            }
            val _tmpBody: String?
            if (_stmt.isNull(_columnIndexOfBody)) {
              _tmpBody = null
            } else {
              _tmpBody = _stmt.getText(_columnIndexOfBody)
            }
            val _tmpService_center: String?
            if (_stmt.isNull(_columnIndexOfServiceCenter)) {
              _tmpService_center = null
            } else {
              _tmpService_center = _stmt.getText(_columnIndexOfServiceCenter)
            }
            val _tmpLocked: Int?
            if (_stmt.isNull(_columnIndexOfLocked)) {
              _tmpLocked = null
            } else {
              _tmpLocked = _stmt.getLong(_columnIndexOfLocked).toInt()
            }
            val _tmpSub_id: Long
            _tmpSub_id = _stmt.getLong(_columnIndexOfSubId)
            val _tmpError_code: Int?
            if (_stmt.isNull(_columnIndexOfErrorCode)) {
              _tmpError_code = null
            } else {
              _tmpError_code = _stmt.getLong(_columnIndexOfErrorCode).toInt()
            }
            val _tmpCreator: String?
            if (_stmt.isNull(_columnIndexOfCreator)) {
              _tmpCreator = null
            } else {
              _tmpCreator = _stmt.getText(_columnIndexOfCreator)
            }
            val _tmpSeen: Int?
            if (_stmt.isNull(_columnIndexOfSeen)) {
              _tmpSeen = null
            } else {
              _tmpSeen = _stmt.getLong(_columnIndexOfSeen).toInt()
            }
            _tmpSms =
                SmsMmsNatives.Sms(_tmp_id,_tmpThread_id,_tmpAddress,_tmpPerson,_tmpDate,_tmpDate_sent,_tmpProtocol,_tmpRead,_tmpStatus,_tmpType,_tmpReply_path_present,_tmpSubject,_tmpBody,_tmpService_center,_tmpLocked,_tmpSub_id,_tmpError_code,_tmpCreator,_tmpSeen)
          } else {
            _tmpSms = null
          }
          val _tmpMms: SmsMmsNatives.Mms?
          if (!(_stmt.isNull(_columnIndexOfId_2) && _stmt.isNull(_columnIndexOfThreadId_1) &&
              _stmt.isNull(_columnIndexOfDate_1) && _stmt.isNull(_columnIndexOfDateSent_1) &&
              _stmt.isNull(_columnIndexOfMsgBox) && _stmt.isNull(_columnIndexOfRead_1) &&
              _stmt.isNull(_columnIndexOfMId) && _stmt.isNull(_columnIndexOfSub) &&
              _stmt.isNull(_columnIndexOfSubCs) && _stmt.isNull(_columnIndexOfCtT) &&
              _stmt.isNull(_columnIndexOfCtL) && _stmt.isNull(_columnIndexOfExp) &&
              _stmt.isNull(_columnIndexOfMCls) && _stmt.isNull(_columnIndexOfMType) &&
              _stmt.isNull(_columnIndexOfV) && _stmt.isNull(_columnIndexOfMSize) &&
              _stmt.isNull(_columnIndexOfPri) && _stmt.isNull(_columnIndexOfRr) &&
              _stmt.isNull(_columnIndexOfRptA) && _stmt.isNull(_columnIndexOfRespSt) &&
              _stmt.isNull(_columnIndexOfSt) && _stmt.isNull(_columnIndexOfTrId) &&
              _stmt.isNull(_columnIndexOfRetrSt) && _stmt.isNull(_columnIndexOfRetrTxt) &&
              _stmt.isNull(_columnIndexOfRetrTxtCs) && _stmt.isNull(_columnIndexOfReadStatus) &&
              _stmt.isNull(_columnIndexOfCtCls) && _stmt.isNull(_columnIndexOfRespTxt) &&
              _stmt.isNull(_columnIndexOfDTm) && _stmt.isNull(_columnIndexOfDRpt) &&
              _stmt.isNull(_columnIndexOfLocked_1) && _stmt.isNull(_columnIndexOfSubId_1) &&
              _stmt.isNull(_columnIndexOfSeen_1) && _stmt.isNull(_columnIndexOfCreator_1) &&
              _stmt.isNull(_columnIndexOfTextOnly))) {
            val _tmp_id_1: Long
            _tmp_id_1 = _stmt.getLong(_columnIndexOfId_2)
            val _tmpThread_id_1: Int
            _tmpThread_id_1 = _stmt.getLong(_columnIndexOfThreadId_1).toInt()
            val _tmpDate_1: Long
            _tmpDate_1 = _stmt.getLong(_columnIndexOfDate_1)
            val _tmpDate_sent_1: Long
            _tmpDate_sent_1 = _stmt.getLong(_columnIndexOfDateSent_1)
            val _tmpMsg_box: Int
            _tmpMsg_box = _stmt.getLong(_columnIndexOfMsgBox).toInt()
            val _tmpRead_1: Int?
            if (_stmt.isNull(_columnIndexOfRead_1)) {
              _tmpRead_1 = null
            } else {
              _tmpRead_1 = _stmt.getLong(_columnIndexOfRead_1).toInt()
            }
            val _tmpM_id: String?
            if (_stmt.isNull(_columnIndexOfMId)) {
              _tmpM_id = null
            } else {
              _tmpM_id = _stmt.getText(_columnIndexOfMId)
            }
            val _tmpSub: String?
            if (_stmt.isNull(_columnIndexOfSub)) {
              _tmpSub = null
            } else {
              _tmpSub = _stmt.getText(_columnIndexOfSub)
            }
            val _tmpSub_cs: Int?
            if (_stmt.isNull(_columnIndexOfSubCs)) {
              _tmpSub_cs = null
            } else {
              _tmpSub_cs = _stmt.getLong(_columnIndexOfSubCs).toInt()
            }
            val _tmpCt_t: String?
            if (_stmt.isNull(_columnIndexOfCtT)) {
              _tmpCt_t = null
            } else {
              _tmpCt_t = _stmt.getText(_columnIndexOfCtT)
            }
            val _tmpCt_l: String?
            if (_stmt.isNull(_columnIndexOfCtL)) {
              _tmpCt_l = null
            } else {
              _tmpCt_l = _stmt.getText(_columnIndexOfCtL)
            }
            val _tmpExp: String?
            if (_stmt.isNull(_columnIndexOfExp)) {
              _tmpExp = null
            } else {
              _tmpExp = _stmt.getText(_columnIndexOfExp)
            }
            val _tmpM_cls: String?
            if (_stmt.isNull(_columnIndexOfMCls)) {
              _tmpM_cls = null
            } else {
              _tmpM_cls = _stmt.getText(_columnIndexOfMCls)
            }
            val _tmpM_type: Int?
            if (_stmt.isNull(_columnIndexOfMType)) {
              _tmpM_type = null
            } else {
              _tmpM_type = _stmt.getLong(_columnIndexOfMType).toInt()
            }
            val _tmpV: Int?
            if (_stmt.isNull(_columnIndexOfV)) {
              _tmpV = null
            } else {
              _tmpV = _stmt.getLong(_columnIndexOfV).toInt()
            }
            val _tmpM_size: Int?
            if (_stmt.isNull(_columnIndexOfMSize)) {
              _tmpM_size = null
            } else {
              _tmpM_size = _stmt.getLong(_columnIndexOfMSize).toInt()
            }
            val _tmpPri: Int?
            if (_stmt.isNull(_columnIndexOfPri)) {
              _tmpPri = null
            } else {
              _tmpPri = _stmt.getLong(_columnIndexOfPri).toInt()
            }
            val _tmpRr: Int?
            if (_stmt.isNull(_columnIndexOfRr)) {
              _tmpRr = null
            } else {
              _tmpRr = _stmt.getLong(_columnIndexOfRr).toInt()
            }
            val _tmpRpt_a: String?
            if (_stmt.isNull(_columnIndexOfRptA)) {
              _tmpRpt_a = null
            } else {
              _tmpRpt_a = _stmt.getText(_columnIndexOfRptA)
            }
            val _tmpResp_st: String?
            if (_stmt.isNull(_columnIndexOfRespSt)) {
              _tmpResp_st = null
            } else {
              _tmpResp_st = _stmt.getText(_columnIndexOfRespSt)
            }
            val _tmpSt: String?
            if (_stmt.isNull(_columnIndexOfSt)) {
              _tmpSt = null
            } else {
              _tmpSt = _stmt.getText(_columnIndexOfSt)
            }
            val _tmpTr_id: String?
            if (_stmt.isNull(_columnIndexOfTrId)) {
              _tmpTr_id = null
            } else {
              _tmpTr_id = _stmt.getText(_columnIndexOfTrId)
            }
            val _tmpRetr_st: String?
            if (_stmt.isNull(_columnIndexOfRetrSt)) {
              _tmpRetr_st = null
            } else {
              _tmpRetr_st = _stmt.getText(_columnIndexOfRetrSt)
            }
            val _tmpRetr_txt: String?
            if (_stmt.isNull(_columnIndexOfRetrTxt)) {
              _tmpRetr_txt = null
            } else {
              _tmpRetr_txt = _stmt.getText(_columnIndexOfRetrTxt)
            }
            val _tmpRetr_txt_cs: String?
            if (_stmt.isNull(_columnIndexOfRetrTxtCs)) {
              _tmpRetr_txt_cs = null
            } else {
              _tmpRetr_txt_cs = _stmt.getText(_columnIndexOfRetrTxtCs)
            }
            val _tmpRead_status: String?
            if (_stmt.isNull(_columnIndexOfReadStatus)) {
              _tmpRead_status = null
            } else {
              _tmpRead_status = _stmt.getText(_columnIndexOfReadStatus)
            }
            val _tmpCt_cls: String?
            if (_stmt.isNull(_columnIndexOfCtCls)) {
              _tmpCt_cls = null
            } else {
              _tmpCt_cls = _stmt.getText(_columnIndexOfCtCls)
            }
            val _tmpResp_txt: String?
            if (_stmt.isNull(_columnIndexOfRespTxt)) {
              _tmpResp_txt = null
            } else {
              _tmpResp_txt = _stmt.getText(_columnIndexOfRespTxt)
            }
            val _tmpD_tm: String?
            if (_stmt.isNull(_columnIndexOfDTm)) {
              _tmpD_tm = null
            } else {
              _tmpD_tm = _stmt.getText(_columnIndexOfDTm)
            }
            val _tmpD_rpt: Int?
            if (_stmt.isNull(_columnIndexOfDRpt)) {
              _tmpD_rpt = null
            } else {
              _tmpD_rpt = _stmt.getLong(_columnIndexOfDRpt).toInt()
            }
            val _tmpLocked_1: Int?
            if (_stmt.isNull(_columnIndexOfLocked_1)) {
              _tmpLocked_1 = null
            } else {
              _tmpLocked_1 = _stmt.getLong(_columnIndexOfLocked_1).toInt()
            }
            val _tmpSub_id_1: Long?
            if (_stmt.isNull(_columnIndexOfSubId_1)) {
              _tmpSub_id_1 = null
            } else {
              _tmpSub_id_1 = _stmt.getLong(_columnIndexOfSubId_1)
            }
            val _tmpSeen_1: Int?
            if (_stmt.isNull(_columnIndexOfSeen_1)) {
              _tmpSeen_1 = null
            } else {
              _tmpSeen_1 = _stmt.getLong(_columnIndexOfSeen_1).toInt()
            }
            val _tmpCreator_1: String?
            if (_stmt.isNull(_columnIndexOfCreator_1)) {
              _tmpCreator_1 = null
            } else {
              _tmpCreator_1 = _stmt.getText(_columnIndexOfCreator_1)
            }
            val _tmpText_only: Int?
            if (_stmt.isNull(_columnIndexOfTextOnly)) {
              _tmpText_only = null
            } else {
              _tmpText_only = _stmt.getLong(_columnIndexOfTextOnly).toInt()
            }
            _tmpMms =
                SmsMmsNatives.Mms(_tmp_id_1,_tmpThread_id_1,_tmpDate_1,_tmpDate_sent_1,_tmpMsg_box,_tmpRead_1,_tmpM_id,_tmpSub,_tmpSub_cs,_tmpCt_t,_tmpCt_l,_tmpExp,_tmpM_cls,_tmpM_type,_tmpV,_tmpM_size,_tmpPri,_tmpRr,_tmpRpt_a,_tmpResp_st,_tmpSt,_tmpTr_id,_tmpRetr_st,_tmpRetr_txt,_tmpRetr_txt_cs,_tmpRead_status,_tmpCt_cls,_tmpResp_txt,_tmpD_tm,_tmpD_rpt,_tmpLocked_1,_tmpSub_id_1,_tmpSeen_1,_tmpCreator_1,_tmpText_only)
          } else {
            _tmpMms = null
          }
          _result =
              Conversations(_tmpId,_tmpSms,_tmpMms,_tmpSms_data,_tmpMms_text,_tmpMms_content_uri,_tmpMms_mimetype,_tmpMms_filename,_tmpMms_filepath)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getConversations(threadId: Int): PagingSource<Int, Conversations> {
    val _sql: String =
        "SELECT * FROM Conversations WHERE thread_id = ? OR Conversations.mms_thread_id = ? ORDER BY date DESC"
    val _rawQuery: RoomRawQuery = RoomRawQuery(_sql) { _stmt ->
      var _argIndex: Int = 1
      _stmt.bindLong(_argIndex, threadId.toLong())
      _argIndex = 2
      _stmt.bindLong(_argIndex, threadId.toLong())
    }
    return object : LimitOffsetPagingSource<Conversations>(_rawQuery, __db, "Conversations") {
      protected override suspend fun convertRows(limitOffsetQuery: RoomRawQuery, itemCount: Int):
          List<Conversations> = performSuspending(__db, true, false) { _connection ->
        val _stmt: SQLiteStatement = _connection.prepare(limitOffsetQuery.sql)
        limitOffsetQuery.getBindingFunction().invoke(_stmt)
        try {
          val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
          val _columnIndexOfSmsData: Int = getColumnIndexOrThrow(_stmt, "sms_data")
          val _columnIndexOfMmsText: Int = getColumnIndexOrThrow(_stmt, "mms_text")
          val _columnIndexOfMmsContentUri: Int = getColumnIndexOrThrow(_stmt, "mms_content_uri")
          val _columnIndexOfMmsMimetype: Int = getColumnIndexOrThrow(_stmt, "mms_mimetype")
          val _columnIndexOfMmsFilename: Int = getColumnIndexOrThrow(_stmt, "mms_filename")
          val _columnIndexOfMmsFilepath: Int = getColumnIndexOrThrow(_stmt, "mms_filepath")
          val _columnIndexOfId_1: Int = getColumnIndexOrThrow(_stmt, "_id")
          val _columnIndexOfThreadId: Int = getColumnIndexOrThrow(_stmt, "thread_id")
          val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
          val _columnIndexOfPerson: Int = getColumnIndexOrThrow(_stmt, "person")
          val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
          val _columnIndexOfDateSent: Int = getColumnIndexOrThrow(_stmt, "date_sent")
          val _columnIndexOfProtocol: Int = getColumnIndexOrThrow(_stmt, "protocol")
          val _columnIndexOfRead: Int = getColumnIndexOrThrow(_stmt, "read")
          val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
          val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
          val _columnIndexOfReplyPathPresent: Int = getColumnIndexOrThrow(_stmt,
              "reply_path_present")
          val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
          val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
          val _columnIndexOfServiceCenter: Int = getColumnIndexOrThrow(_stmt, "service_center")
          val _columnIndexOfLocked: Int = getColumnIndexOrThrow(_stmt, "locked")
          val _columnIndexOfSubId: Int = getColumnIndexOrThrow(_stmt, "sub_id")
          val _columnIndexOfErrorCode: Int = getColumnIndexOrThrow(_stmt, "error_code")
          val _columnIndexOfCreator: Int = getColumnIndexOrThrow(_stmt, "creator")
          val _columnIndexOfSeen: Int = getColumnIndexOrThrow(_stmt, "seen")
          val _columnIndexOfId_2: Int = getColumnIndexOrThrow(_stmt, "mms__id")
          val _columnIndexOfThreadId_1: Int = getColumnIndexOrThrow(_stmt, "mms_thread_id")
          val _columnIndexOfDate_1: Int = getColumnIndexOrThrow(_stmt, "mms_date")
          val _columnIndexOfDateSent_1: Int = getColumnIndexOrThrow(_stmt, "mms_date_sent")
          val _columnIndexOfMsgBox: Int = getColumnIndexOrThrow(_stmt, "mms_msg_box")
          val _columnIndexOfRead_1: Int = getColumnIndexOrThrow(_stmt, "mms_read")
          val _columnIndexOfMId: Int = getColumnIndexOrThrow(_stmt, "mms_m_id")
          val _columnIndexOfSub: Int = getColumnIndexOrThrow(_stmt, "mms_sub")
          val _columnIndexOfSubCs: Int = getColumnIndexOrThrow(_stmt, "mms_sub_cs")
          val _columnIndexOfCtT: Int = getColumnIndexOrThrow(_stmt, "mms_ct_t")
          val _columnIndexOfCtL: Int = getColumnIndexOrThrow(_stmt, "mms_ct_l")
          val _columnIndexOfExp: Int = getColumnIndexOrThrow(_stmt, "mms_exp")
          val _columnIndexOfMCls: Int = getColumnIndexOrThrow(_stmt, "mms_m_cls")
          val _columnIndexOfMType: Int = getColumnIndexOrThrow(_stmt, "mms_m_type")
          val _columnIndexOfV: Int = getColumnIndexOrThrow(_stmt, "mms_v")
          val _columnIndexOfMSize: Int = getColumnIndexOrThrow(_stmt, "mms_m_size")
          val _columnIndexOfPri: Int = getColumnIndexOrThrow(_stmt, "mms_pri")
          val _columnIndexOfRr: Int = getColumnIndexOrThrow(_stmt, "mms_rr")
          val _columnIndexOfRptA: Int = getColumnIndexOrThrow(_stmt, "mms_rpt_a")
          val _columnIndexOfRespSt: Int = getColumnIndexOrThrow(_stmt, "mms_resp_st")
          val _columnIndexOfSt: Int = getColumnIndexOrThrow(_stmt, "mms_st")
          val _columnIndexOfTrId: Int = getColumnIndexOrThrow(_stmt, "mms_tr_id")
          val _columnIndexOfRetrSt: Int = getColumnIndexOrThrow(_stmt, "mms_retr_st")
          val _columnIndexOfRetrTxt: Int = getColumnIndexOrThrow(_stmt, "mms_retr_txt")
          val _columnIndexOfRetrTxtCs: Int = getColumnIndexOrThrow(_stmt, "mms_retr_txt_cs")
          val _columnIndexOfReadStatus: Int = getColumnIndexOrThrow(_stmt, "mms_read_status")
          val _columnIndexOfCtCls: Int = getColumnIndexOrThrow(_stmt, "mms_ct_cls")
          val _columnIndexOfRespTxt: Int = getColumnIndexOrThrow(_stmt, "mms_resp_txt")
          val _columnIndexOfDTm: Int = getColumnIndexOrThrow(_stmt, "mms_d_tm")
          val _columnIndexOfDRpt: Int = getColumnIndexOrThrow(_stmt, "mms_d_rpt")
          val _columnIndexOfLocked_1: Int = getColumnIndexOrThrow(_stmt, "mms_locked")
          val _columnIndexOfSubId_1: Int = getColumnIndexOrThrow(_stmt, "mms_sub_id")
          val _columnIndexOfSeen_1: Int = getColumnIndexOrThrow(_stmt, "mms_seen")
          val _columnIndexOfCreator_1: Int = getColumnIndexOrThrow(_stmt, "mms_creator")
          val _columnIndexOfTextOnly: Int = getColumnIndexOrThrow(_stmt, "mms_text_only")
          val _result: MutableList<Conversations> = mutableListOf()
          while (_stmt.step()) {
            val _item: Conversations
            val _tmpId: Long
            _tmpId = _stmt.getLong(_columnIndexOfId)
            val _tmpSms_data: ByteArray?
            if (_stmt.isNull(_columnIndexOfSmsData)) {
              _tmpSms_data = null
            } else {
              _tmpSms_data = _stmt.getBlob(_columnIndexOfSmsData)
            }
            val _tmpMms_text: String?
            if (_stmt.isNull(_columnIndexOfMmsText)) {
              _tmpMms_text = null
            } else {
              _tmpMms_text = _stmt.getText(_columnIndexOfMmsText)
            }
            val _tmpMms_content_uri: String?
            if (_stmt.isNull(_columnIndexOfMmsContentUri)) {
              _tmpMms_content_uri = null
            } else {
              _tmpMms_content_uri = _stmt.getText(_columnIndexOfMmsContentUri)
            }
            val _tmpMms_mimetype: String?
            if (_stmt.isNull(_columnIndexOfMmsMimetype)) {
              _tmpMms_mimetype = null
            } else {
              _tmpMms_mimetype = _stmt.getText(_columnIndexOfMmsMimetype)
            }
            val _tmpMms_filename: String?
            if (_stmt.isNull(_columnIndexOfMmsFilename)) {
              _tmpMms_filename = null
            } else {
              _tmpMms_filename = _stmt.getText(_columnIndexOfMmsFilename)
            }
            val _tmpMms_filepath: String?
            if (_stmt.isNull(_columnIndexOfMmsFilepath)) {
              _tmpMms_filepath = null
            } else {
              _tmpMms_filepath = _stmt.getText(_columnIndexOfMmsFilepath)
            }
            val _tmpSms: SmsMmsNatives.Sms?
            if (!(_stmt.isNull(_columnIndexOfId_1) && _stmt.isNull(_columnIndexOfThreadId) &&
                _stmt.isNull(_columnIndexOfAddress) && _stmt.isNull(_columnIndexOfPerson) &&
                _stmt.isNull(_columnIndexOfDate) && _stmt.isNull(_columnIndexOfDateSent) &&
                _stmt.isNull(_columnIndexOfProtocol) && _stmt.isNull(_columnIndexOfRead) &&
                _stmt.isNull(_columnIndexOfStatus) && _stmt.isNull(_columnIndexOfType) &&
                _stmt.isNull(_columnIndexOfReplyPathPresent) && _stmt.isNull(_columnIndexOfSubject)
                && _stmt.isNull(_columnIndexOfBody) && _stmt.isNull(_columnIndexOfServiceCenter) &&
                _stmt.isNull(_columnIndexOfLocked) && _stmt.isNull(_columnIndexOfSubId) &&
                _stmt.isNull(_columnIndexOfErrorCode) && _stmt.isNull(_columnIndexOfCreator) &&
                _stmt.isNull(_columnIndexOfSeen))) {
              val _tmp_id: Long?
              if (_stmt.isNull(_columnIndexOfId_1)) {
                _tmp_id = null
              } else {
                _tmp_id = _stmt.getLong(_columnIndexOfId_1)
              }
              val _tmpThread_id: Int
              _tmpThread_id = _stmt.getLong(_columnIndexOfThreadId).toInt()
              val _tmpAddress: String?
              if (_stmt.isNull(_columnIndexOfAddress)) {
                _tmpAddress = null
              } else {
                _tmpAddress = _stmt.getText(_columnIndexOfAddress)
              }
              val _tmpPerson: String?
              if (_stmt.isNull(_columnIndexOfPerson)) {
                _tmpPerson = null
              } else {
                _tmpPerson = _stmt.getText(_columnIndexOfPerson)
              }
              val _tmpDate: Long
              _tmpDate = _stmt.getLong(_columnIndexOfDate)
              val _tmpDate_sent: Long
              _tmpDate_sent = _stmt.getLong(_columnIndexOfDateSent)
              val _tmpProtocol: String?
              if (_stmt.isNull(_columnIndexOfProtocol)) {
                _tmpProtocol = null
              } else {
                _tmpProtocol = _stmt.getText(_columnIndexOfProtocol)
              }
              val _tmpRead: Int
              _tmpRead = _stmt.getLong(_columnIndexOfRead).toInt()
              val _tmpStatus: Int
              _tmpStatus = _stmt.getLong(_columnIndexOfStatus).toInt()
              val _tmpType: Int
              _tmpType = _stmt.getLong(_columnIndexOfType).toInt()
              val _tmpReply_path_present: String?
              if (_stmt.isNull(_columnIndexOfReplyPathPresent)) {
                _tmpReply_path_present = null
              } else {
                _tmpReply_path_present = _stmt.getText(_columnIndexOfReplyPathPresent)
              }
              val _tmpSubject: String?
              if (_stmt.isNull(_columnIndexOfSubject)) {
                _tmpSubject = null
              } else {
                _tmpSubject = _stmt.getText(_columnIndexOfSubject)
              }
              val _tmpBody: String?
              if (_stmt.isNull(_columnIndexOfBody)) {
                _tmpBody = null
              } else {
                _tmpBody = _stmt.getText(_columnIndexOfBody)
              }
              val _tmpService_center: String?
              if (_stmt.isNull(_columnIndexOfServiceCenter)) {
                _tmpService_center = null
              } else {
                _tmpService_center = _stmt.getText(_columnIndexOfServiceCenter)
              }
              val _tmpLocked: Int?
              if (_stmt.isNull(_columnIndexOfLocked)) {
                _tmpLocked = null
              } else {
                _tmpLocked = _stmt.getLong(_columnIndexOfLocked).toInt()
              }
              val _tmpSub_id: Long
              _tmpSub_id = _stmt.getLong(_columnIndexOfSubId)
              val _tmpError_code: Int?
              if (_stmt.isNull(_columnIndexOfErrorCode)) {
                _tmpError_code = null
              } else {
                _tmpError_code = _stmt.getLong(_columnIndexOfErrorCode).toInt()
              }
              val _tmpCreator: String?
              if (_stmt.isNull(_columnIndexOfCreator)) {
                _tmpCreator = null
              } else {
                _tmpCreator = _stmt.getText(_columnIndexOfCreator)
              }
              val _tmpSeen: Int?
              if (_stmt.isNull(_columnIndexOfSeen)) {
                _tmpSeen = null
              } else {
                _tmpSeen = _stmt.getLong(_columnIndexOfSeen).toInt()
              }
              _tmpSms =
                  SmsMmsNatives.Sms(_tmp_id,_tmpThread_id,_tmpAddress,_tmpPerson,_tmpDate,_tmpDate_sent,_tmpProtocol,_tmpRead,_tmpStatus,_tmpType,_tmpReply_path_present,_tmpSubject,_tmpBody,_tmpService_center,_tmpLocked,_tmpSub_id,_tmpError_code,_tmpCreator,_tmpSeen)
            } else {
              _tmpSms = null
            }
            val _tmpMms: SmsMmsNatives.Mms?
            if (!(_stmt.isNull(_columnIndexOfId_2) && _stmt.isNull(_columnIndexOfThreadId_1) &&
                _stmt.isNull(_columnIndexOfDate_1) && _stmt.isNull(_columnIndexOfDateSent_1) &&
                _stmt.isNull(_columnIndexOfMsgBox) && _stmt.isNull(_columnIndexOfRead_1) &&
                _stmt.isNull(_columnIndexOfMId) && _stmt.isNull(_columnIndexOfSub) &&
                _stmt.isNull(_columnIndexOfSubCs) && _stmt.isNull(_columnIndexOfCtT) &&
                _stmt.isNull(_columnIndexOfCtL) && _stmt.isNull(_columnIndexOfExp) &&
                _stmt.isNull(_columnIndexOfMCls) && _stmt.isNull(_columnIndexOfMType) &&
                _stmt.isNull(_columnIndexOfV) && _stmt.isNull(_columnIndexOfMSize) &&
                _stmt.isNull(_columnIndexOfPri) && _stmt.isNull(_columnIndexOfRr) &&
                _stmt.isNull(_columnIndexOfRptA) && _stmt.isNull(_columnIndexOfRespSt) &&
                _stmt.isNull(_columnIndexOfSt) && _stmt.isNull(_columnIndexOfTrId) &&
                _stmt.isNull(_columnIndexOfRetrSt) && _stmt.isNull(_columnIndexOfRetrTxt) &&
                _stmt.isNull(_columnIndexOfRetrTxtCs) && _stmt.isNull(_columnIndexOfReadStatus) &&
                _stmt.isNull(_columnIndexOfCtCls) && _stmt.isNull(_columnIndexOfRespTxt) &&
                _stmt.isNull(_columnIndexOfDTm) && _stmt.isNull(_columnIndexOfDRpt) &&
                _stmt.isNull(_columnIndexOfLocked_1) && _stmt.isNull(_columnIndexOfSubId_1) &&
                _stmt.isNull(_columnIndexOfSeen_1) && _stmt.isNull(_columnIndexOfCreator_1) &&
                _stmt.isNull(_columnIndexOfTextOnly))) {
              val _tmp_id_1: Long
              _tmp_id_1 = _stmt.getLong(_columnIndexOfId_2)
              val _tmpThread_id_1: Int
              _tmpThread_id_1 = _stmt.getLong(_columnIndexOfThreadId_1).toInt()
              val _tmpDate_1: Long
              _tmpDate_1 = _stmt.getLong(_columnIndexOfDate_1)
              val _tmpDate_sent_1: Long
              _tmpDate_sent_1 = _stmt.getLong(_columnIndexOfDateSent_1)
              val _tmpMsg_box: Int
              _tmpMsg_box = _stmt.getLong(_columnIndexOfMsgBox).toInt()
              val _tmpRead_1: Int?
              if (_stmt.isNull(_columnIndexOfRead_1)) {
                _tmpRead_1 = null
              } else {
                _tmpRead_1 = _stmt.getLong(_columnIndexOfRead_1).toInt()
              }
              val _tmpM_id: String?
              if (_stmt.isNull(_columnIndexOfMId)) {
                _tmpM_id = null
              } else {
                _tmpM_id = _stmt.getText(_columnIndexOfMId)
              }
              val _tmpSub: String?
              if (_stmt.isNull(_columnIndexOfSub)) {
                _tmpSub = null
              } else {
                _tmpSub = _stmt.getText(_columnIndexOfSub)
              }
              val _tmpSub_cs: Int?
              if (_stmt.isNull(_columnIndexOfSubCs)) {
                _tmpSub_cs = null
              } else {
                _tmpSub_cs = _stmt.getLong(_columnIndexOfSubCs).toInt()
              }
              val _tmpCt_t: String?
              if (_stmt.isNull(_columnIndexOfCtT)) {
                _tmpCt_t = null
              } else {
                _tmpCt_t = _stmt.getText(_columnIndexOfCtT)
              }
              val _tmpCt_l: String?
              if (_stmt.isNull(_columnIndexOfCtL)) {
                _tmpCt_l = null
              } else {
                _tmpCt_l = _stmt.getText(_columnIndexOfCtL)
              }
              val _tmpExp: String?
              if (_stmt.isNull(_columnIndexOfExp)) {
                _tmpExp = null
              } else {
                _tmpExp = _stmt.getText(_columnIndexOfExp)
              }
              val _tmpM_cls: String?
              if (_stmt.isNull(_columnIndexOfMCls)) {
                _tmpM_cls = null
              } else {
                _tmpM_cls = _stmt.getText(_columnIndexOfMCls)
              }
              val _tmpM_type: Int?
              if (_stmt.isNull(_columnIndexOfMType)) {
                _tmpM_type = null
              } else {
                _tmpM_type = _stmt.getLong(_columnIndexOfMType).toInt()
              }
              val _tmpV: Int?
              if (_stmt.isNull(_columnIndexOfV)) {
                _tmpV = null
              } else {
                _tmpV = _stmt.getLong(_columnIndexOfV).toInt()
              }
              val _tmpM_size: Int?
              if (_stmt.isNull(_columnIndexOfMSize)) {
                _tmpM_size = null
              } else {
                _tmpM_size = _stmt.getLong(_columnIndexOfMSize).toInt()
              }
              val _tmpPri: Int?
              if (_stmt.isNull(_columnIndexOfPri)) {
                _tmpPri = null
              } else {
                _tmpPri = _stmt.getLong(_columnIndexOfPri).toInt()
              }
              val _tmpRr: Int?
              if (_stmt.isNull(_columnIndexOfRr)) {
                _tmpRr = null
              } else {
                _tmpRr = _stmt.getLong(_columnIndexOfRr).toInt()
              }
              val _tmpRpt_a: String?
              if (_stmt.isNull(_columnIndexOfRptA)) {
                _tmpRpt_a = null
              } else {
                _tmpRpt_a = _stmt.getText(_columnIndexOfRptA)
              }
              val _tmpResp_st: String?
              if (_stmt.isNull(_columnIndexOfRespSt)) {
                _tmpResp_st = null
              } else {
                _tmpResp_st = _stmt.getText(_columnIndexOfRespSt)
              }
              val _tmpSt: String?
              if (_stmt.isNull(_columnIndexOfSt)) {
                _tmpSt = null
              } else {
                _tmpSt = _stmt.getText(_columnIndexOfSt)
              }
              val _tmpTr_id: String?
              if (_stmt.isNull(_columnIndexOfTrId)) {
                _tmpTr_id = null
              } else {
                _tmpTr_id = _stmt.getText(_columnIndexOfTrId)
              }
              val _tmpRetr_st: String?
              if (_stmt.isNull(_columnIndexOfRetrSt)) {
                _tmpRetr_st = null
              } else {
                _tmpRetr_st = _stmt.getText(_columnIndexOfRetrSt)
              }
              val _tmpRetr_txt: String?
              if (_stmt.isNull(_columnIndexOfRetrTxt)) {
                _tmpRetr_txt = null
              } else {
                _tmpRetr_txt = _stmt.getText(_columnIndexOfRetrTxt)
              }
              val _tmpRetr_txt_cs: String?
              if (_stmt.isNull(_columnIndexOfRetrTxtCs)) {
                _tmpRetr_txt_cs = null
              } else {
                _tmpRetr_txt_cs = _stmt.getText(_columnIndexOfRetrTxtCs)
              }
              val _tmpRead_status: String?
              if (_stmt.isNull(_columnIndexOfReadStatus)) {
                _tmpRead_status = null
              } else {
                _tmpRead_status = _stmt.getText(_columnIndexOfReadStatus)
              }
              val _tmpCt_cls: String?
              if (_stmt.isNull(_columnIndexOfCtCls)) {
                _tmpCt_cls = null
              } else {
                _tmpCt_cls = _stmt.getText(_columnIndexOfCtCls)
              }
              val _tmpResp_txt: String?
              if (_stmt.isNull(_columnIndexOfRespTxt)) {
                _tmpResp_txt = null
              } else {
                _tmpResp_txt = _stmt.getText(_columnIndexOfRespTxt)
              }
              val _tmpD_tm: String?
              if (_stmt.isNull(_columnIndexOfDTm)) {
                _tmpD_tm = null
              } else {
                _tmpD_tm = _stmt.getText(_columnIndexOfDTm)
              }
              val _tmpD_rpt: Int?
              if (_stmt.isNull(_columnIndexOfDRpt)) {
                _tmpD_rpt = null
              } else {
                _tmpD_rpt = _stmt.getLong(_columnIndexOfDRpt).toInt()
              }
              val _tmpLocked_1: Int?
              if (_stmt.isNull(_columnIndexOfLocked_1)) {
                _tmpLocked_1 = null
              } else {
                _tmpLocked_1 = _stmt.getLong(_columnIndexOfLocked_1).toInt()
              }
              val _tmpSub_id_1: Long?
              if (_stmt.isNull(_columnIndexOfSubId_1)) {
                _tmpSub_id_1 = null
              } else {
                _tmpSub_id_1 = _stmt.getLong(_columnIndexOfSubId_1)
              }
              val _tmpSeen_1: Int?
              if (_stmt.isNull(_columnIndexOfSeen_1)) {
                _tmpSeen_1 = null
              } else {
                _tmpSeen_1 = _stmt.getLong(_columnIndexOfSeen_1).toInt()
              }
              val _tmpCreator_1: String?
              if (_stmt.isNull(_columnIndexOfCreator_1)) {
                _tmpCreator_1 = null
              } else {
                _tmpCreator_1 = _stmt.getText(_columnIndexOfCreator_1)
              }
              val _tmpText_only: Int?
              if (_stmt.isNull(_columnIndexOfTextOnly)) {
                _tmpText_only = null
              } else {
                _tmpText_only = _stmt.getLong(_columnIndexOfTextOnly).toInt()
              }
              _tmpMms =
                  SmsMmsNatives.Mms(_tmp_id_1,_tmpThread_id_1,_tmpDate_1,_tmpDate_sent_1,_tmpMsg_box,_tmpRead_1,_tmpM_id,_tmpSub,_tmpSub_cs,_tmpCt_t,_tmpCt_l,_tmpExp,_tmpM_cls,_tmpM_type,_tmpV,_tmpM_size,_tmpPri,_tmpRr,_tmpRpt_a,_tmpResp_st,_tmpSt,_tmpTr_id,_tmpRetr_st,_tmpRetr_txt,_tmpRetr_txt_cs,_tmpRead_status,_tmpCt_cls,_tmpResp_txt,_tmpD_tm,_tmpD_rpt,_tmpLocked_1,_tmpSub_id_1,_tmpSeen_1,_tmpCreator_1,_tmpText_only)
            } else {
              _tmpMms = null
            }
            _item =
                Conversations(_tmpId,_tmpSms,_tmpMms,_tmpSms_data,_tmpMms_text,_tmpMms_content_uri,_tmpMms_mimetype,_tmpMms_filename,_tmpMms_filepath)
            _result.add(_item)
          }
          _result
        } finally {
          _stmt.close()
        }
      }
    }
  }

  public override fun unreadCount(threadId: Int): Int {
    val _sql: String =
        "SELECT COUNT('_id') FROM Conversations WHERE thread_id = ? OR Conversations.mms_thread_id = ? AND read = 0"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, threadId.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, threadId.toLong())
        val _result: Int
        if (_stmt.step()) {
          _result = _stmt.getLong(0).toInt()
        } else {
          _result = 0
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getThread(threadId: Int): Threads? {
    val _sql: String = "SELECT * FROM Threads WHERE threadId = ?"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, threadId.toLong())
        val _columnIndexOfThreadId: Int = getColumnIndexOrThrow(_stmt, "threadId")
        val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
        val _columnIndexOfSnippet: Int = getColumnIndexOrThrow(_stmt, "snippet")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfConversationId: Int = getColumnIndexOrThrow(_stmt, "conversationId")
        val _columnIndexOfIsMms: Int = getColumnIndexOrThrow(_stmt, "isMms")
        val _columnIndexOfIsMute: Int = getColumnIndexOrThrow(_stmt, "isMute")
        val _columnIndexOfIsArchive: Int = getColumnIndexOrThrow(_stmt, "isArchive")
        val _columnIndexOfIsBlocked: Int = getColumnIndexOrThrow(_stmt, "isBlocked")
        val _columnIndexOfUnread: Int = getColumnIndexOrThrow(_stmt, "unread")
        val _columnIndexOfUnreadCount: Int = getColumnIndexOrThrow(_stmt, "unreadCount")
        val _result: Threads?
        if (_stmt.step()) {
          val _tmpThreadId: Int
          _tmpThreadId = _stmt.getLong(_columnIndexOfThreadId).toInt()
          val _tmpAddress: String
          _tmpAddress = _stmt.getText(_columnIndexOfAddress)
          val _tmpSnippet: String
          _tmpSnippet = _stmt.getText(_columnIndexOfSnippet)
          val _tmpDate: Long
          _tmpDate = _stmt.getLong(_columnIndexOfDate)
          val _tmpType: Int
          _tmpType = _stmt.getLong(_columnIndexOfType).toInt()
          val _tmpConversationId: Long
          _tmpConversationId = _stmt.getLong(_columnIndexOfConversationId)
          val _tmpIsMms: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsMms).toInt()
          _tmpIsMms = _tmp != 0
          val _tmpIsMute: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsMute).toInt()
          _tmpIsMute = _tmp_1 != 0
          val _tmpIsArchive: Boolean
          val _tmp_2: Int
          _tmp_2 = _stmt.getLong(_columnIndexOfIsArchive).toInt()
          _tmpIsArchive = _tmp_2 != 0
          val _tmpIsBlocked: Boolean
          val _tmp_3: Int
          _tmp_3 = _stmt.getLong(_columnIndexOfIsBlocked).toInt()
          _tmpIsBlocked = _tmp_3 != 0
          val _tmpUnread: Boolean
          val _tmp_4: Int
          _tmp_4 = _stmt.getLong(_columnIndexOfUnread).toInt()
          _tmpUnread = _tmp_4 != 0
          val _tmpUnreadCount: Int
          _tmpUnreadCount = _stmt.getLong(_columnIndexOfUnreadCount).toInt()
          _result =
              Threads(_tmpThreadId,_tmpAddress,_tmpSnippet,_tmpDate,_tmpType,_tmpConversationId,_tmpIsMms,_tmpIsMute,_tmpIsArchive,_tmpIsBlocked,_tmpUnread,_tmpUnreadCount)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun fetchConversationsForType(threadId: Int, type: Int): Conversations? {
    val _sql: String =
        "SELECT * FROM Conversations WHERE (thread_id = ? OR Conversations.mms_thread_id = ?) AND type = ? ORDER BY  date DESC LIMIT 1"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, threadId.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, threadId.toLong())
        _argIndex = 3
        _stmt.bindLong(_argIndex, type.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfSmsData: Int = getColumnIndexOrThrow(_stmt, "sms_data")
        val _columnIndexOfMmsText: Int = getColumnIndexOrThrow(_stmt, "mms_text")
        val _columnIndexOfMmsContentUri: Int = getColumnIndexOrThrow(_stmt, "mms_content_uri")
        val _columnIndexOfMmsMimetype: Int = getColumnIndexOrThrow(_stmt, "mms_mimetype")
        val _columnIndexOfMmsFilename: Int = getColumnIndexOrThrow(_stmt, "mms_filename")
        val _columnIndexOfMmsFilepath: Int = getColumnIndexOrThrow(_stmt, "mms_filepath")
        val _columnIndexOfId_1: Int = getColumnIndexOrThrow(_stmt, "_id")
        val _columnIndexOfThreadId: Int = getColumnIndexOrThrow(_stmt, "thread_id")
        val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
        val _columnIndexOfPerson: Int = getColumnIndexOrThrow(_stmt, "person")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfDateSent: Int = getColumnIndexOrThrow(_stmt, "date_sent")
        val _columnIndexOfProtocol: Int = getColumnIndexOrThrow(_stmt, "protocol")
        val _columnIndexOfRead: Int = getColumnIndexOrThrow(_stmt, "read")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfReplyPathPresent: Int = getColumnIndexOrThrow(_stmt, "reply_path_present")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfServiceCenter: Int = getColumnIndexOrThrow(_stmt, "service_center")
        val _columnIndexOfLocked: Int = getColumnIndexOrThrow(_stmt, "locked")
        val _columnIndexOfSubId: Int = getColumnIndexOrThrow(_stmt, "sub_id")
        val _columnIndexOfErrorCode: Int = getColumnIndexOrThrow(_stmt, "error_code")
        val _columnIndexOfCreator: Int = getColumnIndexOrThrow(_stmt, "creator")
        val _columnIndexOfSeen: Int = getColumnIndexOrThrow(_stmt, "seen")
        val _columnIndexOfId_2: Int = getColumnIndexOrThrow(_stmt, "mms__id")
        val _columnIndexOfThreadId_1: Int = getColumnIndexOrThrow(_stmt, "mms_thread_id")
        val _columnIndexOfDate_1: Int = getColumnIndexOrThrow(_stmt, "mms_date")
        val _columnIndexOfDateSent_1: Int = getColumnIndexOrThrow(_stmt, "mms_date_sent")
        val _columnIndexOfMsgBox: Int = getColumnIndexOrThrow(_stmt, "mms_msg_box")
        val _columnIndexOfRead_1: Int = getColumnIndexOrThrow(_stmt, "mms_read")
        val _columnIndexOfMId: Int = getColumnIndexOrThrow(_stmt, "mms_m_id")
        val _columnIndexOfSub: Int = getColumnIndexOrThrow(_stmt, "mms_sub")
        val _columnIndexOfSubCs: Int = getColumnIndexOrThrow(_stmt, "mms_sub_cs")
        val _columnIndexOfCtT: Int = getColumnIndexOrThrow(_stmt, "mms_ct_t")
        val _columnIndexOfCtL: Int = getColumnIndexOrThrow(_stmt, "mms_ct_l")
        val _columnIndexOfExp: Int = getColumnIndexOrThrow(_stmt, "mms_exp")
        val _columnIndexOfMCls: Int = getColumnIndexOrThrow(_stmt, "mms_m_cls")
        val _columnIndexOfMType: Int = getColumnIndexOrThrow(_stmt, "mms_m_type")
        val _columnIndexOfV: Int = getColumnIndexOrThrow(_stmt, "mms_v")
        val _columnIndexOfMSize: Int = getColumnIndexOrThrow(_stmt, "mms_m_size")
        val _columnIndexOfPri: Int = getColumnIndexOrThrow(_stmt, "mms_pri")
        val _columnIndexOfRr: Int = getColumnIndexOrThrow(_stmt, "mms_rr")
        val _columnIndexOfRptA: Int = getColumnIndexOrThrow(_stmt, "mms_rpt_a")
        val _columnIndexOfRespSt: Int = getColumnIndexOrThrow(_stmt, "mms_resp_st")
        val _columnIndexOfSt: Int = getColumnIndexOrThrow(_stmt, "mms_st")
        val _columnIndexOfTrId: Int = getColumnIndexOrThrow(_stmt, "mms_tr_id")
        val _columnIndexOfRetrSt: Int = getColumnIndexOrThrow(_stmt, "mms_retr_st")
        val _columnIndexOfRetrTxt: Int = getColumnIndexOrThrow(_stmt, "mms_retr_txt")
        val _columnIndexOfRetrTxtCs: Int = getColumnIndexOrThrow(_stmt, "mms_retr_txt_cs")
        val _columnIndexOfReadStatus: Int = getColumnIndexOrThrow(_stmt, "mms_read_status")
        val _columnIndexOfCtCls: Int = getColumnIndexOrThrow(_stmt, "mms_ct_cls")
        val _columnIndexOfRespTxt: Int = getColumnIndexOrThrow(_stmt, "mms_resp_txt")
        val _columnIndexOfDTm: Int = getColumnIndexOrThrow(_stmt, "mms_d_tm")
        val _columnIndexOfDRpt: Int = getColumnIndexOrThrow(_stmt, "mms_d_rpt")
        val _columnIndexOfLocked_1: Int = getColumnIndexOrThrow(_stmt, "mms_locked")
        val _columnIndexOfSubId_1: Int = getColumnIndexOrThrow(_stmt, "mms_sub_id")
        val _columnIndexOfSeen_1: Int = getColumnIndexOrThrow(_stmt, "mms_seen")
        val _columnIndexOfCreator_1: Int = getColumnIndexOrThrow(_stmt, "mms_creator")
        val _columnIndexOfTextOnly: Int = getColumnIndexOrThrow(_stmt, "mms_text_only")
        val _result: Conversations?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpSms_data: ByteArray?
          if (_stmt.isNull(_columnIndexOfSmsData)) {
            _tmpSms_data = null
          } else {
            _tmpSms_data = _stmt.getBlob(_columnIndexOfSmsData)
          }
          val _tmpMms_text: String?
          if (_stmt.isNull(_columnIndexOfMmsText)) {
            _tmpMms_text = null
          } else {
            _tmpMms_text = _stmt.getText(_columnIndexOfMmsText)
          }
          val _tmpMms_content_uri: String?
          if (_stmt.isNull(_columnIndexOfMmsContentUri)) {
            _tmpMms_content_uri = null
          } else {
            _tmpMms_content_uri = _stmt.getText(_columnIndexOfMmsContentUri)
          }
          val _tmpMms_mimetype: String?
          if (_stmt.isNull(_columnIndexOfMmsMimetype)) {
            _tmpMms_mimetype = null
          } else {
            _tmpMms_mimetype = _stmt.getText(_columnIndexOfMmsMimetype)
          }
          val _tmpMms_filename: String?
          if (_stmt.isNull(_columnIndexOfMmsFilename)) {
            _tmpMms_filename = null
          } else {
            _tmpMms_filename = _stmt.getText(_columnIndexOfMmsFilename)
          }
          val _tmpMms_filepath: String?
          if (_stmt.isNull(_columnIndexOfMmsFilepath)) {
            _tmpMms_filepath = null
          } else {
            _tmpMms_filepath = _stmt.getText(_columnIndexOfMmsFilepath)
          }
          val _tmpSms: SmsMmsNatives.Sms?
          if (!(_stmt.isNull(_columnIndexOfId_1) && _stmt.isNull(_columnIndexOfThreadId) &&
              _stmt.isNull(_columnIndexOfAddress) && _stmt.isNull(_columnIndexOfPerson) &&
              _stmt.isNull(_columnIndexOfDate) && _stmt.isNull(_columnIndexOfDateSent) &&
              _stmt.isNull(_columnIndexOfProtocol) && _stmt.isNull(_columnIndexOfRead) &&
              _stmt.isNull(_columnIndexOfStatus) && _stmt.isNull(_columnIndexOfType) &&
              _stmt.isNull(_columnIndexOfReplyPathPresent) && _stmt.isNull(_columnIndexOfSubject) &&
              _stmt.isNull(_columnIndexOfBody) && _stmt.isNull(_columnIndexOfServiceCenter) &&
              _stmt.isNull(_columnIndexOfLocked) && _stmt.isNull(_columnIndexOfSubId) &&
              _stmt.isNull(_columnIndexOfErrorCode) && _stmt.isNull(_columnIndexOfCreator) &&
              _stmt.isNull(_columnIndexOfSeen))) {
            val _tmp_id: Long?
            if (_stmt.isNull(_columnIndexOfId_1)) {
              _tmp_id = null
            } else {
              _tmp_id = _stmt.getLong(_columnIndexOfId_1)
            }
            val _tmpThread_id: Int
            _tmpThread_id = _stmt.getLong(_columnIndexOfThreadId).toInt()
            val _tmpAddress: String?
            if (_stmt.isNull(_columnIndexOfAddress)) {
              _tmpAddress = null
            } else {
              _tmpAddress = _stmt.getText(_columnIndexOfAddress)
            }
            val _tmpPerson: String?
            if (_stmt.isNull(_columnIndexOfPerson)) {
              _tmpPerson = null
            } else {
              _tmpPerson = _stmt.getText(_columnIndexOfPerson)
            }
            val _tmpDate: Long
            _tmpDate = _stmt.getLong(_columnIndexOfDate)
            val _tmpDate_sent: Long
            _tmpDate_sent = _stmt.getLong(_columnIndexOfDateSent)
            val _tmpProtocol: String?
            if (_stmt.isNull(_columnIndexOfProtocol)) {
              _tmpProtocol = null
            } else {
              _tmpProtocol = _stmt.getText(_columnIndexOfProtocol)
            }
            val _tmpRead: Int
            _tmpRead = _stmt.getLong(_columnIndexOfRead).toInt()
            val _tmpStatus: Int
            _tmpStatus = _stmt.getLong(_columnIndexOfStatus).toInt()
            val _tmpType: Int
            _tmpType = _stmt.getLong(_columnIndexOfType).toInt()
            val _tmpReply_path_present: String?
            if (_stmt.isNull(_columnIndexOfReplyPathPresent)) {
              _tmpReply_path_present = null
            } else {
              _tmpReply_path_present = _stmt.getText(_columnIndexOfReplyPathPresent)
            }
            val _tmpSubject: String?
            if (_stmt.isNull(_columnIndexOfSubject)) {
              _tmpSubject = null
            } else {
              _tmpSubject = _stmt.getText(_columnIndexOfSubject)
            }
            val _tmpBody: String?
            if (_stmt.isNull(_columnIndexOfBody)) {
              _tmpBody = null
            } else {
              _tmpBody = _stmt.getText(_columnIndexOfBody)
            }
            val _tmpService_center: String?
            if (_stmt.isNull(_columnIndexOfServiceCenter)) {
              _tmpService_center = null
            } else {
              _tmpService_center = _stmt.getText(_columnIndexOfServiceCenter)
            }
            val _tmpLocked: Int?
            if (_stmt.isNull(_columnIndexOfLocked)) {
              _tmpLocked = null
            } else {
              _tmpLocked = _stmt.getLong(_columnIndexOfLocked).toInt()
            }
            val _tmpSub_id: Long
            _tmpSub_id = _stmt.getLong(_columnIndexOfSubId)
            val _tmpError_code: Int?
            if (_stmt.isNull(_columnIndexOfErrorCode)) {
              _tmpError_code = null
            } else {
              _tmpError_code = _stmt.getLong(_columnIndexOfErrorCode).toInt()
            }
            val _tmpCreator: String?
            if (_stmt.isNull(_columnIndexOfCreator)) {
              _tmpCreator = null
            } else {
              _tmpCreator = _stmt.getText(_columnIndexOfCreator)
            }
            val _tmpSeen: Int?
            if (_stmt.isNull(_columnIndexOfSeen)) {
              _tmpSeen = null
            } else {
              _tmpSeen = _stmt.getLong(_columnIndexOfSeen).toInt()
            }
            _tmpSms =
                SmsMmsNatives.Sms(_tmp_id,_tmpThread_id,_tmpAddress,_tmpPerson,_tmpDate,_tmpDate_sent,_tmpProtocol,_tmpRead,_tmpStatus,_tmpType,_tmpReply_path_present,_tmpSubject,_tmpBody,_tmpService_center,_tmpLocked,_tmpSub_id,_tmpError_code,_tmpCreator,_tmpSeen)
          } else {
            _tmpSms = null
          }
          val _tmpMms: SmsMmsNatives.Mms?
          if (!(_stmt.isNull(_columnIndexOfId_2) && _stmt.isNull(_columnIndexOfThreadId_1) &&
              _stmt.isNull(_columnIndexOfDate_1) && _stmt.isNull(_columnIndexOfDateSent_1) &&
              _stmt.isNull(_columnIndexOfMsgBox) && _stmt.isNull(_columnIndexOfRead_1) &&
              _stmt.isNull(_columnIndexOfMId) && _stmt.isNull(_columnIndexOfSub) &&
              _stmt.isNull(_columnIndexOfSubCs) && _stmt.isNull(_columnIndexOfCtT) &&
              _stmt.isNull(_columnIndexOfCtL) && _stmt.isNull(_columnIndexOfExp) &&
              _stmt.isNull(_columnIndexOfMCls) && _stmt.isNull(_columnIndexOfMType) &&
              _stmt.isNull(_columnIndexOfV) && _stmt.isNull(_columnIndexOfMSize) &&
              _stmt.isNull(_columnIndexOfPri) && _stmt.isNull(_columnIndexOfRr) &&
              _stmt.isNull(_columnIndexOfRptA) && _stmt.isNull(_columnIndexOfRespSt) &&
              _stmt.isNull(_columnIndexOfSt) && _stmt.isNull(_columnIndexOfTrId) &&
              _stmt.isNull(_columnIndexOfRetrSt) && _stmt.isNull(_columnIndexOfRetrTxt) &&
              _stmt.isNull(_columnIndexOfRetrTxtCs) && _stmt.isNull(_columnIndexOfReadStatus) &&
              _stmt.isNull(_columnIndexOfCtCls) && _stmt.isNull(_columnIndexOfRespTxt) &&
              _stmt.isNull(_columnIndexOfDTm) && _stmt.isNull(_columnIndexOfDRpt) &&
              _stmt.isNull(_columnIndexOfLocked_1) && _stmt.isNull(_columnIndexOfSubId_1) &&
              _stmt.isNull(_columnIndexOfSeen_1) && _stmt.isNull(_columnIndexOfCreator_1) &&
              _stmt.isNull(_columnIndexOfTextOnly))) {
            val _tmp_id_1: Long
            _tmp_id_1 = _stmt.getLong(_columnIndexOfId_2)
            val _tmpThread_id_1: Int
            _tmpThread_id_1 = _stmt.getLong(_columnIndexOfThreadId_1).toInt()
            val _tmpDate_1: Long
            _tmpDate_1 = _stmt.getLong(_columnIndexOfDate_1)
            val _tmpDate_sent_1: Long
            _tmpDate_sent_1 = _stmt.getLong(_columnIndexOfDateSent_1)
            val _tmpMsg_box: Int
            _tmpMsg_box = _stmt.getLong(_columnIndexOfMsgBox).toInt()
            val _tmpRead_1: Int?
            if (_stmt.isNull(_columnIndexOfRead_1)) {
              _tmpRead_1 = null
            } else {
              _tmpRead_1 = _stmt.getLong(_columnIndexOfRead_1).toInt()
            }
            val _tmpM_id: String?
            if (_stmt.isNull(_columnIndexOfMId)) {
              _tmpM_id = null
            } else {
              _tmpM_id = _stmt.getText(_columnIndexOfMId)
            }
            val _tmpSub: String?
            if (_stmt.isNull(_columnIndexOfSub)) {
              _tmpSub = null
            } else {
              _tmpSub = _stmt.getText(_columnIndexOfSub)
            }
            val _tmpSub_cs: Int?
            if (_stmt.isNull(_columnIndexOfSubCs)) {
              _tmpSub_cs = null
            } else {
              _tmpSub_cs = _stmt.getLong(_columnIndexOfSubCs).toInt()
            }
            val _tmpCt_t: String?
            if (_stmt.isNull(_columnIndexOfCtT)) {
              _tmpCt_t = null
            } else {
              _tmpCt_t = _stmt.getText(_columnIndexOfCtT)
            }
            val _tmpCt_l: String?
            if (_stmt.isNull(_columnIndexOfCtL)) {
              _tmpCt_l = null
            } else {
              _tmpCt_l = _stmt.getText(_columnIndexOfCtL)
            }
            val _tmpExp: String?
            if (_stmt.isNull(_columnIndexOfExp)) {
              _tmpExp = null
            } else {
              _tmpExp = _stmt.getText(_columnIndexOfExp)
            }
            val _tmpM_cls: String?
            if (_stmt.isNull(_columnIndexOfMCls)) {
              _tmpM_cls = null
            } else {
              _tmpM_cls = _stmt.getText(_columnIndexOfMCls)
            }
            val _tmpM_type: Int?
            if (_stmt.isNull(_columnIndexOfMType)) {
              _tmpM_type = null
            } else {
              _tmpM_type = _stmt.getLong(_columnIndexOfMType).toInt()
            }
            val _tmpV: Int?
            if (_stmt.isNull(_columnIndexOfV)) {
              _tmpV = null
            } else {
              _tmpV = _stmt.getLong(_columnIndexOfV).toInt()
            }
            val _tmpM_size: Int?
            if (_stmt.isNull(_columnIndexOfMSize)) {
              _tmpM_size = null
            } else {
              _tmpM_size = _stmt.getLong(_columnIndexOfMSize).toInt()
            }
            val _tmpPri: Int?
            if (_stmt.isNull(_columnIndexOfPri)) {
              _tmpPri = null
            } else {
              _tmpPri = _stmt.getLong(_columnIndexOfPri).toInt()
            }
            val _tmpRr: Int?
            if (_stmt.isNull(_columnIndexOfRr)) {
              _tmpRr = null
            } else {
              _tmpRr = _stmt.getLong(_columnIndexOfRr).toInt()
            }
            val _tmpRpt_a: String?
            if (_stmt.isNull(_columnIndexOfRptA)) {
              _tmpRpt_a = null
            } else {
              _tmpRpt_a = _stmt.getText(_columnIndexOfRptA)
            }
            val _tmpResp_st: String?
            if (_stmt.isNull(_columnIndexOfRespSt)) {
              _tmpResp_st = null
            } else {
              _tmpResp_st = _stmt.getText(_columnIndexOfRespSt)
            }
            val _tmpSt: String?
            if (_stmt.isNull(_columnIndexOfSt)) {
              _tmpSt = null
            } else {
              _tmpSt = _stmt.getText(_columnIndexOfSt)
            }
            val _tmpTr_id: String?
            if (_stmt.isNull(_columnIndexOfTrId)) {
              _tmpTr_id = null
            } else {
              _tmpTr_id = _stmt.getText(_columnIndexOfTrId)
            }
            val _tmpRetr_st: String?
            if (_stmt.isNull(_columnIndexOfRetrSt)) {
              _tmpRetr_st = null
            } else {
              _tmpRetr_st = _stmt.getText(_columnIndexOfRetrSt)
            }
            val _tmpRetr_txt: String?
            if (_stmt.isNull(_columnIndexOfRetrTxt)) {
              _tmpRetr_txt = null
            } else {
              _tmpRetr_txt = _stmt.getText(_columnIndexOfRetrTxt)
            }
            val _tmpRetr_txt_cs: String?
            if (_stmt.isNull(_columnIndexOfRetrTxtCs)) {
              _tmpRetr_txt_cs = null
            } else {
              _tmpRetr_txt_cs = _stmt.getText(_columnIndexOfRetrTxtCs)
            }
            val _tmpRead_status: String?
            if (_stmt.isNull(_columnIndexOfReadStatus)) {
              _tmpRead_status = null
            } else {
              _tmpRead_status = _stmt.getText(_columnIndexOfReadStatus)
            }
            val _tmpCt_cls: String?
            if (_stmt.isNull(_columnIndexOfCtCls)) {
              _tmpCt_cls = null
            } else {
              _tmpCt_cls = _stmt.getText(_columnIndexOfCtCls)
            }
            val _tmpResp_txt: String?
            if (_stmt.isNull(_columnIndexOfRespTxt)) {
              _tmpResp_txt = null
            } else {
              _tmpResp_txt = _stmt.getText(_columnIndexOfRespTxt)
            }
            val _tmpD_tm: String?
            if (_stmt.isNull(_columnIndexOfDTm)) {
              _tmpD_tm = null
            } else {
              _tmpD_tm = _stmt.getText(_columnIndexOfDTm)
            }
            val _tmpD_rpt: Int?
            if (_stmt.isNull(_columnIndexOfDRpt)) {
              _tmpD_rpt = null
            } else {
              _tmpD_rpt = _stmt.getLong(_columnIndexOfDRpt).toInt()
            }
            val _tmpLocked_1: Int?
            if (_stmt.isNull(_columnIndexOfLocked_1)) {
              _tmpLocked_1 = null
            } else {
              _tmpLocked_1 = _stmt.getLong(_columnIndexOfLocked_1).toInt()
            }
            val _tmpSub_id_1: Long?
            if (_stmt.isNull(_columnIndexOfSubId_1)) {
              _tmpSub_id_1 = null
            } else {
              _tmpSub_id_1 = _stmt.getLong(_columnIndexOfSubId_1)
            }
            val _tmpSeen_1: Int?
            if (_stmt.isNull(_columnIndexOfSeen_1)) {
              _tmpSeen_1 = null
            } else {
              _tmpSeen_1 = _stmt.getLong(_columnIndexOfSeen_1).toInt()
            }
            val _tmpCreator_1: String?
            if (_stmt.isNull(_columnIndexOfCreator_1)) {
              _tmpCreator_1 = null
            } else {
              _tmpCreator_1 = _stmt.getText(_columnIndexOfCreator_1)
            }
            val _tmpText_only: Int?
            if (_stmt.isNull(_columnIndexOfTextOnly)) {
              _tmpText_only = null
            } else {
              _tmpText_only = _stmt.getLong(_columnIndexOfTextOnly).toInt()
            }
            _tmpMms =
                SmsMmsNatives.Mms(_tmp_id_1,_tmpThread_id_1,_tmpDate_1,_tmpDate_sent_1,_tmpMsg_box,_tmpRead_1,_tmpM_id,_tmpSub,_tmpSub_cs,_tmpCt_t,_tmpCt_l,_tmpExp,_tmpM_cls,_tmpM_type,_tmpV,_tmpM_size,_tmpPri,_tmpRr,_tmpRpt_a,_tmpResp_st,_tmpSt,_tmpTr_id,_tmpRetr_st,_tmpRetr_txt,_tmpRetr_txt_cs,_tmpRead_status,_tmpCt_cls,_tmpResp_txt,_tmpD_tm,_tmpD_rpt,_tmpLocked_1,_tmpSub_id_1,_tmpSeen_1,_tmpCreator_1,_tmpText_only)
          } else {
            _tmpMms = null
          }
          _result =
              Conversations(_tmpId,_tmpSms,_tmpMms,_tmpSms_data,_tmpMms_text,_tmpMms_content_uri,_tmpMms_mimetype,_tmpMms_filename,_tmpMms_filepath)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getConversationsList(threadId: Int): List<Conversations> {
    val _sql: String =
        "SELECT * FROM Conversations WHERE (thread_id = ? OR Conversations.mms_thread_id = ?) ORDER BY date DESC"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, threadId.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, threadId.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfSmsData: Int = getColumnIndexOrThrow(_stmt, "sms_data")
        val _columnIndexOfMmsText: Int = getColumnIndexOrThrow(_stmt, "mms_text")
        val _columnIndexOfMmsContentUri: Int = getColumnIndexOrThrow(_stmt, "mms_content_uri")
        val _columnIndexOfMmsMimetype: Int = getColumnIndexOrThrow(_stmt, "mms_mimetype")
        val _columnIndexOfMmsFilename: Int = getColumnIndexOrThrow(_stmt, "mms_filename")
        val _columnIndexOfMmsFilepath: Int = getColumnIndexOrThrow(_stmt, "mms_filepath")
        val _columnIndexOfId_1: Int = getColumnIndexOrThrow(_stmt, "_id")
        val _columnIndexOfThreadId: Int = getColumnIndexOrThrow(_stmt, "thread_id")
        val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
        val _columnIndexOfPerson: Int = getColumnIndexOrThrow(_stmt, "person")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfDateSent: Int = getColumnIndexOrThrow(_stmt, "date_sent")
        val _columnIndexOfProtocol: Int = getColumnIndexOrThrow(_stmt, "protocol")
        val _columnIndexOfRead: Int = getColumnIndexOrThrow(_stmt, "read")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfReplyPathPresent: Int = getColumnIndexOrThrow(_stmt, "reply_path_present")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfServiceCenter: Int = getColumnIndexOrThrow(_stmt, "service_center")
        val _columnIndexOfLocked: Int = getColumnIndexOrThrow(_stmt, "locked")
        val _columnIndexOfSubId: Int = getColumnIndexOrThrow(_stmt, "sub_id")
        val _columnIndexOfErrorCode: Int = getColumnIndexOrThrow(_stmt, "error_code")
        val _columnIndexOfCreator: Int = getColumnIndexOrThrow(_stmt, "creator")
        val _columnIndexOfSeen: Int = getColumnIndexOrThrow(_stmt, "seen")
        val _columnIndexOfId_2: Int = getColumnIndexOrThrow(_stmt, "mms__id")
        val _columnIndexOfThreadId_1: Int = getColumnIndexOrThrow(_stmt, "mms_thread_id")
        val _columnIndexOfDate_1: Int = getColumnIndexOrThrow(_stmt, "mms_date")
        val _columnIndexOfDateSent_1: Int = getColumnIndexOrThrow(_stmt, "mms_date_sent")
        val _columnIndexOfMsgBox: Int = getColumnIndexOrThrow(_stmt, "mms_msg_box")
        val _columnIndexOfRead_1: Int = getColumnIndexOrThrow(_stmt, "mms_read")
        val _columnIndexOfMId: Int = getColumnIndexOrThrow(_stmt, "mms_m_id")
        val _columnIndexOfSub: Int = getColumnIndexOrThrow(_stmt, "mms_sub")
        val _columnIndexOfSubCs: Int = getColumnIndexOrThrow(_stmt, "mms_sub_cs")
        val _columnIndexOfCtT: Int = getColumnIndexOrThrow(_stmt, "mms_ct_t")
        val _columnIndexOfCtL: Int = getColumnIndexOrThrow(_stmt, "mms_ct_l")
        val _columnIndexOfExp: Int = getColumnIndexOrThrow(_stmt, "mms_exp")
        val _columnIndexOfMCls: Int = getColumnIndexOrThrow(_stmt, "mms_m_cls")
        val _columnIndexOfMType: Int = getColumnIndexOrThrow(_stmt, "mms_m_type")
        val _columnIndexOfV: Int = getColumnIndexOrThrow(_stmt, "mms_v")
        val _columnIndexOfMSize: Int = getColumnIndexOrThrow(_stmt, "mms_m_size")
        val _columnIndexOfPri: Int = getColumnIndexOrThrow(_stmt, "mms_pri")
        val _columnIndexOfRr: Int = getColumnIndexOrThrow(_stmt, "mms_rr")
        val _columnIndexOfRptA: Int = getColumnIndexOrThrow(_stmt, "mms_rpt_a")
        val _columnIndexOfRespSt: Int = getColumnIndexOrThrow(_stmt, "mms_resp_st")
        val _columnIndexOfSt: Int = getColumnIndexOrThrow(_stmt, "mms_st")
        val _columnIndexOfTrId: Int = getColumnIndexOrThrow(_stmt, "mms_tr_id")
        val _columnIndexOfRetrSt: Int = getColumnIndexOrThrow(_stmt, "mms_retr_st")
        val _columnIndexOfRetrTxt: Int = getColumnIndexOrThrow(_stmt, "mms_retr_txt")
        val _columnIndexOfRetrTxtCs: Int = getColumnIndexOrThrow(_stmt, "mms_retr_txt_cs")
        val _columnIndexOfReadStatus: Int = getColumnIndexOrThrow(_stmt, "mms_read_status")
        val _columnIndexOfCtCls: Int = getColumnIndexOrThrow(_stmt, "mms_ct_cls")
        val _columnIndexOfRespTxt: Int = getColumnIndexOrThrow(_stmt, "mms_resp_txt")
        val _columnIndexOfDTm: Int = getColumnIndexOrThrow(_stmt, "mms_d_tm")
        val _columnIndexOfDRpt: Int = getColumnIndexOrThrow(_stmt, "mms_d_rpt")
        val _columnIndexOfLocked_1: Int = getColumnIndexOrThrow(_stmt, "mms_locked")
        val _columnIndexOfSubId_1: Int = getColumnIndexOrThrow(_stmt, "mms_sub_id")
        val _columnIndexOfSeen_1: Int = getColumnIndexOrThrow(_stmt, "mms_seen")
        val _columnIndexOfCreator_1: Int = getColumnIndexOrThrow(_stmt, "mms_creator")
        val _columnIndexOfTextOnly: Int = getColumnIndexOrThrow(_stmt, "mms_text_only")
        val _result: MutableList<Conversations> = mutableListOf()
        while (_stmt.step()) {
          val _item: Conversations
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpSms_data: ByteArray?
          if (_stmt.isNull(_columnIndexOfSmsData)) {
            _tmpSms_data = null
          } else {
            _tmpSms_data = _stmt.getBlob(_columnIndexOfSmsData)
          }
          val _tmpMms_text: String?
          if (_stmt.isNull(_columnIndexOfMmsText)) {
            _tmpMms_text = null
          } else {
            _tmpMms_text = _stmt.getText(_columnIndexOfMmsText)
          }
          val _tmpMms_content_uri: String?
          if (_stmt.isNull(_columnIndexOfMmsContentUri)) {
            _tmpMms_content_uri = null
          } else {
            _tmpMms_content_uri = _stmt.getText(_columnIndexOfMmsContentUri)
          }
          val _tmpMms_mimetype: String?
          if (_stmt.isNull(_columnIndexOfMmsMimetype)) {
            _tmpMms_mimetype = null
          } else {
            _tmpMms_mimetype = _stmt.getText(_columnIndexOfMmsMimetype)
          }
          val _tmpMms_filename: String?
          if (_stmt.isNull(_columnIndexOfMmsFilename)) {
            _tmpMms_filename = null
          } else {
            _tmpMms_filename = _stmt.getText(_columnIndexOfMmsFilename)
          }
          val _tmpMms_filepath: String?
          if (_stmt.isNull(_columnIndexOfMmsFilepath)) {
            _tmpMms_filepath = null
          } else {
            _tmpMms_filepath = _stmt.getText(_columnIndexOfMmsFilepath)
          }
          val _tmpSms: SmsMmsNatives.Sms?
          if (!(_stmt.isNull(_columnIndexOfId_1) && _stmt.isNull(_columnIndexOfThreadId) &&
              _stmt.isNull(_columnIndexOfAddress) && _stmt.isNull(_columnIndexOfPerson) &&
              _stmt.isNull(_columnIndexOfDate) && _stmt.isNull(_columnIndexOfDateSent) &&
              _stmt.isNull(_columnIndexOfProtocol) && _stmt.isNull(_columnIndexOfRead) &&
              _stmt.isNull(_columnIndexOfStatus) && _stmt.isNull(_columnIndexOfType) &&
              _stmt.isNull(_columnIndexOfReplyPathPresent) && _stmt.isNull(_columnIndexOfSubject) &&
              _stmt.isNull(_columnIndexOfBody) && _stmt.isNull(_columnIndexOfServiceCenter) &&
              _stmt.isNull(_columnIndexOfLocked) && _stmt.isNull(_columnIndexOfSubId) &&
              _stmt.isNull(_columnIndexOfErrorCode) && _stmt.isNull(_columnIndexOfCreator) &&
              _stmt.isNull(_columnIndexOfSeen))) {
            val _tmp_id: Long?
            if (_stmt.isNull(_columnIndexOfId_1)) {
              _tmp_id = null
            } else {
              _tmp_id = _stmt.getLong(_columnIndexOfId_1)
            }
            val _tmpThread_id: Int
            _tmpThread_id = _stmt.getLong(_columnIndexOfThreadId).toInt()
            val _tmpAddress: String?
            if (_stmt.isNull(_columnIndexOfAddress)) {
              _tmpAddress = null
            } else {
              _tmpAddress = _stmt.getText(_columnIndexOfAddress)
            }
            val _tmpPerson: String?
            if (_stmt.isNull(_columnIndexOfPerson)) {
              _tmpPerson = null
            } else {
              _tmpPerson = _stmt.getText(_columnIndexOfPerson)
            }
            val _tmpDate: Long
            _tmpDate = _stmt.getLong(_columnIndexOfDate)
            val _tmpDate_sent: Long
            _tmpDate_sent = _stmt.getLong(_columnIndexOfDateSent)
            val _tmpProtocol: String?
            if (_stmt.isNull(_columnIndexOfProtocol)) {
              _tmpProtocol = null
            } else {
              _tmpProtocol = _stmt.getText(_columnIndexOfProtocol)
            }
            val _tmpRead: Int
            _tmpRead = _stmt.getLong(_columnIndexOfRead).toInt()
            val _tmpStatus: Int
            _tmpStatus = _stmt.getLong(_columnIndexOfStatus).toInt()
            val _tmpType: Int
            _tmpType = _stmt.getLong(_columnIndexOfType).toInt()
            val _tmpReply_path_present: String?
            if (_stmt.isNull(_columnIndexOfReplyPathPresent)) {
              _tmpReply_path_present = null
            } else {
              _tmpReply_path_present = _stmt.getText(_columnIndexOfReplyPathPresent)
            }
            val _tmpSubject: String?
            if (_stmt.isNull(_columnIndexOfSubject)) {
              _tmpSubject = null
            } else {
              _tmpSubject = _stmt.getText(_columnIndexOfSubject)
            }
            val _tmpBody: String?
            if (_stmt.isNull(_columnIndexOfBody)) {
              _tmpBody = null
            } else {
              _tmpBody = _stmt.getText(_columnIndexOfBody)
            }
            val _tmpService_center: String?
            if (_stmt.isNull(_columnIndexOfServiceCenter)) {
              _tmpService_center = null
            } else {
              _tmpService_center = _stmt.getText(_columnIndexOfServiceCenter)
            }
            val _tmpLocked: Int?
            if (_stmt.isNull(_columnIndexOfLocked)) {
              _tmpLocked = null
            } else {
              _tmpLocked = _stmt.getLong(_columnIndexOfLocked).toInt()
            }
            val _tmpSub_id: Long
            _tmpSub_id = _stmt.getLong(_columnIndexOfSubId)
            val _tmpError_code: Int?
            if (_stmt.isNull(_columnIndexOfErrorCode)) {
              _tmpError_code = null
            } else {
              _tmpError_code = _stmt.getLong(_columnIndexOfErrorCode).toInt()
            }
            val _tmpCreator: String?
            if (_stmt.isNull(_columnIndexOfCreator)) {
              _tmpCreator = null
            } else {
              _tmpCreator = _stmt.getText(_columnIndexOfCreator)
            }
            val _tmpSeen: Int?
            if (_stmt.isNull(_columnIndexOfSeen)) {
              _tmpSeen = null
            } else {
              _tmpSeen = _stmt.getLong(_columnIndexOfSeen).toInt()
            }
            _tmpSms =
                SmsMmsNatives.Sms(_tmp_id,_tmpThread_id,_tmpAddress,_tmpPerson,_tmpDate,_tmpDate_sent,_tmpProtocol,_tmpRead,_tmpStatus,_tmpType,_tmpReply_path_present,_tmpSubject,_tmpBody,_tmpService_center,_tmpLocked,_tmpSub_id,_tmpError_code,_tmpCreator,_tmpSeen)
          } else {
            _tmpSms = null
          }
          val _tmpMms: SmsMmsNatives.Mms?
          if (!(_stmt.isNull(_columnIndexOfId_2) && _stmt.isNull(_columnIndexOfThreadId_1) &&
              _stmt.isNull(_columnIndexOfDate_1) && _stmt.isNull(_columnIndexOfDateSent_1) &&
              _stmt.isNull(_columnIndexOfMsgBox) && _stmt.isNull(_columnIndexOfRead_1) &&
              _stmt.isNull(_columnIndexOfMId) && _stmt.isNull(_columnIndexOfSub) &&
              _stmt.isNull(_columnIndexOfSubCs) && _stmt.isNull(_columnIndexOfCtT) &&
              _stmt.isNull(_columnIndexOfCtL) && _stmt.isNull(_columnIndexOfExp) &&
              _stmt.isNull(_columnIndexOfMCls) && _stmt.isNull(_columnIndexOfMType) &&
              _stmt.isNull(_columnIndexOfV) && _stmt.isNull(_columnIndexOfMSize) &&
              _stmt.isNull(_columnIndexOfPri) && _stmt.isNull(_columnIndexOfRr) &&
              _stmt.isNull(_columnIndexOfRptA) && _stmt.isNull(_columnIndexOfRespSt) &&
              _stmt.isNull(_columnIndexOfSt) && _stmt.isNull(_columnIndexOfTrId) &&
              _stmt.isNull(_columnIndexOfRetrSt) && _stmt.isNull(_columnIndexOfRetrTxt) &&
              _stmt.isNull(_columnIndexOfRetrTxtCs) && _stmt.isNull(_columnIndexOfReadStatus) &&
              _stmt.isNull(_columnIndexOfCtCls) && _stmt.isNull(_columnIndexOfRespTxt) &&
              _stmt.isNull(_columnIndexOfDTm) && _stmt.isNull(_columnIndexOfDRpt) &&
              _stmt.isNull(_columnIndexOfLocked_1) && _stmt.isNull(_columnIndexOfSubId_1) &&
              _stmt.isNull(_columnIndexOfSeen_1) && _stmt.isNull(_columnIndexOfCreator_1) &&
              _stmt.isNull(_columnIndexOfTextOnly))) {
            val _tmp_id_1: Long
            _tmp_id_1 = _stmt.getLong(_columnIndexOfId_2)
            val _tmpThread_id_1: Int
            _tmpThread_id_1 = _stmt.getLong(_columnIndexOfThreadId_1).toInt()
            val _tmpDate_1: Long
            _tmpDate_1 = _stmt.getLong(_columnIndexOfDate_1)
            val _tmpDate_sent_1: Long
            _tmpDate_sent_1 = _stmt.getLong(_columnIndexOfDateSent_1)
            val _tmpMsg_box: Int
            _tmpMsg_box = _stmt.getLong(_columnIndexOfMsgBox).toInt()
            val _tmpRead_1: Int?
            if (_stmt.isNull(_columnIndexOfRead_1)) {
              _tmpRead_1 = null
            } else {
              _tmpRead_1 = _stmt.getLong(_columnIndexOfRead_1).toInt()
            }
            val _tmpM_id: String?
            if (_stmt.isNull(_columnIndexOfMId)) {
              _tmpM_id = null
            } else {
              _tmpM_id = _stmt.getText(_columnIndexOfMId)
            }
            val _tmpSub: String?
            if (_stmt.isNull(_columnIndexOfSub)) {
              _tmpSub = null
            } else {
              _tmpSub = _stmt.getText(_columnIndexOfSub)
            }
            val _tmpSub_cs: Int?
            if (_stmt.isNull(_columnIndexOfSubCs)) {
              _tmpSub_cs = null
            } else {
              _tmpSub_cs = _stmt.getLong(_columnIndexOfSubCs).toInt()
            }
            val _tmpCt_t: String?
            if (_stmt.isNull(_columnIndexOfCtT)) {
              _tmpCt_t = null
            } else {
              _tmpCt_t = _stmt.getText(_columnIndexOfCtT)
            }
            val _tmpCt_l: String?
            if (_stmt.isNull(_columnIndexOfCtL)) {
              _tmpCt_l = null
            } else {
              _tmpCt_l = _stmt.getText(_columnIndexOfCtL)
            }
            val _tmpExp: String?
            if (_stmt.isNull(_columnIndexOfExp)) {
              _tmpExp = null
            } else {
              _tmpExp = _stmt.getText(_columnIndexOfExp)
            }
            val _tmpM_cls: String?
            if (_stmt.isNull(_columnIndexOfMCls)) {
              _tmpM_cls = null
            } else {
              _tmpM_cls = _stmt.getText(_columnIndexOfMCls)
            }
            val _tmpM_type: Int?
            if (_stmt.isNull(_columnIndexOfMType)) {
              _tmpM_type = null
            } else {
              _tmpM_type = _stmt.getLong(_columnIndexOfMType).toInt()
            }
            val _tmpV: Int?
            if (_stmt.isNull(_columnIndexOfV)) {
              _tmpV = null
            } else {
              _tmpV = _stmt.getLong(_columnIndexOfV).toInt()
            }
            val _tmpM_size: Int?
            if (_stmt.isNull(_columnIndexOfMSize)) {
              _tmpM_size = null
            } else {
              _tmpM_size = _stmt.getLong(_columnIndexOfMSize).toInt()
            }
            val _tmpPri: Int?
            if (_stmt.isNull(_columnIndexOfPri)) {
              _tmpPri = null
            } else {
              _tmpPri = _stmt.getLong(_columnIndexOfPri).toInt()
            }
            val _tmpRr: Int?
            if (_stmt.isNull(_columnIndexOfRr)) {
              _tmpRr = null
            } else {
              _tmpRr = _stmt.getLong(_columnIndexOfRr).toInt()
            }
            val _tmpRpt_a: String?
            if (_stmt.isNull(_columnIndexOfRptA)) {
              _tmpRpt_a = null
            } else {
              _tmpRpt_a = _stmt.getText(_columnIndexOfRptA)
            }
            val _tmpResp_st: String?
            if (_stmt.isNull(_columnIndexOfRespSt)) {
              _tmpResp_st = null
            } else {
              _tmpResp_st = _stmt.getText(_columnIndexOfRespSt)
            }
            val _tmpSt: String?
            if (_stmt.isNull(_columnIndexOfSt)) {
              _tmpSt = null
            } else {
              _tmpSt = _stmt.getText(_columnIndexOfSt)
            }
            val _tmpTr_id: String?
            if (_stmt.isNull(_columnIndexOfTrId)) {
              _tmpTr_id = null
            } else {
              _tmpTr_id = _stmt.getText(_columnIndexOfTrId)
            }
            val _tmpRetr_st: String?
            if (_stmt.isNull(_columnIndexOfRetrSt)) {
              _tmpRetr_st = null
            } else {
              _tmpRetr_st = _stmt.getText(_columnIndexOfRetrSt)
            }
            val _tmpRetr_txt: String?
            if (_stmt.isNull(_columnIndexOfRetrTxt)) {
              _tmpRetr_txt = null
            } else {
              _tmpRetr_txt = _stmt.getText(_columnIndexOfRetrTxt)
            }
            val _tmpRetr_txt_cs: String?
            if (_stmt.isNull(_columnIndexOfRetrTxtCs)) {
              _tmpRetr_txt_cs = null
            } else {
              _tmpRetr_txt_cs = _stmt.getText(_columnIndexOfRetrTxtCs)
            }
            val _tmpRead_status: String?
            if (_stmt.isNull(_columnIndexOfReadStatus)) {
              _tmpRead_status = null
            } else {
              _tmpRead_status = _stmt.getText(_columnIndexOfReadStatus)
            }
            val _tmpCt_cls: String?
            if (_stmt.isNull(_columnIndexOfCtCls)) {
              _tmpCt_cls = null
            } else {
              _tmpCt_cls = _stmt.getText(_columnIndexOfCtCls)
            }
            val _tmpResp_txt: String?
            if (_stmt.isNull(_columnIndexOfRespTxt)) {
              _tmpResp_txt = null
            } else {
              _tmpResp_txt = _stmt.getText(_columnIndexOfRespTxt)
            }
            val _tmpD_tm: String?
            if (_stmt.isNull(_columnIndexOfDTm)) {
              _tmpD_tm = null
            } else {
              _tmpD_tm = _stmt.getText(_columnIndexOfDTm)
            }
            val _tmpD_rpt: Int?
            if (_stmt.isNull(_columnIndexOfDRpt)) {
              _tmpD_rpt = null
            } else {
              _tmpD_rpt = _stmt.getLong(_columnIndexOfDRpt).toInt()
            }
            val _tmpLocked_1: Int?
            if (_stmt.isNull(_columnIndexOfLocked_1)) {
              _tmpLocked_1 = null
            } else {
              _tmpLocked_1 = _stmt.getLong(_columnIndexOfLocked_1).toInt()
            }
            val _tmpSub_id_1: Long?
            if (_stmt.isNull(_columnIndexOfSubId_1)) {
              _tmpSub_id_1 = null
            } else {
              _tmpSub_id_1 = _stmt.getLong(_columnIndexOfSubId_1)
            }
            val _tmpSeen_1: Int?
            if (_stmt.isNull(_columnIndexOfSeen_1)) {
              _tmpSeen_1 = null
            } else {
              _tmpSeen_1 = _stmt.getLong(_columnIndexOfSeen_1).toInt()
            }
            val _tmpCreator_1: String?
            if (_stmt.isNull(_columnIndexOfCreator_1)) {
              _tmpCreator_1 = null
            } else {
              _tmpCreator_1 = _stmt.getText(_columnIndexOfCreator_1)
            }
            val _tmpText_only: Int?
            if (_stmt.isNull(_columnIndexOfTextOnly)) {
              _tmpText_only = null
            } else {
              _tmpText_only = _stmt.getLong(_columnIndexOfTextOnly).toInt()
            }
            _tmpMms =
                SmsMmsNatives.Mms(_tmp_id_1,_tmpThread_id_1,_tmpDate_1,_tmpDate_sent_1,_tmpMsg_box,_tmpRead_1,_tmpM_id,_tmpSub,_tmpSub_cs,_tmpCt_t,_tmpCt_l,_tmpExp,_tmpM_cls,_tmpM_type,_tmpV,_tmpM_size,_tmpPri,_tmpRr,_tmpRpt_a,_tmpResp_st,_tmpSt,_tmpTr_id,_tmpRetr_st,_tmpRetr_txt,_tmpRetr_txt_cs,_tmpRead_status,_tmpCt_cls,_tmpResp_txt,_tmpD_tm,_tmpD_rpt,_tmpLocked_1,_tmpSub_id_1,_tmpSeen_1,_tmpCreator_1,_tmpText_only)
          } else {
            _tmpMms = null
          }
          _item =
              Conversations(_tmpId,_tmpSms,_tmpMms,_tmpSms_data,_tmpMms_text,_tmpMms_content_uri,_tmpMms_mimetype,_tmpMms_filename,_tmpMms_filepath)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getLatestConversation(threadId: Int): Conversations? {
    val _sql: String =
        "SELECT * FROM Conversations WHERE (thread_id = ? OR Conversations.mms_thread_id = ?) ORDER BY date DESC LIMIT 1"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, threadId.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, threadId.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfSmsData: Int = getColumnIndexOrThrow(_stmt, "sms_data")
        val _columnIndexOfMmsText: Int = getColumnIndexOrThrow(_stmt, "mms_text")
        val _columnIndexOfMmsContentUri: Int = getColumnIndexOrThrow(_stmt, "mms_content_uri")
        val _columnIndexOfMmsMimetype: Int = getColumnIndexOrThrow(_stmt, "mms_mimetype")
        val _columnIndexOfMmsFilename: Int = getColumnIndexOrThrow(_stmt, "mms_filename")
        val _columnIndexOfMmsFilepath: Int = getColumnIndexOrThrow(_stmt, "mms_filepath")
        val _columnIndexOfId_1: Int = getColumnIndexOrThrow(_stmt, "_id")
        val _columnIndexOfThreadId: Int = getColumnIndexOrThrow(_stmt, "thread_id")
        val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
        val _columnIndexOfPerson: Int = getColumnIndexOrThrow(_stmt, "person")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfDateSent: Int = getColumnIndexOrThrow(_stmt, "date_sent")
        val _columnIndexOfProtocol: Int = getColumnIndexOrThrow(_stmt, "protocol")
        val _columnIndexOfRead: Int = getColumnIndexOrThrow(_stmt, "read")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfReplyPathPresent: Int = getColumnIndexOrThrow(_stmt, "reply_path_present")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfBody: Int = getColumnIndexOrThrow(_stmt, "body")
        val _columnIndexOfServiceCenter: Int = getColumnIndexOrThrow(_stmt, "service_center")
        val _columnIndexOfLocked: Int = getColumnIndexOrThrow(_stmt, "locked")
        val _columnIndexOfSubId: Int = getColumnIndexOrThrow(_stmt, "sub_id")
        val _columnIndexOfErrorCode: Int = getColumnIndexOrThrow(_stmt, "error_code")
        val _columnIndexOfCreator: Int = getColumnIndexOrThrow(_stmt, "creator")
        val _columnIndexOfSeen: Int = getColumnIndexOrThrow(_stmt, "seen")
        val _columnIndexOfId_2: Int = getColumnIndexOrThrow(_stmt, "mms__id")
        val _columnIndexOfThreadId_1: Int = getColumnIndexOrThrow(_stmt, "mms_thread_id")
        val _columnIndexOfDate_1: Int = getColumnIndexOrThrow(_stmt, "mms_date")
        val _columnIndexOfDateSent_1: Int = getColumnIndexOrThrow(_stmt, "mms_date_sent")
        val _columnIndexOfMsgBox: Int = getColumnIndexOrThrow(_stmt, "mms_msg_box")
        val _columnIndexOfRead_1: Int = getColumnIndexOrThrow(_stmt, "mms_read")
        val _columnIndexOfMId: Int = getColumnIndexOrThrow(_stmt, "mms_m_id")
        val _columnIndexOfSub: Int = getColumnIndexOrThrow(_stmt, "mms_sub")
        val _columnIndexOfSubCs: Int = getColumnIndexOrThrow(_stmt, "mms_sub_cs")
        val _columnIndexOfCtT: Int = getColumnIndexOrThrow(_stmt, "mms_ct_t")
        val _columnIndexOfCtL: Int = getColumnIndexOrThrow(_stmt, "mms_ct_l")
        val _columnIndexOfExp: Int = getColumnIndexOrThrow(_stmt, "mms_exp")
        val _columnIndexOfMCls: Int = getColumnIndexOrThrow(_stmt, "mms_m_cls")
        val _columnIndexOfMType: Int = getColumnIndexOrThrow(_stmt, "mms_m_type")
        val _columnIndexOfV: Int = getColumnIndexOrThrow(_stmt, "mms_v")
        val _columnIndexOfMSize: Int = getColumnIndexOrThrow(_stmt, "mms_m_size")
        val _columnIndexOfPri: Int = getColumnIndexOrThrow(_stmt, "mms_pri")
        val _columnIndexOfRr: Int = getColumnIndexOrThrow(_stmt, "mms_rr")
        val _columnIndexOfRptA: Int = getColumnIndexOrThrow(_stmt, "mms_rpt_a")
        val _columnIndexOfRespSt: Int = getColumnIndexOrThrow(_stmt, "mms_resp_st")
        val _columnIndexOfSt: Int = getColumnIndexOrThrow(_stmt, "mms_st")
        val _columnIndexOfTrId: Int = getColumnIndexOrThrow(_stmt, "mms_tr_id")
        val _columnIndexOfRetrSt: Int = getColumnIndexOrThrow(_stmt, "mms_retr_st")
        val _columnIndexOfRetrTxt: Int = getColumnIndexOrThrow(_stmt, "mms_retr_txt")
        val _columnIndexOfRetrTxtCs: Int = getColumnIndexOrThrow(_stmt, "mms_retr_txt_cs")
        val _columnIndexOfReadStatus: Int = getColumnIndexOrThrow(_stmt, "mms_read_status")
        val _columnIndexOfCtCls: Int = getColumnIndexOrThrow(_stmt, "mms_ct_cls")
        val _columnIndexOfRespTxt: Int = getColumnIndexOrThrow(_stmt, "mms_resp_txt")
        val _columnIndexOfDTm: Int = getColumnIndexOrThrow(_stmt, "mms_d_tm")
        val _columnIndexOfDRpt: Int = getColumnIndexOrThrow(_stmt, "mms_d_rpt")
        val _columnIndexOfLocked_1: Int = getColumnIndexOrThrow(_stmt, "mms_locked")
        val _columnIndexOfSubId_1: Int = getColumnIndexOrThrow(_stmt, "mms_sub_id")
        val _columnIndexOfSeen_1: Int = getColumnIndexOrThrow(_stmt, "mms_seen")
        val _columnIndexOfCreator_1: Int = getColumnIndexOrThrow(_stmt, "mms_creator")
        val _columnIndexOfTextOnly: Int = getColumnIndexOrThrow(_stmt, "mms_text_only")
        val _result: Conversations?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpSms_data: ByteArray?
          if (_stmt.isNull(_columnIndexOfSmsData)) {
            _tmpSms_data = null
          } else {
            _tmpSms_data = _stmt.getBlob(_columnIndexOfSmsData)
          }
          val _tmpMms_text: String?
          if (_stmt.isNull(_columnIndexOfMmsText)) {
            _tmpMms_text = null
          } else {
            _tmpMms_text = _stmt.getText(_columnIndexOfMmsText)
          }
          val _tmpMms_content_uri: String?
          if (_stmt.isNull(_columnIndexOfMmsContentUri)) {
            _tmpMms_content_uri = null
          } else {
            _tmpMms_content_uri = _stmt.getText(_columnIndexOfMmsContentUri)
          }
          val _tmpMms_mimetype: String?
          if (_stmt.isNull(_columnIndexOfMmsMimetype)) {
            _tmpMms_mimetype = null
          } else {
            _tmpMms_mimetype = _stmt.getText(_columnIndexOfMmsMimetype)
          }
          val _tmpMms_filename: String?
          if (_stmt.isNull(_columnIndexOfMmsFilename)) {
            _tmpMms_filename = null
          } else {
            _tmpMms_filename = _stmt.getText(_columnIndexOfMmsFilename)
          }
          val _tmpMms_filepath: String?
          if (_stmt.isNull(_columnIndexOfMmsFilepath)) {
            _tmpMms_filepath = null
          } else {
            _tmpMms_filepath = _stmt.getText(_columnIndexOfMmsFilepath)
          }
          val _tmpSms: SmsMmsNatives.Sms?
          if (!(_stmt.isNull(_columnIndexOfId_1) && _stmt.isNull(_columnIndexOfThreadId) &&
              _stmt.isNull(_columnIndexOfAddress) && _stmt.isNull(_columnIndexOfPerson) &&
              _stmt.isNull(_columnIndexOfDate) && _stmt.isNull(_columnIndexOfDateSent) &&
              _stmt.isNull(_columnIndexOfProtocol) && _stmt.isNull(_columnIndexOfRead) &&
              _stmt.isNull(_columnIndexOfStatus) && _stmt.isNull(_columnIndexOfType) &&
              _stmt.isNull(_columnIndexOfReplyPathPresent) && _stmt.isNull(_columnIndexOfSubject) &&
              _stmt.isNull(_columnIndexOfBody) && _stmt.isNull(_columnIndexOfServiceCenter) &&
              _stmt.isNull(_columnIndexOfLocked) && _stmt.isNull(_columnIndexOfSubId) &&
              _stmt.isNull(_columnIndexOfErrorCode) && _stmt.isNull(_columnIndexOfCreator) &&
              _stmt.isNull(_columnIndexOfSeen))) {
            val _tmp_id: Long?
            if (_stmt.isNull(_columnIndexOfId_1)) {
              _tmp_id = null
            } else {
              _tmp_id = _stmt.getLong(_columnIndexOfId_1)
            }
            val _tmpThread_id: Int
            _tmpThread_id = _stmt.getLong(_columnIndexOfThreadId).toInt()
            val _tmpAddress: String?
            if (_stmt.isNull(_columnIndexOfAddress)) {
              _tmpAddress = null
            } else {
              _tmpAddress = _stmt.getText(_columnIndexOfAddress)
            }
            val _tmpPerson: String?
            if (_stmt.isNull(_columnIndexOfPerson)) {
              _tmpPerson = null
            } else {
              _tmpPerson = _stmt.getText(_columnIndexOfPerson)
            }
            val _tmpDate: Long
            _tmpDate = _stmt.getLong(_columnIndexOfDate)
            val _tmpDate_sent: Long
            _tmpDate_sent = _stmt.getLong(_columnIndexOfDateSent)
            val _tmpProtocol: String?
            if (_stmt.isNull(_columnIndexOfProtocol)) {
              _tmpProtocol = null
            } else {
              _tmpProtocol = _stmt.getText(_columnIndexOfProtocol)
            }
            val _tmpRead: Int
            _tmpRead = _stmt.getLong(_columnIndexOfRead).toInt()
            val _tmpStatus: Int
            _tmpStatus = _stmt.getLong(_columnIndexOfStatus).toInt()
            val _tmpType: Int
            _tmpType = _stmt.getLong(_columnIndexOfType).toInt()
            val _tmpReply_path_present: String?
            if (_stmt.isNull(_columnIndexOfReplyPathPresent)) {
              _tmpReply_path_present = null
            } else {
              _tmpReply_path_present = _stmt.getText(_columnIndexOfReplyPathPresent)
            }
            val _tmpSubject: String?
            if (_stmt.isNull(_columnIndexOfSubject)) {
              _tmpSubject = null
            } else {
              _tmpSubject = _stmt.getText(_columnIndexOfSubject)
            }
            val _tmpBody: String?
            if (_stmt.isNull(_columnIndexOfBody)) {
              _tmpBody = null
            } else {
              _tmpBody = _stmt.getText(_columnIndexOfBody)
            }
            val _tmpService_center: String?
            if (_stmt.isNull(_columnIndexOfServiceCenter)) {
              _tmpService_center = null
            } else {
              _tmpService_center = _stmt.getText(_columnIndexOfServiceCenter)
            }
            val _tmpLocked: Int?
            if (_stmt.isNull(_columnIndexOfLocked)) {
              _tmpLocked = null
            } else {
              _tmpLocked = _stmt.getLong(_columnIndexOfLocked).toInt()
            }
            val _tmpSub_id: Long
            _tmpSub_id = _stmt.getLong(_columnIndexOfSubId)
            val _tmpError_code: Int?
            if (_stmt.isNull(_columnIndexOfErrorCode)) {
              _tmpError_code = null
            } else {
              _tmpError_code = _stmt.getLong(_columnIndexOfErrorCode).toInt()
            }
            val _tmpCreator: String?
            if (_stmt.isNull(_columnIndexOfCreator)) {
              _tmpCreator = null
            } else {
              _tmpCreator = _stmt.getText(_columnIndexOfCreator)
            }
            val _tmpSeen: Int?
            if (_stmt.isNull(_columnIndexOfSeen)) {
              _tmpSeen = null
            } else {
              _tmpSeen = _stmt.getLong(_columnIndexOfSeen).toInt()
            }
            _tmpSms =
                SmsMmsNatives.Sms(_tmp_id,_tmpThread_id,_tmpAddress,_tmpPerson,_tmpDate,_tmpDate_sent,_tmpProtocol,_tmpRead,_tmpStatus,_tmpType,_tmpReply_path_present,_tmpSubject,_tmpBody,_tmpService_center,_tmpLocked,_tmpSub_id,_tmpError_code,_tmpCreator,_tmpSeen)
          } else {
            _tmpSms = null
          }
          val _tmpMms: SmsMmsNatives.Mms?
          if (!(_stmt.isNull(_columnIndexOfId_2) && _stmt.isNull(_columnIndexOfThreadId_1) &&
              _stmt.isNull(_columnIndexOfDate_1) && _stmt.isNull(_columnIndexOfDateSent_1) &&
              _stmt.isNull(_columnIndexOfMsgBox) && _stmt.isNull(_columnIndexOfRead_1) &&
              _stmt.isNull(_columnIndexOfMId) && _stmt.isNull(_columnIndexOfSub) &&
              _stmt.isNull(_columnIndexOfSubCs) && _stmt.isNull(_columnIndexOfCtT) &&
              _stmt.isNull(_columnIndexOfCtL) && _stmt.isNull(_columnIndexOfExp) &&
              _stmt.isNull(_columnIndexOfMCls) && _stmt.isNull(_columnIndexOfMType) &&
              _stmt.isNull(_columnIndexOfV) && _stmt.isNull(_columnIndexOfMSize) &&
              _stmt.isNull(_columnIndexOfPri) && _stmt.isNull(_columnIndexOfRr) &&
              _stmt.isNull(_columnIndexOfRptA) && _stmt.isNull(_columnIndexOfRespSt) &&
              _stmt.isNull(_columnIndexOfSt) && _stmt.isNull(_columnIndexOfTrId) &&
              _stmt.isNull(_columnIndexOfRetrSt) && _stmt.isNull(_columnIndexOfRetrTxt) &&
              _stmt.isNull(_columnIndexOfRetrTxtCs) && _stmt.isNull(_columnIndexOfReadStatus) &&
              _stmt.isNull(_columnIndexOfCtCls) && _stmt.isNull(_columnIndexOfRespTxt) &&
              _stmt.isNull(_columnIndexOfDTm) && _stmt.isNull(_columnIndexOfDRpt) &&
              _stmt.isNull(_columnIndexOfLocked_1) && _stmt.isNull(_columnIndexOfSubId_1) &&
              _stmt.isNull(_columnIndexOfSeen_1) && _stmt.isNull(_columnIndexOfCreator_1) &&
              _stmt.isNull(_columnIndexOfTextOnly))) {
            val _tmp_id_1: Long
            _tmp_id_1 = _stmt.getLong(_columnIndexOfId_2)
            val _tmpThread_id_1: Int
            _tmpThread_id_1 = _stmt.getLong(_columnIndexOfThreadId_1).toInt()
            val _tmpDate_1: Long
            _tmpDate_1 = _stmt.getLong(_columnIndexOfDate_1)
            val _tmpDate_sent_1: Long
            _tmpDate_sent_1 = _stmt.getLong(_columnIndexOfDateSent_1)
            val _tmpMsg_box: Int
            _tmpMsg_box = _stmt.getLong(_columnIndexOfMsgBox).toInt()
            val _tmpRead_1: Int?
            if (_stmt.isNull(_columnIndexOfRead_1)) {
              _tmpRead_1 = null
            } else {
              _tmpRead_1 = _stmt.getLong(_columnIndexOfRead_1).toInt()
            }
            val _tmpM_id: String?
            if (_stmt.isNull(_columnIndexOfMId)) {
              _tmpM_id = null
            } else {
              _tmpM_id = _stmt.getText(_columnIndexOfMId)
            }
            val _tmpSub: String?
            if (_stmt.isNull(_columnIndexOfSub)) {
              _tmpSub = null
            } else {
              _tmpSub = _stmt.getText(_columnIndexOfSub)
            }
            val _tmpSub_cs: Int?
            if (_stmt.isNull(_columnIndexOfSubCs)) {
              _tmpSub_cs = null
            } else {
              _tmpSub_cs = _stmt.getLong(_columnIndexOfSubCs).toInt()
            }
            val _tmpCt_t: String?
            if (_stmt.isNull(_columnIndexOfCtT)) {
              _tmpCt_t = null
            } else {
              _tmpCt_t = _stmt.getText(_columnIndexOfCtT)
            }
            val _tmpCt_l: String?
            if (_stmt.isNull(_columnIndexOfCtL)) {
              _tmpCt_l = null
            } else {
              _tmpCt_l = _stmt.getText(_columnIndexOfCtL)
            }
            val _tmpExp: String?
            if (_stmt.isNull(_columnIndexOfExp)) {
              _tmpExp = null
            } else {
              _tmpExp = _stmt.getText(_columnIndexOfExp)
            }
            val _tmpM_cls: String?
            if (_stmt.isNull(_columnIndexOfMCls)) {
              _tmpM_cls = null
            } else {
              _tmpM_cls = _stmt.getText(_columnIndexOfMCls)
            }
            val _tmpM_type: Int?
            if (_stmt.isNull(_columnIndexOfMType)) {
              _tmpM_type = null
            } else {
              _tmpM_type = _stmt.getLong(_columnIndexOfMType).toInt()
            }
            val _tmpV: Int?
            if (_stmt.isNull(_columnIndexOfV)) {
              _tmpV = null
            } else {
              _tmpV = _stmt.getLong(_columnIndexOfV).toInt()
            }
            val _tmpM_size: Int?
            if (_stmt.isNull(_columnIndexOfMSize)) {
              _tmpM_size = null
            } else {
              _tmpM_size = _stmt.getLong(_columnIndexOfMSize).toInt()
            }
            val _tmpPri: Int?
            if (_stmt.isNull(_columnIndexOfPri)) {
              _tmpPri = null
            } else {
              _tmpPri = _stmt.getLong(_columnIndexOfPri).toInt()
            }
            val _tmpRr: Int?
            if (_stmt.isNull(_columnIndexOfRr)) {
              _tmpRr = null
            } else {
              _tmpRr = _stmt.getLong(_columnIndexOfRr).toInt()
            }
            val _tmpRpt_a: String?
            if (_stmt.isNull(_columnIndexOfRptA)) {
              _tmpRpt_a = null
            } else {
              _tmpRpt_a = _stmt.getText(_columnIndexOfRptA)
            }
            val _tmpResp_st: String?
            if (_stmt.isNull(_columnIndexOfRespSt)) {
              _tmpResp_st = null
            } else {
              _tmpResp_st = _stmt.getText(_columnIndexOfRespSt)
            }
            val _tmpSt: String?
            if (_stmt.isNull(_columnIndexOfSt)) {
              _tmpSt = null
            } else {
              _tmpSt = _stmt.getText(_columnIndexOfSt)
            }
            val _tmpTr_id: String?
            if (_stmt.isNull(_columnIndexOfTrId)) {
              _tmpTr_id = null
            } else {
              _tmpTr_id = _stmt.getText(_columnIndexOfTrId)
            }
            val _tmpRetr_st: String?
            if (_stmt.isNull(_columnIndexOfRetrSt)) {
              _tmpRetr_st = null
            } else {
              _tmpRetr_st = _stmt.getText(_columnIndexOfRetrSt)
            }
            val _tmpRetr_txt: String?
            if (_stmt.isNull(_columnIndexOfRetrTxt)) {
              _tmpRetr_txt = null
            } else {
              _tmpRetr_txt = _stmt.getText(_columnIndexOfRetrTxt)
            }
            val _tmpRetr_txt_cs: String?
            if (_stmt.isNull(_columnIndexOfRetrTxtCs)) {
              _tmpRetr_txt_cs = null
            } else {
              _tmpRetr_txt_cs = _stmt.getText(_columnIndexOfRetrTxtCs)
            }
            val _tmpRead_status: String?
            if (_stmt.isNull(_columnIndexOfReadStatus)) {
              _tmpRead_status = null
            } else {
              _tmpRead_status = _stmt.getText(_columnIndexOfReadStatus)
            }
            val _tmpCt_cls: String?
            if (_stmt.isNull(_columnIndexOfCtCls)) {
              _tmpCt_cls = null
            } else {
              _tmpCt_cls = _stmt.getText(_columnIndexOfCtCls)
            }
            val _tmpResp_txt: String?
            if (_stmt.isNull(_columnIndexOfRespTxt)) {
              _tmpResp_txt = null
            } else {
              _tmpResp_txt = _stmt.getText(_columnIndexOfRespTxt)
            }
            val _tmpD_tm: String?
            if (_stmt.isNull(_columnIndexOfDTm)) {
              _tmpD_tm = null
            } else {
              _tmpD_tm = _stmt.getText(_columnIndexOfDTm)
            }
            val _tmpD_rpt: Int?
            if (_stmt.isNull(_columnIndexOfDRpt)) {
              _tmpD_rpt = null
            } else {
              _tmpD_rpt = _stmt.getLong(_columnIndexOfDRpt).toInt()
            }
            val _tmpLocked_1: Int?
            if (_stmt.isNull(_columnIndexOfLocked_1)) {
              _tmpLocked_1 = null
            } else {
              _tmpLocked_1 = _stmt.getLong(_columnIndexOfLocked_1).toInt()
            }
            val _tmpSub_id_1: Long?
            if (_stmt.isNull(_columnIndexOfSubId_1)) {
              _tmpSub_id_1 = null
            } else {
              _tmpSub_id_1 = _stmt.getLong(_columnIndexOfSubId_1)
            }
            val _tmpSeen_1: Int?
            if (_stmt.isNull(_columnIndexOfSeen_1)) {
              _tmpSeen_1 = null
            } else {
              _tmpSeen_1 = _stmt.getLong(_columnIndexOfSeen_1).toInt()
            }
            val _tmpCreator_1: String?
            if (_stmt.isNull(_columnIndexOfCreator_1)) {
              _tmpCreator_1 = null
            } else {
              _tmpCreator_1 = _stmt.getText(_columnIndexOfCreator_1)
            }
            val _tmpText_only: Int?
            if (_stmt.isNull(_columnIndexOfTextOnly)) {
              _tmpText_only = null
            } else {
              _tmpText_only = _stmt.getLong(_columnIndexOfTextOnly).toInt()
            }
            _tmpMms =
                SmsMmsNatives.Mms(_tmp_id_1,_tmpThread_id_1,_tmpDate_1,_tmpDate_sent_1,_tmpMsg_box,_tmpRead_1,_tmpM_id,_tmpSub,_tmpSub_cs,_tmpCt_t,_tmpCt_l,_tmpExp,_tmpM_cls,_tmpM_type,_tmpV,_tmpM_size,_tmpPri,_tmpRr,_tmpRpt_a,_tmpResp_st,_tmpSt,_tmpTr_id,_tmpRetr_st,_tmpRetr_txt,_tmpRetr_txt_cs,_tmpRead_status,_tmpCt_cls,_tmpResp_txt,_tmpD_tm,_tmpD_rpt,_tmpLocked_1,_tmpSub_id_1,_tmpSeen_1,_tmpCreator_1,_tmpText_only)
          } else {
            _tmpMms = null
          }
          _result =
              Conversations(_tmpId,_tmpSms,_tmpMms,_tmpSms_data,_tmpMms_text,_tmpMms_content_uri,_tmpMms_mimetype,_tmpMms_filename,_tmpMms_filepath)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun deleteAllConversations() {
    val _sql: String = "DELETE FROM Conversations"
    return performBlocking(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun deleteAllThreads() {
    val _sql: String = "DELETE FROM Threads"
    return performBlocking(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun deleteThreadConversation(conversationId: Long) {
    val _sql: String = "DELETE FROM Threads WHERE conversationId = ?"
    return performBlocking(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, conversationId)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun deleteThreadConversations(ids: List<Long>) {
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("DELETE FROM Threads WHERE conversationId IN (")
    val _inputSize: Int = ids.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    return performBlocking(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        for (_item: Long in ids) {
          _stmt.bindLong(_argIndex, _item)
          _argIndex++
        }
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
