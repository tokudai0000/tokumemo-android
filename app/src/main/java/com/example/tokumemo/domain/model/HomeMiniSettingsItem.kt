package com.example.tokumemo.domain.model

data class HomeMiniSettingsItem(
    val title: String,
    val id: Type,
    val targetUrl: String?
) {
    enum class Type {
        Review,
        PrApplication,
        ContactUs,
        HomePage,
        TermsOfService,
        PrivacyPolicy,
        OfficialSNS,
        SourceCode,
    }
}
