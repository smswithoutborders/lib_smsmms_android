package com.afkanerd.smswithoutborders_libsmsmms.data

import android.content.Context
import android.security.KeyStoreException
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.datastore.core.IOException
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.generateSecureRandom
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.isAvailableInKeystore
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.settingsGetDbPassword
import com.afkanerd.smswithoutborders_libsmsmms.extensions.context.settingsSetDbPassword
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.UnrecoverableEntryException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.security.cert.CertificateException

object Cryptography {
    @Throws(
        KeyStoreException::class,
        NoSuchAlgorithmException::class,
        NoSuchProviderException::class,
        InvalidAlgorithmParameterException::class
    )
    fun createAndStoreSecretKey(keystoreAlias: String) {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )

        val params = KeyGenParameterSpec.Builder(
            keystoreAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setKeySize(256)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(true)
            .build()

        keyGenerator.init(params)
        keyGenerator.generateKey()
    }

    @Throws(
        KeyStoreException::class,
        UnrecoverableEntryException::class,
        NoSuchAlgorithmException::class,
        CertificateException::class,
        IOException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    private fun encryptWithKeyStore(context: Context, data: ByteArray, keystoreAlias: String): ByteArray {
        if(!context.isAvailableInKeystore(keystoreAlias))
            createAndStoreSecretKey(keystoreAlias)

        // Initialize KeyStore
        val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        // Retrieve the key with alias androidKeyStoreAlias created before
        val keyEntry: KeyStore.SecretKeyEntry =
            keyStore.getEntry(keystoreAlias, null) as KeyStore.SecretKeyEntry
        val key: SecretKey = keyEntry.secretKey
        // Use the secret key at your convenience
        val cipher: Cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher.iv + cipher.doFinal(data)
    }


    @Throws(
        KeyStoreException::class,
        UnrecoverableEntryException::class,
        NoSuchAlgorithmException::class,
        CertificateException::class,
        IOException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    private fun decryptWithKeyStore(data: ByteArray, keystoreAlias: String): ByteArray? {
        val ivSize = 12 // GCM standard
        val iv = data.copyOfRange(0, ivSize)
        val data = data.copyOfRange(ivSize, data.size)

        // Initialize KeyStore
        val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        // Retrieve the key with alias androidKeyStoreAlias created before
        val keyEntry: KeyStore.SecretKeyEntry =
            keyStore.getEntry(keystoreAlias, null) as KeyStore.SecretKeyEntry
        val key: SecretKey = keyEntry.secretKey
        // Use the secret key at your convenience
        val cipher: Cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, iv) // 128-bit auth tag
        cipher.init(Cipher.DECRYPT_MODE, key, spec)
        return cipher.doFinal(data)
    }

    fun getDatabasePassword(context: Context, keystoreAlias: String) : ByteArray {
        val password = context.settingsGetDbPassword
        if(password == null) {
            val password = context.generateSecureRandom()
            val encryptedPassword = encryptWithKeyStore(
                context,
                password,
                keystoreAlias
            )
            context.settingsSetDbPassword(encryptedPassword)
            return password
        } else {
            return decryptWithKeyStore(password, keystoreAlias) ?:
            throw Exception("Failed to decrypt database keystore")
        }
    }
}