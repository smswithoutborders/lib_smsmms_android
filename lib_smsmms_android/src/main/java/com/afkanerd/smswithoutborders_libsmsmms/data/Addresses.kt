package com.afkanerd.smswithoutborders_libsmsmms.data

import android.R.attr.phoneNumber
import android.content.Context
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat.getSystemService
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.getDefaultRegion
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isShortCode
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import kotlinx.serialization.Serializable

@Serializable
class Addresses {
    internal val dialingCode: Long
    internal val subscriberNumber: String
    internal val isTextable: Boolean

    internal val isLocal: Boolean

    private constructor(
        dialingCode: Long,
        subscriberNumber: String,
        isTextable: Boolean,
        isLocal: Boolean,
    ) {
        this.dialingCode = dialingCode
        this.subscriberNumber = subscriberNumber
        this.isTextable = isTextable
        this.isLocal = isLocal
    }

    val getAddress: String by lazy {
        val phoneUtil = PhoneNumberUtil.getInstance()
        var value = subscriberNumber
        try {
            val parsed = phoneUtil.parse(
                subscriberNumber,
                null
            )
            value = phoneUtil.format(parsed, PhoneNumberUtil.PhoneNumberFormat.E164)
        } catch (e: Exception){
        }
        value
    }

    val getFullE164Address: String by lazy {
        val address = getAddress
        if (dialingCode == -1L)
            address
        else "+$dialingCode$address"
    }

    companion object {
        fun isPotentiallyValidNumber(phoneNumber: String, defaultRegion: String = "CM"): Boolean {
            val phoneUtil = PhoneNumberUtil.getInstance()
            return try {
                val parsedNumber = phoneUtil.parse(phoneNumber, defaultRegion)
                // Checks if the number matches valid mobile or fixed-line patterns
                val type = phoneUtil.getNumberType(parsedNumber)
                phoneUtil.isValidNumber(parsedNumber) &&
                        (type == PhoneNumberUtil.PhoneNumberType.MOBILE ||
                                type == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE)
            } catch (e: NumberParseException) {
                false
            }
        }

        fun getDefaultRegion(context: Context) : Int {
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
            var address = address
            try {
                val phoneNumberUtil = PhoneNumberUtil.getInstance()
                val phoneNumber = phoneNumberUtil.parse(address, null)
                return Addresses(
                    dialingCode = phoneNumber.countryCode.toLong(),
                    subscriberNumber = phoneNumber.nationalNumber.toString(),
                    isTextable = isPotentiallyValidNumber(address),
                    isLocal = getDefaultRegion(context) == phoneNumber.countryCode
                )
            } catch (e: NumberParseException) {
                val defaultRegion = getDefaultRegion(context)
                if (e.errorType == NumberParseException.ErrorType.INVALID_COUNTRY_CODE) {
                    address = address.replace("%2B".toRegex(), "+")
                        .replace("-".toRegex(), "")
                        .replace("%20".toRegex(), "")
                        .replace(" ".toRegex(), "")
                        .replace("sms[to]*:".toRegex(), "")
                        .replaceFirst("^0+".toRegex(), "")
                    address = "+$defaultRegion$address"
                    return build(context, address)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return Addresses(
                dialingCode = -1,
                subscriberNumber = address,
                isTextable = isPotentiallyValidNumber(address),
                isLocal = false
            )
        }
    }
}