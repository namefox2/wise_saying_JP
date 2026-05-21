package com.kotoba.takarabako.data.model

data class QuoteSegment(
    val kanji: String,
    val reading: String
)

data class Quote(
    val id: String,
    val cat: String,
    val kanji: String,
    val segments: List<QuoteSegment>,
    val korean: String,
    val author: String
)
