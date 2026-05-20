import React, {useState} from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  LayoutAnimation,
  Platform,
  UIManager,
} from 'react-native';
import {useStore} from '../store/useStore';
import BlurReveal from './BlurReveal';
import {SERIF_FONT} from '../theme/fonts';

if (
  Platform.OS === 'android' &&
  UIManager.setLayoutAnimationEnabledExperimental
) {
  UIManager.setLayoutAnimationEnabledExperimental(true);
}

export default function TodayQuoteCard() {
  const {theme, todayQuote, fontSize} = useStore();

  const [expanded, setExpanded] = useState(false);
  const [readingRevealed, setReadingRevealed] = useState(false);
  const [koreanRevealed, setKoreanRevealed] = useState(false);

  const quoteFontSize = fontSize === 'small' ? 18 : fontSize === 'large' ? 26 : 22;

  const toggleExpanded = () => {
    LayoutAnimation.configureNext({
      duration: 300,
      create: {type: 'easeInEaseOut', property: 'opacity'},
      update: {type: 'easeInEaseOut'},
      delete: {type: 'easeInEaseOut', property: 'opacity'},
    });
    setExpanded(prev => !prev);
    if (expanded) {
      setReadingRevealed(false);
      setKoreanRevealed(false);
    }
  };

  return (
    <View
      style={[
        styles.card,
        {backgroundColor: theme.bgCard, borderColor: theme.border},
      ]}>

      {/* 오늘의 명언 label */}
      <Text style={[styles.todayLabel, {color: theme.textMuted}]}>오늘의 명언</Text>

      {/* Japanese quote */}
      <Text style={[styles.quoteText, {color: theme.text, fontSize: quoteFontSize}]}>
        {todayQuote.japanese}
      </Text>

      {/* Author */}
      <Text style={[styles.author, {color: theme.textMuted}]}>
        — {todayQuote.author}
      </Text>

      {/* Expand toggle */}
      <TouchableOpacity
        style={styles.expandBtn}
        onPress={toggleExpanded}
        activeOpacity={0.7}>
        <Text style={[styles.expandBtnText, {color: theme.accentSoft}]}>
          {expanded ? '∧  닫기' : '∨  읽는 법 · 한국어 보기'}
        </Text>
      </TouchableOpacity>

      {/* Expanded section */}
      {expanded && (
        <View style={[styles.expandedSection, {borderTopColor: theme.border}]}>
          <View style={styles.blurSection}>
            <Text style={[styles.blurLabel, {color: theme.textMuted}]}>읽는 법</Text>
            <BlurReveal
              revealed={readingRevealed}
              onToggle={() => setReadingRevealed(p => !p)}
              label="탭하여 확인"
              closeLabel="닫기">
              <Text style={[styles.readingText, {color: theme.textSub}]}>
                {todayQuote.reading}
              </Text>
            </BlurReveal>
          </View>

          <View style={styles.blurSection}>
            <Text style={[styles.blurLabel, {color: theme.textMuted}]}>한국어</Text>
            <BlurReveal
              revealed={koreanRevealed}
              onToggle={() => setKoreanRevealed(p => !p)}
              label="탭하여 확인"
              closeLabel="닫기">
              <Text style={[styles.koreanText, {color: theme.text}]}>
                {todayQuote.korean}
              </Text>
            </BlurReveal>
          </View>
        </View>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    borderRadius: 16,
    borderWidth: 1,
    padding: 20,
    marginBottom: 24,
  },
  todayLabel: {
    fontSize: 12,
    letterSpacing: 0.5,
    marginBottom: 14,
  },
  quoteText: {
    fontFamily: SERIF_FONT,
    lineHeight: 36,
    letterSpacing: 1,
    marginBottom: 12,
  },
  author: {
    fontSize: 13,
    letterSpacing: 0.3,
    marginBottom: 16,
  },
  expandBtn: {
    alignItems: 'center',
    paddingVertical: 6,
  },
  expandBtnText: {
    fontSize: 13,
    letterSpacing: 0.3,
  },
  expandedSection: {
    marginTop: 16,
    paddingTop: 16,
    borderTopWidth: StyleSheet.hairlineWidth,
    gap: 16,
  },
  blurSection: {gap: 6},
  blurLabel: {
    fontSize: 11,
    letterSpacing: 0.5,
    textTransform: 'uppercase',
  },
  readingText: {
    fontSize: 15,
    lineHeight: 24,
    letterSpacing: 0.5,
    fontStyle: 'italic',
  },
  koreanText: {
    fontSize: 15,
    lineHeight: 24,
    letterSpacing: 0.2,
  },
});
