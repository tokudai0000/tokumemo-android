import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.Key
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.KeyStoreException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.security.auth.x500.X500Principal

/*
参考
https://qiita.com/Sab_swiftlin/items/f92b4118b5abd1203154
 */

object KeyStoreUtils {
    // 共通定義
    private const val PROVIDER = "AndroidKeyStore"
    private const val KEY_STORE_ALIAS = "this_apps_alias"

    // API22以下で利用
    private const val ALGORITHM = "RSA"
    private const val CIPHER_TRANSFORMATION_RSA = "RSA/ECB/PKCS1Padding"

    // API23以上で利用
    @TargetApi(23)
    private const val CIPHER_TRANSFORMATION_AES = "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_CBC}/${KeyProperties.ENCRYPTION_PADDING_PKCS7}"

    // APIレベルで分岐して暗号化
    fun encrypt(context: Context, plainText: String): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            encryptAES(plainText)
        } else {
            encryptRSA(context, plainText)
        }
    }

    // APIレベルで分岐して復号
    fun decrypt(encryptedText: String): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            decryptAES(encryptedText)
        } else {
            decryptRSA(encryptedText)
        }
    }

    // API22以下用暗号化メソッド
    private fun encryptRSA(context: Context, plainText: String): String? {

        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 100)

        val keyStore = KeyStore.getInstance(PROVIDER).apply {
            load(null)
        }.also {
            if (!it.containsAlias(KEY_STORE_ALIAS)) {
                KeyPairGenerator.getInstance(ALGORITHM, PROVIDER).apply {
                    initialize(
                        KeyPairGeneratorSpec.Builder(context)
                            .setAlias(KEY_STORE_ALIAS)
                            .setSubject(X500Principal("CN=$KEY_STORE_ALIAS"))
                            .setSerialNumber(BigInteger.ONE)
                            .setStartDate(start.time)
                            .setEndDate(end.time)
                            .build()
                    )
                }.run {
                    generateKeyPair()
                }
            }
        }
        val key = keyStore.getCertificate(KEY_STORE_ALIAS).publicKey

        return Cipher.getInstance(CIPHER_TRANSFORMATION_RSA).apply {
            init(Cipher.ENCRYPT_MODE, key)
        }.run {
            this.doFinal(plainText.toByteArray(Charset.defaultCharset()))
        }.let {
            Base64.encodeToString(it, Base64.DEFAULT)
        }
    }

    // API22以下用復号メソッド
    private fun decryptRSA(encryptedText: String): String? {

        val keyStore = KeyStore.getInstance(PROVIDER).apply {
            load(null)
        }.also {
            if (!it.containsAlias(KEY_STORE_ALIAS)) return null
        }

        val privateKey = keyStore.getKey(KEY_STORE_ALIAS, null)
        return Cipher.getInstance(CIPHER_TRANSFORMATION_RSA).apply {
            init(Cipher.DECRYPT_MODE, privateKey)
        }.run {
            this.doFinal(Base64.decode(encryptedText, Base64.DEFAULT))
        }?.let {
            String(it)
        }
    }

    // API23以上用暗号化メソッド
    @TargetApi(23)
    private fun encryptAES(plainText: String): String? {
        val keyStore = KeyStore.getInstance(PROVIDER).apply {
            load(null)
        }

        val key = getKey(keyStore)
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION_AES).apply {
            init(Cipher.ENCRYPT_MODE, key)
        }
        val ivStr = Base64.encodeToString(cipher.iv, Base64.DEFAULT)
        val textBytes = cipher.doFinal(plainText.toByteArray(Charset.defaultCharset()))
        return ivStr + Base64.encodeToString(textBytes, Base64.DEFAULT)
    }

    // API23以上用復号メソッド
    @TargetApi(23)
    private fun decryptAES(encryptedText: String): String? {
        val keyStore = KeyStore.getInstance(PROVIDER).apply {
            load(null)
        }.also {
            if (!it.containsAlias(KEY_STORE_ALIAS)) return null
        }

        val splitText = encryptedText.split("\n").also {
            if (it.size < 2) return null
        }
        val ivStr = splitText[0]
        val encryptedBytes = Base64.decode(splitText[1], Base64.DEFAULT)
        val key = keyStore.getKey(KEY_STORE_ALIAS, null)
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION_AES).apply {
            init(Cipher.DECRYPT_MODE, key, IvParameterSpec(Base64.decode(ivStr, Base64.DEFAULT)))
        }
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }

    // API23以上用共通鍵取得メソッド
    @TargetApi(23)
    @Throws(KeyStoreException::class)
    private fun getKey(keyStore: KeyStore): Key {
        // 既に鍵を所有していたらRSAか否かで処理を分岐
        if (keyStore.containsAlias(KEY_STORE_ALIAS)) {
            keyStore.getKey(KEY_STORE_ALIAS, null).let {
                if (it.algorithm == ALGORITHM) {
                    // RSAだったら削除して例外をスロー
                    keyStore.deleteEntry(KEY_STORE_ALIAS)
                    throw KeyStoreException("Mismatch key")
                } else {
                    // AESだったら鍵を返す
                    return it
                }
            }
        }

        // 鍵が無ければ作って返す
        KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, PROVIDER).apply {
            init(
                KeyGenParameterSpec.Builder(KEY_STORE_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setCertificateSubject(X500Principal("CN=$KEY_STORE_ALIAS"))
                    .setCertificateSerialNumber(BigInteger.ONE)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build()
            )
        }.run {
            generateKey()
        }

        return keyStore.getKey(KEY_STORE_ALIAS, null)
    }
}