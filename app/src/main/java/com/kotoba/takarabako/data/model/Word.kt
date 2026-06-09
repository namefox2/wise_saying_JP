package com.kotoba.takarabako.data.model

data class Word(
    val id: String,
    val level: String,
    val kanji: String,
    val reading: String,
    val pos: String,
    val meaning: String,
    val exKanji: String,
    val exSegments: List<QuoteSegment>,
    val exKorean: String
)
