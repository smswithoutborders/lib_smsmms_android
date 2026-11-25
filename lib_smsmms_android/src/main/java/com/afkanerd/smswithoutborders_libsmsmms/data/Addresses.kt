package com.afkanerd.smswithoutborders_libsmsmms.data

import android.R.attr.phoneNumber
import android.content.Context
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat.getSystemService
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getDefaultRegion
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isShortCode
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil

class Addresses {

    val dialingCode: Long
    val subscriberNumber: Long
    val isShortCode: Boolean

    private constructor(
        dialingCode: Long,
        subscriberNumber: Long,
        isShortCode: Boolean
    ) {
        this.dialingCode = dialingCode
        this.subscriberNumber = subscriberNumber
        this.isShortCode = isShortCode
    }

    val getAddress by lazy { dialingCode.toString() + subscriberNumber.toString() }

    companion object {
        private fun getDefaultRegion(context: Context) : Int {
            var countryCode: String? = null

            // Check if network information is available
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            if (cm != null) {
                // Get the TelephonyManager to access network-related information
                val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?
                if (tm != null) {
                    // Get the ISO country code from the network
                    countryCode = tm.networkCountryIso.uppercase()
                }
            }
            return PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryCode)
        }

        fun build(
            context: Context,
            address: String
        ): Addresses {
            val phoneNumberUtil = PhoneNumberUtil.getInstance()
            val defaultRegion = getDefaultRegion(context)

            var address = address
            if(address.startsWith("0"))
                address = address.substring(1)

            address = address.replace("%2B".toRegex(), "+")
                .replace("-".toRegex(), "")
                .replace("%20".toRegex(), "")
                .replace(" ".toRegex(), "")
                .replace("sms[to]*:".toRegex(), "")
                .replaceFirst("^0+".toRegex(), "")


            if (address.length < 5) return Addresses(
                dialingCode = defaultRegion.toLong(),
                subscriberNumber = address.toLong(),
                isShortCode = true
            )

            try {
                val phoneNumber = phoneNumberUtil.parse(address, defaultRegion.toString())
                return Addresses(
                    dialingCode = phoneNumber.countryCode.toLong(),
                    subscriberNumber = phoneNumber.nationalNumber,
                    isShortCode = false
                )
            } catch (e: NumberParseException) {
                if (e.errorType == NumberParseException.ErrorType.INVALID_COUNTRY_CODE) {
                    return Addresses(
                        dialingCode = defaultRegion.toLong(),
                        subscriberNumber = address.toLong(),
                        isShortCode = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return Addresses(
                dialingCode = defaultRegion.toLong(),
                subscriberNumber = address.toLong(),
                isShortCode = false
            )
        }
    }
}