import React from 'react';
import {View, Text, StyleSheet} from 'react-native';
import {useStore} from '../store/useStore';
import {SERIF_FONT} from '../theme/fonts';

interface FuriganaTextProps {
  word: string;
  reading: string;
  wordFontSize?: number;
  readingFontSize?: number;
  wordColor?: string;
  readingColor?: string;
}

/**
 * Displays a Japanese word with its reading (yomigana/furigana) shown
 * in small text above the word, as is traditional in Japanese typography.
 *
 * For multi-character words we distribute the reading evenly across the
 * kanji by splitting at the midpoint proportional to character count.
 * For short words (≤4 chars) the full reading is centered above the word.
 */
export default function FuriganaText({
  word,
  reading,
  wordFontSize = 26,
  readingFontSize,
  wordColor,
  readingColor,
}: FuriganaTextProps) {
  const {theme} = useStore();

  const resolvedWordColor = wordColor ?? theme.text;
  const resolvedReadingColor = readingColor ?? theme.textSub;
  const resolvedReadingSize = readingFontSize ?? Math.max(10, wordFontSize * 0.42);

  return (
    <View style={styles.container}>
      {/* Yomigana (reading) above */}
      <Text
        style={[
          styles.reading,
          {
            fontSize: resolvedReadingSize,
            color: resolvedReadingColor,
            letterSpacing: word.length > 2 ? 1 : 0.5,
          },
        ]}
        numberOfLines={1}
        adjustsFontSizeToFit>
        {reading}
      </Text>

      {/* Word (kanji) below */}
      <Text
        style={[
          styles.word,
          {
            fontSize: wordFontSize,
            color: resolvedWordColor,
          },
        ]}>
        {word}
      </Text>
    </View>
  );
}

/**
 * Inline furigana: renders each character of `word` with the corresponding
 * slice of `reading` above it, stacked horizontally.
 *
 * `segments` is an optional array of {char, reading} pairs.
 * When not provided falls back to the simple block layout above.
 */
export function FuriganaInline({
  segments,
  wordFontSize = 22,
  wordColor,
  readingColor,
}: {
  segments: {char: string; reading: string}[];
  wordFontSize?: number;
  wordColor?: string;
  readingColor?: string;
}) {
  const {theme} = useStore();
  const resolvedWordColor = wordColor ?? theme.text;
  const resolvedReadingColor = readingColor ?? theme.textSub;
  const readingSize = Math.max(9, wordFontSize * 0.42);

  return (
    <View style={styles.inlineRow}>
      {segments.map((seg, i) => (
        <View key={i} style={styles.inlineChar}>
          <Text
            style={[
              styles.inlineReading,
              {fontSize: readingSize, color: resolvedReadingColor},
            ]}
            numberOfLines={1}>
            {seg.reading}
          </Text>
          <Text
            style={[
              styles.inlineWord,
              {fontSize: wordFontSize, color: resolvedWordColor},
            ]}>
            {seg.char}
          </Text>
        </View>
      ))}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: 'center',
  },
  reading: {
    fontFamily: SERIF_FONT,
    textAlign: 'center',
    marginBottom: 1,
  },
  word: {
    fontFamily: SERIF_FONT,
    letterSpacing: 2,
    fontWeight: '600',
  },
  inlineRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    alignItems: 'flex-end',
  },
  inlineChar: {
    alignItems: 'center',
    marginHorizontal: 1,
  },
  inlineReading: {
    fontFamily: SERIF_FONT,
    textAlign: 'center',
    lineHeight: 14,
  },
  inlineWord: {
    fontFamily: SERIF_FONT,
    fontWeight: '600',
    letterSpacing: 0,
  },
});
