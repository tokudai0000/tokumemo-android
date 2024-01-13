import KeyStoreUtils.decrypt
import KeyStoreUtils.encrypt
import android.content.Context
import com.example.tokumemo.domain.model.UnivAuth

interface UnivAuthRepositoryInterface {
    fun fetchUnivAuth(): UnivAuth
    fun setUnivAuth(items: UnivAuth)
}

class UnivAuthRepository(private val context: Context) : UnivAuthRepositoryInterface {

    private val KEY_C_ACCOUNT = "KEY_cAccount"
    private var accountCID: String
        get() = getKeyStore(KEY_C_ACCOUNT)
        set(value) = setKeyStore(KEY_C_ACCOUNT, value)

    private val KEY_PASSWORD = "KEY_password"
    private var password: String
        get() = getKeyStore(KEY_PASSWORD)
        set(value) = setKeyStore(KEY_PASSWORD, value)

    override fun fetchUnivAuth(): UnivAuth {
        val accountCID = accountCID
        val password = password
        return UnivAuth(accountCID, password)
    }

    override fun setUnivAuth(items: UnivAuth) {
        accountCID = items.accountCID
        password = items.password
    }

    private fun getKeyStore(key: String): String {
        // shaaredPreferencesから読み込み
        context.getSharedPreferences(key, Context.MODE_PRIVATE).getString(key, null)?.let { encrypted ->
            try {
                decrypt(encrypted)?.let { str ->
                    return str
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }
        return ""
    }

    private fun setKeyStore(key: String, value: String) {
        try {
            encrypt(context, value)?.let { str ->
                var editor = context.getSharedPreferences(key, Context.MODE_PRIVATE).edit().apply {
                    putString(key, str).commit()
                }
            }
        } catch (e: Exception) {
            println(e.localizedMessage)
        }
    }
}
