import React, {useState, useRef, useCallback} from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Animated,
  Dimensions,
  StatusBar,
  ScrollView,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {useStore} from '../store/useStore';
import BlurReveal from '../components/BlurReveal';
import {getQuotesByCategory, CATEGORIES} from '../data/quotes';
import type {Category, Quote} from '../data/quotes';

const {width: SCREEN_WIDTH} = Dimensions.get('window');

interface QuoteCardScreenProps {
  navigation: any;
  route: any;
}

export default function QuoteCardScreen({
  navigation,
  route,
}: QuoteCardScreenProps) {
  const category: Category = route?.params?.category ?? 'all';
  const startIndex: number = route?.params?.startIndex ?? 0;

  const {theme, isFavorite, toggleFavorite, markQuoteSeen, fontSize, activeQuotes} =
    useStore();

  const quotes = getQuotesByCategory(category, activeQuotes ?? undefined);
  const [currentIndex, setCurrentIndex] = useState(startIndex);
  const [readingRevealed, setReadingRevealed] = useState(false);
  const [koreanRevealed, setKoreanRevealed] = useState(false);

  const slideAnim = useRef(new Animated.Value(0)).current;
  const heartScale = useRef(new Animated.Value(1)).current;
  const cardOpacity = useRef(new Animated.Value(1)).current;

  const quote: Quote = quotes[currentIndex];
  const favorite = isFavorite(quote.id);
  const categoryInfo = CATEGORIES.find(c => c.id === category) ?? CATEGORIES[0];

  const quoteFontSize =
    fontSize === 'small' ? 20 : fontSize === 'large' ? 30 : 24;

  const navigateQuote = useCallback(
    (direction: 'prev' | 'next') => {
      const nextIndex =
        direction === 'next'
          ? (currentIndex + 1) % quotes.length
          : (currentIndex - 1 + quotes.length) % quotes.length;

      const toValue = direction === 'next' ? -SCREEN_WIDTH : SCREEN_WIDTH;

      Animated.sequence([
        Animated.parallel([
          Animated.timing(cardOpacity, {
            toValue: 0,
            duration: 150,
            useNativeDriver: true,
          }),
          Animated.timing(slideAnim, {
            toValue,
            duration: 150,
            useNativeDriver: true,
          }),
        ]),
      ]).start(() => {
        slideAnim.setValue(-toValue * 0.4);
        cardOpacity.setValue(0);
        setCurrentIndex(nextIndex);
        setReadingRevealed(false);
        setKoreanRevealed(false);
        markQuoteSeen();
        Animated.parallel([
          Animated.spring(slideAnim, {
            toValue: 0,
            useNativeDriver: true,
            speed: 20,
            bounciness: 4,
          }),
          Animated.timing(cardOpacity, {
            toValue: 1,
            duration: 200,
            useNativeDriver: true,
          }),
        ]).start();
      });
    },
    [currentIndex, quotes.length, slideAnim, cardOpacity, markQuoteSeen],
  );

  const handleFavorite = () => {
    Animated.sequence([
      Animated.spring(heartScale, {
        toValue: 1.5,
        useNativeDriver: true,
        speed: 30,
      }),
      Animated.spring(heartScale, {
        toValue: 1,
        useNativeDriver: true,
        speed: 20,
      }),
    ]).start();
    toggleFavorite(quote.id);
  };

  const progressPercent = ((currentIndex + 1) / quotes.length) * 100;

  return (
    <SafeAreaView
      style={[styles.safeArea, {backgroundColor: theme.bg}]}
      edges={['top', 'left', 'right']}>
      <StatusBar barStyle="light-content" backgroundColor={theme.bg} />

      {/* ── Header ── */}
      <View
        style={[
          styles.header,
          {borderBottomColor: theme.border},
        ]}>
        <TouchableOpacity
          onPress={() => navigation.goBack()}
          style={styles.backBtn}
          hitSlop={12}>
          <Text style={[styles.backIcon, {color: theme.accent}]}>←</Text>
        </TouchableOpacity>
        <View style={styles.headerCenter}>
          <Text style={[styles.headerKanji, {color: theme.text}]}>
            {categoryInfo.emoji} {categoryInfo.kanji}
          </Text>
          <Text style={[styles.headerLabel, {color: theme.textMuted}]}>
            {categoryInfo.label}
          </Text>
        </View>
        <Animated.View style={{transform: [{scale: heartScale}]}}>
          <TouchableOpacity onPress={handleFavorite} hitSlop={12}>
            <Text
              style={[
                styles.heartIcon,
                {color: favorite ? '#E8507A' : theme.textMuted},
              ]}>
              {favorite ? '♥' : '♡'}
            </Text>
          </TouchableOpacity>
        </Animated.View>
      </View>

      {/* ── Progress Bar ── */}
      <View
        style={[styles.progressTrack, {backgroundColor: theme.progressTrack}]}>
        <Animated.View
          style={[
            styles.progressFill,
            {
              backgroundColor: theme.progressFill,
              width: `${progressPercent}%`,
            },
          ]}
        />
      </View>
      <Text style={[styles.progressLabel, {color: theme.textMuted}]}>
        {currentIndex + 1} / {quotes.length}
      </Text>

      {/* ── Main Quote Card ── */}
      <ScrollView
        contentContainerStyle={styles.scrollContent}
        showsVerticalScrollIndicator={false}>
        <Animated.View
          style={[
            styles.quoteCard,
            {
              backgroundColor: theme.bgCard,
              borderColor: theme.border,
              shadowColor: theme.accent,
              transform: [{translateX: slideAnim}],
              opacity: cardOpacity,
            },
          ]}>
          {/* Decorative corner */}
          <View
            style={[styles.cornerAccent, {borderColor: theme.accentSoft}]}
          />
          <View
            style={[
              styles.cornerAccentBR,
              {borderColor: theme.accentSoft},
            ]}
          />

          {/* Quote text */}
          <Text
            style={[
              styles.quoteText,
              {color: theme.text, fontSize: quoteFontSize},
            ]}>
            {quote.japanese}
          </Text>

          {/* Author */}
          <Text style={[styles.author, {color: theme.accentSoft}]}>
            — {quote.author}
          </Text>

          {/* Divider */}
          <View
            style={[styles.cardDivider, {backgroundColor: theme.border}]}
          />

          {/* Reading blur */}
          <View style={styles.blurSection}>
            <Text style={[styles.blurLabel, {color: theme.textMuted}]}>
              🔤 읽는 법 (ひらがな)
            </Text>
            <BlurReveal
              revealed={readingRevealed}
              onToggle={() => setReadingRevealed(p => !p)}
              label="탭하여 히라가나 독음 확인"
              closeLabel="닫기">
              <Text style={[styles.readingText, {color: theme.textSub}]}>
                {quote.reading}
              </Text>
            </BlurReveal>
          </View>

          {/* Korean blur */}
          <View style={styles.blurSection}>
            <Text style={[styles.blurLabel, {color: theme.textMuted}]}>
              🇰🇷 한국어 뜻
            </Text>
            <BlurReveal
              revealed={koreanRevealed}
              onToggle={() => setKoreanRevealed(p => !p)}
              label="탭하여 한국어 의미 확인"
              closeLabel="닫기">
              <Text style={[styles.koreanText, {color: theme.text}]}>
                {quote.korean}
              </Text>
            </BlurReveal>
          </View>
        </Animated.View>

        {/* ── Navigation Buttons ── */}
        <View style={styles.navRow}>
          <TouchableOpacity
            style={[
              styles.navBtn,
              {
                backgroundColor: theme.bgCardAlt,
                borderColor: theme.border,
              },
            ]}
            onPress={() => navigateQuote('prev')}
            activeOpacity={0.75}>
            <Text style={[styles.navBtnIcon, {color: theme.accent}]}>◀</Text>
            <Text style={[styles.navBtnLabel, {color: theme.textMuted}]}>
              이전
            </Text>
          </TouchableOpacity>

          <View
            style={[
              styles.indexBadge,
              {backgroundColor: theme.bgCardAlt, borderColor: theme.border},
            ]}>
            <Text style={[styles.indexText, {color: theme.accent}]}>
              {currentIndex + 1}
            </Text>
            <Text style={[styles.indexSep, {color: theme.textMuted}]}>/</Text>
            <Text style={[styles.indexTotal, {color: theme.textMuted}]}>
              {quotes.length}
            </Text>
          </View>

          <TouchableOpacity
            style={[
              styles.navBtn,
              {
                backgroundColor: theme.bgCardAlt,
                borderColor: theme.border,
              },
            ]}
            onPress={() => navigateQuote('next')}
            activeOpacity={0.75}>
            <Text style={[styles.navBtnLabel, {color: theme.textMuted}]}>
              다음
            </Text>
            <Text style={[styles.navBtnIcon, {color: theme.accent}]}>▶</Text>
          </TouchableOpacity>
        </View>

        <View style={{height: 32}} />
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingVertical: 12,
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  backBtn: {
    width: 36,
  },
  backIcon: {
    fontSize: 22,
    fontWeight: '300',
  },
  headerCenter: {
    flex: 1,
    alignItems: 'center',
  },
  headerKanji: {
    fontSize: 16,
    fontFamily: 'Georgia',
    letterSpacing: 1,
    fontWeight: '600',
  },
  headerLabel: {
    fontSize: 10,
    letterSpacing: 0.5,
    marginTop: 1,
  },
  heartIcon: {
    fontSize: 24,
    width: 36,
    textAlign: 'right',
  },
  progressTrack: {
    height: 3,
    width: '100%',
  },
  progressFill: {
    height: 3,
    borderRadius: 1.5,
  },
  progressLabel: {
    fontSize: 10,
    textAlign: 'right',
    paddingRight: 16,
    marginTop: 4,
    letterSpacing: 0.3,
  },
  scrollContent: {
    padding: 16,
    paddingTop: 12,
  },
  quoteCard: {
    borderRadius: 20,
    borderWidth: 1,
    padding: 28,
    marginBottom: 20,
    shadowOffset: {width: 0, height: 6},
    shadowOpacity: 0.2,
    shadowRadius: 16,
    elevation: 10,
    overflow: 'hidden',
    minHeight: 320,
  },
  cornerAccent: {
    position: 'absolute',
    top: 12,
    left: 12,
    width: 20,
    height: 20,
    borderTopWidth: 1.5,
    borderLeftWidth: 1.5,
    borderRadius: 3,
  },
  cornerAccentBR: {
    position: 'absolute',
    bottom: 12,
    right: 12,
    width: 20,
    height: 20,
    borderBottomWidth: 1.5,
    borderRightWidth: 1.5,
    borderRadius: 3,
  },
  quoteText: {
    fontFamily: 'Georgia',
    lineHeight: 44,
    letterSpacing: 2,
    marginBottom: 16,
    marginTop: 8,
    textAlign: 'center',
  },
  author: {
    fontSize: 13,
    letterSpacing: 0.5,
    textAlign: 'right',
    marginBottom: 20,
    fontStyle: 'italic',
  },
  cardDivider: {
    height: StyleSheet.hairlineWidth,
    marginBottom: 20,
  },
  blurSection: {
    marginBottom: 14,
    gap: 6,
  },
  blurLabel: {
    fontSize: 10,
    letterSpacing: 0.8,
    textTransform: 'uppercase',
  },
  readingText: {
    fontSize: 15,
    lineHeight: 26,
    letterSpacing: 0.5,
    fontStyle: 'italic',
    paddingVertical: 4,
  },
  koreanText: {
    fontSize: 15,
    lineHeight: 26,
    letterSpacing: 0.2,
    paddingVertical: 4,
  },
  navRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    gap: 12,
  },
  navBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    borderRadius: 12,
    borderWidth: 1,
    paddingVertical: 12,
    paddingHorizontal: 20,
    gap: 8,
    flex: 1,
    justifyContent: 'center',
  },
  navBtnIcon: {
    fontSize: 14,
  },
  navBtnLabel: {
    fontSize: 13,
    letterSpacing: 0.3,
  },
  indexBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    borderRadius: 10,
    borderWidth: 1,
    paddingVertical: 10,
    paddingHorizontal: 14,
    gap: 4,
  },
  indexText: {
    fontSize: 16,
    fontWeight: '700',
    letterSpacing: 0.5,
  },
  indexSep: {
    fontSize: 12,
  },
  indexTotal: {
    fontSize: 13,
  },
});
