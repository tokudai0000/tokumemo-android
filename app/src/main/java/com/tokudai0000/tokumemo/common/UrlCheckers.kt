package com.tokudai0000.tokumemo.common

object UrlCheckers {

    enum class UrlType {
        UniversityLogin,      // 大学のログインページ
        OutlookLoginForm,     // Outlookのログインフォーム
        TokudaiCareerCenter   // 徳大キャリアセンター
    }

    /// 指定されたURLにJavaScriptを挿入できるかどうかを判断します。
    /// - Parameters:
    ///   - urlString: 確認するURLの文字列
    ///   - canExecuteJavascript: JavaScriptの実行が許可されているかどうかのフラグ
    ///   - urlType: 確認するURLのタイプ
    /// - Returns: JavaScriptを挿入できる場合は`true`、そうでない場合は`false`
    fun shouldInjectJavaScript(urlString: String, canExecuteJavascript: Boolean, urlType: UrlType): Boolean {
        if (!canExecuteJavascript) return false
        return when (urlType) {
            UrlType.UniversityLogin -> urlString.contains(Url.UniversityLogin.urlString)
            UrlType.OutlookLoginForm -> urlString.contains(Url.OutlookLoginForm.urlString)
            UrlType.TokudaiCareerCenter -> urlString.contains(Url.TokudaiCareerCenter.urlString)
        }
    }

    /// URLが大学サイトのアンケート催促画面かを確認します。
    /// - Parameter urlString: 確認するURLの文字列
    /// - Returns: アンケート催促のURLである場合は`true`、そうでない場合は`false`
    fun isSkipReminderURL(urlString: String): Boolean {
        return urlString.contains(Url.SkipReminder.urlString)
    }

    /// URLが大学サイトのタイムアウト画面かを確認します。(2通り存在)
    fun isUniversityServiceTimeoutURL(urlStr: String): Boolean {
        return urlStr == Url.UniversityServiceTimeOut.urlString || urlStr == Url.UniversityServiceTimeOut2.urlString
    }

    /// URLが大学サイトのログインに失敗し、再入力を求められている画面かを確認します。
    /// 初回ログイン https://localidp.ait230.tokushima-u.ac.jp/idp/profile/SAML2/Redirect/SSO?execution=e1s1
    /// 1回ログイン失敗 https://localidp.ait230.tokushima-u.ac.jp/idp/profile/SAML2/Redirect/SSO?execution=e1s2
    /// 2回ログイン失敗 https://localidp.ait230.tokushima-u.ac.jp/idp/profile/SAML2/Redirect/SSO?execution=e1s3
    /// - Parameter urlStr: 確認するURLの文字列
    /// - Returns: ログインに失敗した際のURLである場合は`true`、そうでない場合は`false`
    fun isFailureUniversityServiceLoggedInURL(urlStr: String): Boolean {
        if (urlStr.contains("https://localidp.ait230.tokushima-u.ac.jp/idp/profile/SAML2/Redirect/SSO?execution=")) {
            val start = urlStr.length - 2
            // 下2桁を比較
            if (urlStr.substring(start) != "s1") {
                return true
            }
        }
        return false
    }

    /// URLが大学サイトのログイン直後かを確認します。(3通り存在)
    /// 1, アンケート最速画面
    /// 2, 教務事務システムのモバイル画面(iPhone)
    /// 3, 教務事務システムのPC画面(iPad)
    /// - Parameter urlStr: 確認するURLの文字列
    /// - Returns: ログイン直後のURLである場合は`true`、そうでない場合は`false`
    fun isImmediatelyAfterLoginURL(urlStr: String): Boolean {
        val targetURLs = listOf(Url.SkipReminder.urlString,
            Url.CourseManagementMobile.urlString,
            Url.CourseManagementPC.urlString)
        return targetURLs.contains(urlStr)
    }

    // .pdfのurlであれば、Google Docs Viewerを使用したurlに変更して返す
    // .pdfのリンクでなければ、そのままの状態を返す
    fun convertToGoogleDocsViewerUrlIfNeeded(originalUrl: String): String? {
        // .pdfの拡張子を確認
        if (originalUrl.endsWith(".pdf", ignoreCase = true)) {
            // Google Docs ViewerのURLに変換して返す
            return "https://docs.google.com/viewer?url=${originalUrl}&embedded=true"
        }
        return null

    }
}
