import React, {useState, useRef, useCallback} from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Animated,
  StatusBar,
  ScrollView,
  Dimensions,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {useStore} from '../store/useStore';
import {getVocabByLevel, JLPT_LEVELS} from '../data/vocabulary';
import type {JLPTLevel, VocabWord} from '../data/vocabulary';
import FuriganaText from '../components/FuriganaText';

const {width: SCREEN_WIDTH} = Dimensions.get('window');

const POS_COLORS: Record<string, string> = {
  명사: '#5B9BD5',
  동사: '#70AD47',
  'い형용사': '#ED7D31',
  'な형용사': '#FFC000',
  부사: '#A061C5',
  접속사: '#C8A96E',
  관용구: '#C00000',
  사자성어: '#C8A96E',
};

interface VocabCardScreenProps {
  navigation: any;
  route: any;
}

export default function VocabCardScreen({
  navigation,
  route,
}: VocabCardScreenProps) {
  const level: JLPTLevel = route?.params?.level ?? 'N5';
  const startIndex: number = route?.params?.startIndex ?? 0;

  const {theme, fontSize} = useStore();

  const words = getVocabByLevel(level);
  const [currentIndex, setCurrentIndex] = useState(startIndex);
  const [readingRevealed, setReadingRevealed] = useState(false);
  const [koreanRevealed, setKoreanRevealed] = useState(false);
  const [exampleVisible, setExampleVisible] = useState(false);

  const slideAnim = useRef(new Animated.Value(0)).current;
  const cardOpacity = useRef(new Animated.Value(1)).current;
  const flipAnim = useRef(new Animated.Value(0)).current;

  const word: VocabWord = words[currentIndex];
  const levelInfo = JLPT_LEVELS.find(l => l.level === level)!;
  const posColor = POS_COLORS[word.pos] ?? theme.accentSoft;

  const kanjiFontSize =
    fontSize === 'small' ? 52 : fontSize === 'large' ? 76 : 64;

  const navigateWord = useCallback(
    (direction: 'prev' | 'next') => {
      const nextIndex =
        direction === 'next'
          ? (currentIndex + 1) % words.length
          : (currentIndex - 1 + words.length) % words.length;

      const toValue = direction === 'next' ? -SCREEN_WIDTH : SCREEN_WIDTH;

      Animated.parallel([
        Animated.timing(cardOpacity, {
          toValue: 0,
          duration: 120,
          useNativeDriver: true,
        }),
        Animated.timing(slideAnim, {
          toValue,
          duration: 120,
          useNativeDriver: true,
        }),
      ]).start(() => {
        slideAnim.setValue(-toValue * 0.4);
        cardOpacity.setValue(0);
        setCurrentIndex(nextIndex);
        setReadingRevealed(false);
        setKoreanRevealed(false);
        setExampleVisible(false);

        Animated.parallel([
          Animated.spring(slideAnim, {
            toValue: 0,
            useNativeDriver: true,
            speed: 22,
            bounciness: 5,
          }),
          Animated.timing(cardOpacity, {
            toValue: 1,
            duration: 180,
            useNativeDriver: true,
          }),
        ]).start();
      });
    },
    [currentIndex, words.length, slideAnim, cardOpacity],
  );

  const progressPercent = ((currentIndex + 1) / words.length) * 100;

  return (
    <SafeAreaView
      style={[styles.safeArea, {backgroundColor: theme.bg}]}
      edges={['top', 'left', 'right']}>
      <StatusBar barStyle="light-content" backgroundColor={theme.bg} />

      {/* ── Header ── */}
      <View style={[styles.header, {borderBottomColor: theme.border}]}>
        <TouchableOpacity
          onPress={() => navigation.goBack()}
          hitSlop={12}
          style={styles.backBtn}>
          <Text style={[styles.backIcon, {color: theme.accent}]}>←</Text>
        </TouchableOpacity>
        <View style={styles.headerCenter}>
          <Text style={[styles.headerLevel, {color: levelInfo.color}]}>
            {levelInfo.emoji} JLPT {level}
          </Text>
          <Text style={[styles.headerDesc, {color: theme.textMuted}]}>
            {levelInfo.description}
          </Text>
        </View>
        <View style={styles.headerRight} />
      </View>

      {/* ── Progress Bar ── */}
      <View style={[styles.progressTrack, {backgroundColor: theme.progressTrack}]}>
        <View
          style={[
            styles.progressFill,
            {
              backgroundColor: levelInfo.color,
              width: `${progressPercent}%`,
            },
          ]}
        />
      </View>
      <Text style={[styles.progressLabel, {color: theme.textMuted}]}>
        {currentIndex + 1} / {words.length}
      </Text>

      <ScrollView
        contentContainerStyle={styles.scrollContent}
        showsVerticalScrollIndicator={false}>

        {/* ── Main Card ── */}
        <Animated.View
          style={[
            styles.card,
            {
              backgroundColor: theme.bgCard,
              borderColor: theme.border,
              shadowColor: levelInfo.color,
              transform: [{translateX: slideAnim}],
              opacity: cardOpacity,
            },
          ]}>
          {/* Level badge */}
          <View style={styles.cardHeader}>
            <View
              style={[
                styles.levelBadge,
                {backgroundColor: levelInfo.color + '22', borderColor: levelInfo.color},
              ]}>
              <Text style={[styles.levelBadgeText, {color: levelInfo.color}]}>
                {level}
              </Text>
            </View>
            <View
              style={[
                styles.posBadge,
                {backgroundColor: posColor + '22', borderColor: posColor},
              ]}>
              <Text style={[styles.posBadgeText, {color: posColor}]}>
                {word.pos}
              </Text>
            </View>
          </View>

          {/* ── ① 한자 (항상 표시) ── */}
          <View style={styles.kanjiSection}>
            <Text
              style={[
                styles.kanjiText,
                {color: theme.text, fontSize: kanjiFontSize},
              ]}>
              {word.kanji}
            </Text>
          </View>

          <View style={[styles.divider, {backgroundColor: theme.border}]} />

          {/* ── ② 요미가나 (블러) ── */}
          <View style={styles.blurRow}>
            <Text style={[styles.blurRowTitle, {color: theme.textMuted}]}>
              読み方 (읽는 법)
            </Text>
            {readingRevealed ? (
              <TouchableOpacity
                style={[
                  styles.revealedBlock,
                  {backgroundColor: theme.bgCardAlt, borderColor: theme.accentSoft},
                ]}
                onPress={() => setReadingRevealed(false)}
                activeOpacity={0.85}>
                {/* 후리가나 스타일 표시 */}
                <FuriganaText
                  word={word.kanji}
                  reading={word.reading}
                  wordFontSize={28}
                  wordColor={theme.text}
                  readingColor={theme.accentSoft}
                />
                <Text style={[styles.revealedClose, {color: theme.textMuted}]}>
                  닫기
                </Text>
              </TouchableOpacity>
            ) : (
              <TouchableOpacity
                style={[
                  styles.blurBlock,
                  {backgroundColor: theme.bgCardAlt, borderColor: theme.border},
                ]}
                onPress={() => setReadingRevealed(true)}
                activeOpacity={0.85}>
                <Text style={[styles.blurHint, {color: theme.textMuted}]}>
                  🔤 탭하여 요미가나 확인
                </Text>
              </TouchableOpacity>
            )}
          </View>

          {/* ── ③ 한국어 (블러) ── */}
          <View style={styles.blurRow}>
            <Text style={[styles.blurRowTitle, {color: theme.textMuted}]}>
              🇰🇷 한국어 의미
            </Text>
            {koreanRevealed ? (
              <TouchableOpacity
                style={[
                  styles.revealedBlock,
                  {backgroundColor: theme.bgCardAlt, borderColor: theme.accentSoft},
                ]}
                onPress={() => setKoreanRevealed(false)}
                activeOpacity={0.85}>
                <View style={styles.meaningsWrap}>
                  {word.korean.map((k, i) => (
                    <View
                      key={i}
                      style={[
                        styles.meaningChip,
                        {backgroundColor: theme.bg, borderColor: theme.border},
                      ]}>
                      <Text style={[styles.meaningChipText, {color: theme.text}]}>
                        {k}
                      </Text>
                    </View>
                  ))}
                </View>
                <Text style={[styles.revealedClose, {color: theme.textMuted}]}>
                  닫기
                </Text>
              </TouchableOpacity>
            ) : (
              <TouchableOpacity
                style={[
                  styles.blurBlock,
                  {backgroundColor: theme.bgCardAlt, borderColor: theme.border},
                ]}
                onPress={() => setKoreanRevealed(true)}
                activeOpacity={0.85}>
                <Text style={[styles.blurHint, {color: theme.textMuted}]}>
                  🇰🇷 탭하여 한국어 뜻 확인
                </Text>
              </TouchableOpacity>
            )}
          </View>

          {/* ── 예문 (선택) ── */}
          {word.example && (
            <View style={styles.exampleSection}>
              <TouchableOpacity
                style={[styles.exampleToggle, {borderTopColor: theme.border}]}
                onPress={() => setExampleVisible(p => !p)}
                activeOpacity={0.75}>
                <Text style={[styles.exampleToggleLabel, {color: theme.textMuted}]}>
                  예문 보기
                </Text>
                <Text style={[styles.exampleArrow, {color: theme.accentSoft}]}>
                  {exampleVisible ? '▲' : '▼'}
                </Text>
              </TouchableOpacity>
              {exampleVisible && (
                <View
                  style={[
                    styles.exampleBlock,
                    {backgroundColor: theme.bgCardAlt, borderColor: theme.border},
                  ]}>
                  <Text style={[styles.exampleJP, {color: theme.text}]}>
                    {word.example.japanese}
                  </Text>
                  <Text style={[styles.exampleReading, {color: theme.textSub}]}>
                    {word.example.reading}
                  </Text>
                  <Text style={[styles.exampleKO, {color: theme.textMuted}]}>
                    {word.example.korean}
                  </Text>
                </View>
              )}
            </View>
          )}
        </Animated.View>

        {/* ── Navigation ── */}
        <View style={styles.navRow}>
          <TouchableOpacity
            style={[
              styles.navBtn,
              {backgroundColor: theme.bgCardAlt, borderColor: theme.border},
            ]}
            onPress={() => navigateWord('prev')}
            activeOpacity={0.75}>
            <Text style={[styles.navIcon, {color: theme.accent}]}>◀</Text>
            <Text style={[styles.navLabel, {color: theme.textMuted}]}>이전</Text>
          </TouchableOpacity>

          <View
            style={[
              styles.indexBadge,
              {backgroundColor: theme.bgCardAlt, borderColor: levelInfo.color + '55'},
            ]}>
            <Text style={[styles.indexCurrent, {color: levelInfo.color}]}>
              {currentIndex + 1}
            </Text>
            <Text style={[styles.indexSep, {color: theme.textMuted}]}>/</Text>
            <Text style={[styles.indexTotal, {color: theme.textMuted}]}>
              {words.length}
            </Text>
          </View>

          <TouchableOpacity
            style={[
              styles.navBtn,
              {backgroundColor: theme.bgCardAlt, borderColor: theme.border},
            ]}
            onPress={() => navigateWord('next')}
            activeOpacity={0.75}>
            <Text style={[styles.navLabel, {color: theme.textMuted}]}>다음</Text>
            <Text style={[styles.navIcon, {color: theme.accent}]}>▶</Text>
          </TouchableOpacity>
        </View>

        {/* ── 전체 다시 보기 버튼 ── */}
        <TouchableOpacity
          style={[
            styles.resetBtn,
            {borderColor: theme.border, backgroundColor: theme.bgCardAlt},
          ]}
          onPress={() => {
            setCurrentIndex(0);
            setReadingRevealed(false);
            setKoreanRevealed(false);
            setExampleVisible(false);
          }}
          activeOpacity={0.75}>
          <Text style={[styles.resetBtnText, {color: theme.textMuted}]}>
            ↺ 처음부터 다시
          </Text>
        </TouchableOpacity>

        <View style={{height: 32}} />
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {flex: 1},
  header: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingHorizontal: 16,
    paddingVertical: 12,
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  backBtn: {width: 36},
  backIcon: {fontSize: 22, fontWeight: '300'},
  headerCenter: {flex: 1, alignItems: 'center'},
  headerLevel: {
    fontSize: 16,
    fontFamily: 'Georgia',
    letterSpacing: 1,
    fontWeight: '700',
  },
  headerDesc: {fontSize: 10, letterSpacing: 0.5, marginTop: 1},
  headerRight: {width: 36},

  progressTrack: {height: 3, width: '100%'},
  progressFill: {height: 3, borderRadius: 1.5},
  progressLabel: {
    fontSize: 10,
    textAlign: 'right',
    paddingRight: 16,
    marginTop: 4,
    letterSpacing: 0.3,
  },

  scrollContent: {padding: 16, paddingTop: 12},

  card: {
    borderRadius: 20,
    borderWidth: 1,
    padding: 24,
    marginBottom: 20,
    shadowOffset: {width: 0, height: 6},
    shadowOpacity: 0.2,
    shadowRadius: 16,
    elevation: 10,
    gap: 16,
  },
  cardHeader: {
    flexDirection: 'row',
    gap: 8,
  },
  levelBadge: {
    borderRadius: 6,
    borderWidth: 1,
    paddingVertical: 3,
    paddingHorizontal: 10,
  },
  levelBadgeText: {fontSize: 11, fontWeight: '800', letterSpacing: 0.5},
  posBadge: {
    borderRadius: 6,
    borderWidth: 1,
    paddingVertical: 3,
    paddingHorizontal: 10,
  },
  posBadgeText: {fontSize: 11, fontWeight: '600', letterSpacing: 0.3},

  // ① 한자
  kanjiSection: {
    alignItems: 'center',
    paddingVertical: 16,
  },
  kanjiText: {
    fontFamily: 'Georgia',
    letterSpacing: 4,
    fontWeight: '700',
    textAlign: 'center',
  },

  divider: {height: StyleSheet.hairlineWidth},

  // ② ③ 블러 rows
  blurRow: {gap: 8},
  blurRowTitle: {
    fontSize: 10,
    letterSpacing: 0.8,
    textTransform: 'uppercase',
  },
  blurBlock: {
    borderRadius: 12,
    borderWidth: 1,
    paddingVertical: 18,
    alignItems: 'center',
    justifyContent: 'center',
  },
  blurHint: {fontSize: 13, letterSpacing: 0.3},
  revealedBlock: {
    borderRadius: 12,
    borderWidth: 1,
    padding: 16,
    alignItems: 'center',
    gap: 8,
  },
  revealedClose: {
    fontSize: 10,
    letterSpacing: 0.5,
    alignSelf: 'flex-end',
  },
  meaningsWrap: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
    justifyContent: 'center',
  },
  meaningChip: {
    borderRadius: 8,
    borderWidth: 1,
    paddingVertical: 6,
    paddingHorizontal: 14,
  },
  meaningChipText: {fontSize: 16, letterSpacing: 0.3, fontWeight: '600'},

  // 예문
  exampleSection: {gap: 8},
  exampleToggle: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingTop: 10,
    borderTopWidth: StyleSheet.hairlineWidth,
  },
  exampleToggleLabel: {fontSize: 12, letterSpacing: 0.3},
  exampleArrow: {fontSize: 10},
  exampleBlock: {
    borderRadius: 10,
    borderWidth: 1,
    padding: 14,
    gap: 4,
  },
  exampleJP: {
    fontSize: 15,
    fontFamily: 'Georgia',
    letterSpacing: 1,
    lineHeight: 26,
  },
  exampleReading: {fontSize: 12, letterSpacing: 0.4, fontStyle: 'italic'},
  exampleKO: {fontSize: 13, lineHeight: 20, letterSpacing: 0.2},

  // Navigation
  navRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
    marginBottom: 12,
  },
  navBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    borderRadius: 12,
    borderWidth: 1,
    paddingVertical: 14,
    paddingHorizontal: 20,
    gap: 8,
    flex: 1,
    justifyContent: 'center',
  },
  navIcon: {fontSize: 14},
  navLabel: {fontSize: 13, letterSpacing: 0.3},
  indexBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    borderRadius: 10,
    borderWidth: 1,
    paddingVertical: 10,
    paddingHorizontal: 14,
    gap: 4,
  },
  indexCurrent: {fontSize: 18, fontWeight: '800', letterSpacing: 0.5},
  indexSep: {fontSize: 12},
  indexTotal: {fontSize: 13},
  resetBtn: {
    borderRadius: 10,
    borderWidth: 1,
    paddingVertical: 12,
    alignItems: 'center',
  },
  resetBtnText: {fontSize: 13, letterSpacing: 0.3},
});
