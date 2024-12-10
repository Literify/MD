package com.literify.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.literify.BuildConfig
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec


object KeystoreUtils {
    private const val KEY_ALIAS = BuildConfig.KEYSTORE_KEY_ALIAS

    init {
        generateKey()
    }

    private fun generateKey() {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        if (keyStore.containsAlias(KEY_ALIAS)) return

        val keyGenParameterSpec = KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .build()

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    fun encrypt(data: String): String {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val secretKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val encryptedData = cipher.doFinal(data.toByteArray())

        val iv = cipher.iv
        val ivAndEncryptedData = iv + encryptedData

        return Base64.encodeToString(ivAndEncryptedData, Base64.NO_WRAP)
    }

    fun decrypt(encryptedData: String): String {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val secretKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        val ivAndEncryptedData = Base64.decode(encryptedData, Base64.NO_WRAP)
        val iv = ivAndEncryptedData.copyOfRange(0, 12)
        val encryptedBytes = ivAndEncryptedData.copyOfRange(12, ivAndEncryptedData.size)

        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(128, iv))

        val decryptedData = cipher.doFinal(encryptedBytes)
        return String(decryptedData)
    }
}