package com.tokudai0000.tokumemo.domain.model

data class SettingsItem(
    val title: String,
    val id: Type,
    val targetUrl: String?
) {
    enum class Type {
        Password,
        AboutThisApp,
        ContactUs,
        OfficialSNS,
        HomePage,
        TermsOfService,
        PrivacyPolicy,
        SourceCode,
        Review,
        Acknowledgements,
    }
}
