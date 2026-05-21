package com.kotoba.takarabako

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kotoba.takarabako.data.model.Quote
import com.kotoba.takarabako.data.model.Word
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DataParseTest {

    private val gson = Gson()

    @Test
    fun `quote json sample parses correctly`() {
        val json = """
        [{"id":"q001","cat":"노력","kanji":"千里の道も一歩から。",
          "segments":[{"kanji":"千里","reading":"せんり"},{"kanji":"の","reading":""},
                      {"kanji":"道","reading":"みち"},{"kanji":"も","reading":""},
                      {"kanji":"一歩","reading":"いっぽ"},{"kanji":"から。","reading":""}],
          "korean":"천 리 길도 한 걸음부터.","author":"老子"}]
        """.trimIndent()

        val type = object : TypeToken<List<Quote>>() {}.type
        val quotes: List<Quote> = gson.fromJson(json, type)
        assertTrue(quotes.isNotEmpty())
        assertTrue(quotes[0].segments.isNotEmpty())
        assertFalse(quotes[0].korean.isEmpty())
    }

    @Test
    fun `word json sample parses correctly`() {
        val json = """
        [{"id":"n5_001","level":"N5","kanji":"食べる","reading":"たべる","pos":"동사",
          "meaning":"먹다","exKanji":"毎日ご飯を食べる。",
          "exSegments":[{"kanji":"毎日","reading":"まいにち"},{"kanji":"ご飯","reading":"ごはん"},
                        {"kanji":"を","reading":""},{"kanji":"食べる","reading":"たべる"},
                        {"kanji":"。","reading":""}],
          "exKorean":"매일 밥을 먹는다."}]
        """.trimIndent()

        val type = object : TypeToken<List<Word>>() {}.type
        val words: List<Word> = gson.fromJson(json, type)
        assertTrue(words.isNotEmpty())
        assertTrue(words[0].exSegments.isNotEmpty())
    }
}
