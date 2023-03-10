package com.example.tokumemo

inline fun <T> guard(value: T?, ifNull: () -> Unit): T {
    if (value != null) return value
    ifNull()
    throw Exception("Guarded from null!")
}