package com.afkanerd.smswithoutborders_libsmsmms.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

class MmsReceivedReceiverImplTest: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val uri = intent!!.getStringExtra("uri")
        MmsReceivedReceiverImpl().processIncomingMms(
            context!!,
            uri!!.toUri(),
        )
    }
}