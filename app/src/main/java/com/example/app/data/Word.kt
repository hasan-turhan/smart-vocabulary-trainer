package com.example.app.data

data class Word(
    val article: String,
    val german: String,
    val plural: String,
    val english: String,
    val example: String
)
 {
    val id: String
        get() = "$article-$german"
}
