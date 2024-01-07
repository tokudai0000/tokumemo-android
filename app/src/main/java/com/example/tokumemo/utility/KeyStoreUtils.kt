package com.example.tokumemo.utility

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyPairGenerator
import java.security.KeyStore
import javax.crypto.Cipher

val KEY_PROVIDER = "AndroidKeyStore"
val CIPHER_TRANSFORMATION = "RSA/ECB/PKCS1Padding"

/*
参考記事
https://developer.android.com/privacy-and-security/keystore?hl=ja
https://qiita.com/masaki_shoji/items/2ada7a182677a98d1cf9
https://qiita.com/f_nishio/items/485490dea126dbbb5001
https://qiita.com/sekitaka_1214/items/1942621118bba78ddf5b
 */

/**
 * テキストを暗号化する関数
 * @param alias キーペアを識別するためのエイリアス
 * @param plainText 暗号化したいテキスト
 * @return Base64でエンコードされた暗号化テキスト
 */
public fun encrypt(alias: String, plainText: String): String {
    /*
    Keystore.getInstanceの引数に"AndroidKeyStore"を指定し、Android KeyStoreのインスタンスを取得する。
    このインスタンスはフィールドにAndroidKeyStoreSpiのインスタンスを保持する。
     */
    val keyStore = KeyStore.getInstance(KEY_PROVIDER)
    //Android KeyStoreをロードする。内部のAndroidKeystoreSpiがこのメソッドにより初期化されるため、この処理が必要
    keyStore.load(null)

    // 鍵ペア(公開鍵&秘密鍵)がない場合生成
    if (!keyStore.containsAlias(alias)) {
        generateKeyPair(alias)
    }
    // 公開鍵を取得
    val publicKey = keyStore.getCertificate(alias).publicKey
    val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)

    // 平文を暗号化
    val encryptedBytes = cipher.doFinal(plainText.toByteArray())
    // 暗号化されたデータをBase64でエンコード
    return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
}

/**
 * 暗号化されたテキストを復号化する関数
 * @param alias キーペアを識別するためのエイリアス
 * @param encryptedText 暗号化されたテキスト
 * @return 復号化されたテキスト
 */
public fun decrypt(alias: String, encryptedText: String): String? {
    val keyStore = KeyStore.getInstance(KEY_PROVIDER)
    keyStore.load(null)

    // 鍵ペア(公開鍵&秘密鍵)がない場合は復号できないので、nullを返す
    if (!keyStore.containsAlias(alias)) {
        return null
    }

    // 秘密鍵を取得
    val privateKey = keyStore.getKey(alias, null)

    // 復号化処理
    val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
    cipher.init(Cipher.DECRYPT_MODE, privateKey)
    val encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT)

    // 復号化したデータ
    val decryptedBytes = cipher.doFinal(encryptedBytes)
    return String(decryptedBytes)
}

private fun generateKeyPair(alias: String) {
    /*
    鍵ペアを作成するためのKeyPairGeneratorのインスタンスを取得する
    引数のproviderに"AndroidKeyStore"を指定することで、
    Android Keystoreに鍵ペアを作成するKeyPairGeneratorSpiインスタンスを取得する
     */
    val keyPairGenerator = KeyPairGenerator.getInstance(
        KeyProperties.KEY_ALGORITHM_RSA,
        KEY_PROVIDER
    )

    // 作成する鍵ペアのスペックを指定するためのKeyGenParameterSpecインスタンスを生成
    val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
        alias, //エイリアスを"hoge"に設定。Android KeyStoreからエントリーを取得する際はこのエイリアスを使用する。
        KeyProperties.PURPOSE_ENCRYPT//鍵の使用目的を指定する。ここでは暗号化のみを目的とした鍵を生成。指定した目的以外で鍵を使用するとInvalidKeyExceptionが発生する
    ).run {
        //使用するダイジェストのアルゴリズムをSHA-256に限定する。これ以外のダイジェストアルゴリズムの使用は拒否される。
        setDigests(KeyProperties.DIGEST_SHA256)
        build()
    }

    keyPairGenerator.initialize(parameterSpec)
    keyPairGenerator.generateKeyPair()
}
