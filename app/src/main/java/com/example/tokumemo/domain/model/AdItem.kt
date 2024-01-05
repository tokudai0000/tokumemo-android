package com.example.tokumemo.domain.model

import java.io.Serializable


data class AdItem(
    val id: Int,
    val clientName: String,
    val imageUrlStr: String,
    val targetUrlStr: String,
    val imageDescription: String,
) : Serializable