import android.content.Context
import android.security.KeyPairGeneratorSpec
import android.util.Base64
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.security.auth.x500.X500Principal


val PROVIDER = "AndroidKeyStore"
val ALGORITHM = "RSA"
val CIPHER_TRANSFORMATION = "RSA/ECB/PKCS1Padding"

/**
 * テキストを暗号化する
 * @param context
 * @param alias キーペアを識別するためのエリアス。用途ごとに一意にする。
 * @param plainText 暗号化したいテキスト
 * @return 暗号化されBase64でラップされた文字列
 */
fun encrypt(context: Context, alias: String, plainText: String): String {
    val keyStore = KeyStore.getInstance(PROVIDER)
    keyStore.load(null)

    // キーペアがない場合生成
    if (!keyStore.containsAlias(alias)) {
        val keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER)
        keyPairGenerator.initialize(createKeyPairGeneratorSpec(context, alias))
        keyPairGenerator.generateKeyPair()
    }
    val publicKey = keyStore.getCertificate(alias).getPublicKey()
    val privateKey = keyStore.getKey(alias, null)

    // 公開鍵で暗号化
    val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)
    val bytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

    // SharedPreferencesに保存しやすいようにBase64でString化
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}

/**
 * 暗号化されたテキストを復号化する
 * @param alias キーペアを識別するためのエリアス。用途ごとに一意にする。
 * @param encryptedText encryptで暗号化されたテキスト
 * @return 復号化された文字列
 */
fun decrypt(alias: String, encryptedText: String): String? {
    val keyStore = KeyStore.getInstance(PROVIDER)
    keyStore.load(null)
    if (!keyStore.containsAlias(alias)) {
        return null
    }

    // 秘密鍵で復号化
    val privateKey = keyStore.getKey(alias, null)
    val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
    cipher.init(Cipher.DECRYPT_MODE, privateKey)
    val bytes = Base64.decode(encryptedText, Base64.DEFAULT)

    val b = cipher.doFinal(bytes)
    return String(b)
}

/**
 * キーペアを生成する
 */
fun createKeyPairGeneratorSpec(context: Context, alias: String): KeyPairGeneratorSpec {
    val start = Calendar.getInstance()
    val end = Calendar.getInstance()
    end.add(Calendar.YEAR, 100)

    return KeyPairGeneratorSpec.Builder(context)
        .setAlias(alias)
        .setSubject(X500Principal(String.format("CN=%s", alias)))
        .setSerialNumber(BigInteger.valueOf(1000000))
        .setStartDate(start.getTime())
        .setEndDate(end.getTime())
        .build()
}
