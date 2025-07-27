package com.tokudai0000.tokumemo.domain.model

import java.io.Serializable

data class HomeEventInfoPopupItems(
    val id: Int,
    val clientName: String,
    val titleName: String,
    val description: String,
) : Serializable
// Serializable は putExtra でデータ送信する際のバイト列変換のライブラリ

data class HomeEventInfoButtonItems(
    val id: Int,
    val clientName: String,
    val titleName: String,
    val targetUrlStr: String,
) : Serializable
// Serializable は putExtra でデータ送信する際のバイト列変換のライブラリ
