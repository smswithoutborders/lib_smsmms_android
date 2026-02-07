package com.afkanerd.smswithoutborders_libsmsmms.extensions.context

import android.content.Context
import android.util.Base64
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.KeyStore
import java.security.SecureRandom



private object Security {
    const val FILENAME: String = "com.afkanerd.deku.security"
    const val DB_PASSWORD = "DB_PASSWORD"
}

fun Context.isAvailableInKeystore(keystoreAlias: String) : Boolean {
    val ks = KeyStore.getInstance("AndroidKeyStore")
    ks.load(null)
    return ks.containsAlias(keystoreAlias)
}

val Context.settingsGetDbPassword get(): ByteArray? {
    val masterKey: MasterKey = MasterKey.Builder(this)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    return EncryptedSharedPreferences.create(
        this,
        Security.FILENAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    ).getString(Security.DB_PASSWORD, "").run {
        if(!this.isNullOrBlank()) Base64.decode(this, Base64.DEFAULT) else null
    }
}

fun Context.settingsSetDbPassword(password: ByteArray) {
    val masterKey: MasterKey = MasterKey.Builder(this)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    EncryptedSharedPreferences.create(
        this,
        Security.FILENAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    ).edit {
        putString(Security.DB_PASSWORD,
            Base64.encodeToString(password, Base64.DEFAULT))
        apply()
    }
}

fun Context.generateSecureRandom() : ByteArray{
    val secureRandom = SecureRandom()
    val secretBytes = ByteArray(32) // Example: 256 bits
    secureRandom.nextBytes(secretBytes)
    return secretBytes
}
