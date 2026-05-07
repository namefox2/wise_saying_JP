import React from 'react';
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  StatusBar,
  Dimensions,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {useStore} from '../store/useStore';
import {JLPT_LEVELS, getVocabByLevel} from '../data/vocabulary';

const {width} = Dimensions.get('window');
const CARD_GAP = 12;
const H_PAD = 20;
const CARD_W = (width - H_PAD * 2 - CARD_GAP) / 2;

interface VocabHomeScreenProps {
  navigation: any;
}

export default function VocabHomeScreen({navigation}: VocabHomeScreenProps) {
  const {theme} = useStore();

  return (
    <SafeAreaView
      style={[styles.safeArea, {backgroundColor: theme.bg}]}
      edges={['top', 'left', 'right']}>
      <StatusBar barStyle="light-content" backgroundColor={theme.bg} />

      {/* Header */}
      <View style={[styles.header, {borderBottomColor: theme.border}]}>
        <View>
          <Text style={[styles.headerTitle, {color: theme.accent}]}>
            単語カード
          </Text>
          <Text style={[styles.headerSub, {color: theme.textMuted}]}>
            단어 카드 — JLPT N1~N5
          </Text>
        </View>
        <View
          style={[
            styles.totalBadge,
            {backgroundColor: theme.bgCardAlt, borderColor: theme.border},
          ]}>
          <Text style={[styles.totalText, {color: theme.accent}]}>150</Text>
          <Text style={[styles.totalLabel, {color: theme.textMuted}]}>단어</Text>
        </View>
      </View>

      <ScrollView
        contentContainerStyle={styles.scrollContent}
        showsVerticalScrollIndicator={false}>

        {/* Description */}
        <View
          style={[
            styles.descBox,
            {backgroundColor: theme.bgCard, borderColor: theme.border},
          ]}>
          <Text style={[styles.descText, {color: theme.textMuted}]}>
            한자를 보고 읽는 법과 뜻을 맞혀보세요.{'\n'}
            레벨을 선택하여 단어 카드를 시작합니다.
          </Text>
          <View style={styles.stepsRow}>
            {['한자 확인', '요미가나 탭', '한국어 탭'].map((s, i) => (
              <View key={s} style={styles.stepItem}>
                <View
                  style={[
                    styles.stepCircle,
                    {backgroundColor: theme.accent + '22', borderColor: theme.accentSoft},
                  ]}>
                  <Text style={[styles.stepNum, {color: theme.accent}]}>
                    {i + 1}
                  </Text>
                </View>
                <Text style={[styles.stepLabel, {color: theme.textMuted}]}>
                  {s}
                </Text>
              </View>
            ))}
          </View>
        </View>

        {/* Level grid */}
        <Text style={[styles.sectionLabel, {color: theme.textMuted}]}>
          레벨 선택
        </Text>
        <View style={styles.grid}>
          {JLPT_LEVELS.map(lvl => {
            const count = getVocabByLevel(lvl.level).length;
            return (
              <TouchableOpacity
                key={lvl.level}
                style={[
                  styles.levelCard,
                  {
                    width: CARD_W,
                    backgroundColor: theme.bgCard,
                    borderColor: theme.border,
                    shadowColor: lvl.color,
                  },
                ]}
                onPress={() =>
                  navigation.navigate('VocabCard', {level: lvl.level})
                }
                activeOpacity={0.8}>
                {/* Top color bar */}
                <View
                  style={[styles.colorBar, {backgroundColor: lvl.color}]}
                />
                <Text style={styles.levelEmoji}>{lvl.emoji}</Text>
                <Text
                  style={[
                    styles.levelLabel,
                    {color: lvl.color, fontWeight: '800'},
                  ]}>
                  {lvl.label}
                </Text>
                <Text style={[styles.levelDesc, {color: theme.textMuted}]}>
                  {lvl.description}
                </Text>
                <View
                  style={[
                    styles.countBadge,
                    {backgroundColor: lvl.color + '18', borderColor: lvl.color + '55'},
                  ]}>
                  <Text style={[styles.countText, {color: lvl.color}]}>
                    {count}단어
                  </Text>
                </View>

                {/* Arrow */}
                <Text style={[styles.arrow, {color: lvl.color}]}>→</Text>
              </TouchableOpacity>
            );
          })}
        </View>

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
    justifyContent: 'space-between',
    paddingHorizontal: 20,
    paddingVertical: 14,
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  headerTitle: {
    fontSize: 20,
    fontFamily: 'Georgia',
    letterSpacing: 1,
    fontWeight: '600',
  },
  headerSub: {fontSize: 10, letterSpacing: 0.5, marginTop: 2},
  totalBadge: {
    borderRadius: 10,
    borderWidth: 1,
    paddingVertical: 6,
    paddingHorizontal: 12,
    alignItems: 'center',
  },
  totalText: {fontSize: 18, fontWeight: '700'},
  totalLabel: {fontSize: 10, letterSpacing: 0.3},
  scrollContent: {padding: H_PAD, gap: 0},
  descBox: {
    borderRadius: 14,
    borderWidth: 1,
    padding: 16,
    marginBottom: 24,
    gap: 14,
  },
  descText: {fontSize: 13, lineHeight: 20, letterSpacing: 0.2},
  stepsRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    gap: 8,
  },
  stepItem: {alignItems: 'center', gap: 6, flex: 1},
  stepCircle: {
    width: 32,
    height: 32,
    borderRadius: 16,
    borderWidth: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  stepNum: {fontSize: 14, fontWeight: '700'},
  stepLabel: {fontSize: 11, letterSpacing: 0.3, textAlign: 'center'},
  sectionLabel: {
    fontSize: 11,
    letterSpacing: 1,
    textTransform: 'uppercase',
    marginBottom: 12,
  },
  grid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: CARD_GAP,
  },
  levelCard: {
    borderRadius: 16,
    borderWidth: 1,
    paddingHorizontal: 16,
    paddingBottom: 16,
    paddingTop: 0,
    overflow: 'hidden',
    shadowOffset: {width: 0, height: 4},
    shadowOpacity: 0.15,
    shadowRadius: 8,
    elevation: 6,
    gap: 6,
  },
  colorBar: {
    height: 4,
    borderRadius: 2,
    marginHorizontal: -16,
    marginBottom: 12,
    marginTop: 0,
  },
  levelEmoji: {fontSize: 28},
  levelLabel: {fontSize: 26, fontFamily: 'Georgia', letterSpacing: 1},
  levelDesc: {fontSize: 11, letterSpacing: 0.3},
  countBadge: {
    alignSelf: 'flex-start',
    borderRadius: 6,
    borderWidth: 1,
    paddingVertical: 3,
    paddingHorizontal: 8,
    marginTop: 2,
  },
  countText: {fontSize: 11, fontWeight: '600', letterSpacing: 0.3},
  arrow: {
    fontSize: 16,
    textAlign: 'right',
    marginTop: 4,
    fontWeight: '600',
  },
});
