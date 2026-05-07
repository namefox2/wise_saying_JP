import React, {useState, useRef, useEffect} from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Animated,
  LayoutAnimation,
  Platform,
  UIManager,
} from 'react-native';
import {useStore} from '../store/useStore';
import BlurReveal from './BlurReveal';

if (
  Platform.OS === 'android' &&
  UIManager.setLayoutAnimationEnabledExperimental
) {
  UIManager.setLayoutAnimationEnabledExperimental(true);
}

export default function TodayQuoteCard() {
  const {theme, todayQuote, isFavorite, toggleFavorite, fontSize} = useStore();

  const [expanded, setExpanded] = useState(false);
  const [readingRevealed, setReadingRevealed] = useState(false);
  const [koreanRevealed, setKoreanRevealed] = useState(false);

  const expandAnim = useRef(new Animated.Value(0)).current;
  const heartScale = useRef(new Animated.Value(1)).current;
  const favorite = isFavorite(todayQuote.id);

  const quoteFontSize =
    fontSize === 'small' ? 18 : fontSize === 'large' ? 26 : 22;

  const toggleExpanded = () => {
    LayoutAnimation.configureNext({
      duration: 350,
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

  const handleFavorite = () => {
    Animated.sequence([
      Animated.spring(heartScale, {
        toValue: 1.4,
        useNativeDriver: true,
        speed: 30,
      }),
      Animated.spring(heartScale, {
        toValue: 1,
        useNativeDriver: true,
        speed: 20,
      }),
    ]).start();
    toggleFavorite(todayQuote.id);
  };

  return (
    <View
      style={[
        styles.card,
        {
          backgroundColor: theme.bgCard,
          borderColor: theme.border,
          shadowColor: theme.accent,
        },
      ]}>
      {/* Header */}
      <View style={styles.headerRow}>
        <View style={styles.todayBadge}>
          <Text style={[styles.todayBadgeText, {color: theme.accent}]}>
            오늘의 명언
          </Text>
          <Text style={[styles.todayKanji, {color: theme.accentSoft}]}>
            今日の言葉
          </Text>
        </View>
        <Animated.View style={{transform: [{scale: heartScale}]}}>
          <TouchableOpacity onPress={handleFavorite} hitSlop={12}>
            <Text style={styles.heartIcon}>{favorite ? '♥' : '♡'}</Text>
          </TouchableOpacity>
        </Animated.View>
      </View>

      {/* Japanese quote */}
      <Text
        style={[
          styles.quoteText,
          {color: theme.text, fontSize: quoteFontSize},
        ]}>
        {todayQuote.japanese}
      </Text>

      {/* Author */}
      <Text style={[styles.author, {color: theme.textMuted}]}>
        — {todayQuote.author}
      </Text>

      {/* Expand toggle button */}
      <TouchableOpacity
        style={[
          styles.expandButton,
          {
            borderColor: expanded ? theme.accentSoft : theme.border,
            backgroundColor: expanded ? theme.bgCardAlt : 'transparent',
          },
        ]}
        onPress={toggleExpanded}
        activeOpacity={0.8}>
        <Text style={[styles.expandButtonText, {color: theme.accent}]}>
          {expanded ? '닫기 ▲' : '읽는 법 · 한국어 보기 ▼'}
        </Text>
      </TouchableOpacity>

      {/* Expanded section */}
      {expanded && (
        <View style={styles.expandedSection}>
          {/* Reading blur */}
          <View style={styles.blurSection}>
            <Text style={[styles.blurSectionTitle, {color: theme.textMuted}]}>
              🔤 읽는 법
            </Text>
            <BlurReveal
              revealed={readingRevealed}
              onToggle={() => setReadingRevealed(p => !p)}
              label="탭하여 히라가나 확인"
              closeLabel="닫기">
              <Text style={[styles.readingText, {color: theme.textSub}]}>
                {todayQuote.reading}
              </Text>
            </BlurReveal>
          </View>

          {/* Korean blur */}
          <View style={styles.blurSection}>
            <Text style={[styles.blurSectionTitle, {color: theme.textMuted}]}>
              🇰🇷 한국어
            </Text>
            <BlurReveal
              revealed={koreanRevealed}
              onToggle={() => setKoreanRevealed(p => !p)}
              label="탭하여 한국어 확인"
              closeLabel="닫기">
              <Text style={[styles.koreanText, {color: theme.text}]}>
                {todayQuote.korean}
              </Text>
            </BlurReveal>
          </View>

          {/* Category tag */}
          <View style={styles.categoryRow}>
            <View
              style={[
                styles.categoryTag,
                {
                  backgroundColor: theme.bgCardAlt,
                  borderColor: theme.accentSoft,
                },
              ]}>
              <Text style={[styles.categoryTagText, {color: theme.accentSoft}]}>
                #{todayQuote.category}
              </Text>
            </View>
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
    shadowOffset: {width: 0, height: 4},
    shadowOpacity: 0.15,
    shadowRadius: 12,
    elevation: 8,
  },
  headerRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: 14,
  },
  todayBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 8,
  },
  todayBadgeText: {
    fontSize: 12,
    fontWeight: '600',
    letterSpacing: 0.5,
  },
  todayKanji: {
    fontSize: 11,
    letterSpacing: 0.3,
  },
  heartIcon: {
    fontSize: 22,
    color: '#E8507A',
  },
  quoteText: {
    fontFamily: 'Georgia',
    lineHeight: 36,
    letterSpacing: 1,
    marginBottom: 10,
  },
  author: {
    fontSize: 12,
    letterSpacing: 0.5,
    marginBottom: 16,
    textAlign: 'right',
  },
  expandButton: {
    borderRadius: 8,
    borderWidth: 1,
    paddingVertical: 10,
    paddingHorizontal: 14,
    alignItems: 'center',
  },
  expandButtonText: {
    fontSize: 13,
    letterSpacing: 0.3,
    fontWeight: '500',
  },
  expandedSection: {
    marginTop: 16,
    gap: 12,
  },
  blurSection: {
    gap: 6,
  },
  blurSectionTitle: {
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
  categoryRow: {
    flexDirection: 'row',
    marginTop: 4,
  },
  categoryTag: {
    borderRadius: 6,
    borderWidth: 1,
    paddingVertical: 4,
    paddingHorizontal: 10,
  },
  categoryTagText: {
    fontSize: 11,
    letterSpacing: 0.3,
  },
});
