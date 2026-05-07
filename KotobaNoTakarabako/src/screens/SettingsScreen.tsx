import React, {useState} from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
  Switch,
  StatusBar,
  Share,
  Alert,
  Modal,
  FlatList,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {useStore} from '../store/useStore';
import {themes} from '../theme/colors';
import type {ThemeKey} from '../theme/colors';
import quotes from '../data/quotes';
import {CATEGORIES} from '../data/quotes';

// ── 시간 선택 모달 ──────────────────────────────────────────────
function TimePickerModal({
  visible,
  hour,
  minute,
  onConfirm,
  onClose,
}: {
  visible: boolean;
  hour: number;
  minute: number;
  onConfirm: (h: number, m: number) => void;
  onClose: () => void;
}) {
  const {theme} = useStore();
  const [selHour, setSelHour] = useState(hour);
  const [selMin, setSelMin] = useState(minute);

  const hours = Array.from({length: 24}, (_, i) => i);
  const minutes = [0, 10, 20, 30, 40, 50];

  return (
    <Modal visible={visible} transparent animationType="fade">
      <View style={styles.modalOverlay}>
        <View
          style={[
            styles.modalBox,
            {backgroundColor: theme.bgCard, borderColor: theme.border},
          ]}>
          <Text style={[styles.modalTitle, {color: theme.accent}]}>
            알림 시간 설정
          </Text>
          <View style={styles.pickerRow}>
            {/* Hour */}
            <View style={styles.pickerCol}>
              <Text style={[styles.pickerLabel, {color: theme.textMuted}]}>
                시
              </Text>
              <ScrollView
                style={[
                  styles.pickerScroll,
                  {borderColor: theme.border, backgroundColor: theme.bgCardAlt},
                ]}
                showsVerticalScrollIndicator={false}>
                {hours.map(h => (
                  <TouchableOpacity
                    key={h}
                    style={[
                      styles.pickerItem,
                      selHour === h && {backgroundColor: theme.accent + '33'},
                    ]}
                    onPress={() => setSelHour(h)}>
                    <Text
                      style={[
                        styles.pickerItemText,
                        {color: selHour === h ? theme.accent : theme.text},
                        selHour === h && {fontWeight: '700'},
                      ]}>
                      {String(h).padStart(2, '0')}
                    </Text>
                  </TouchableOpacity>
                ))}
              </ScrollView>
            </View>

            <Text style={[styles.colonText, {color: theme.text}]}>:</Text>

            {/* Minute */}
            <View style={styles.pickerCol}>
              <Text style={[styles.pickerLabel, {color: theme.textMuted}]}>
                분
              </Text>
              <ScrollView
                style={[
                  styles.pickerScroll,
                  {borderColor: theme.border, backgroundColor: theme.bgCardAlt},
                ]}
                showsVerticalScrollIndicator={false}>
                {minutes.map(m => (
                  <TouchableOpacity
                    key={m}
                    style={[
                      styles.pickerItem,
                      selMin === m && {backgroundColor: theme.accent + '33'},
                    ]}
                    onPress={() => setSelMin(m)}>
                    <Text
                      style={[
                        styles.pickerItemText,
                        {color: selMin === m ? theme.accent : theme.text},
                        selMin === m && {fontWeight: '700'},
                      ]}>
                      {String(m).padStart(2, '0')}
                    </Text>
                  </TouchableOpacity>
                ))}
              </ScrollView>
            </View>
          </View>

          <View style={styles.modalBtns}>
            <TouchableOpacity
              style={[
                styles.modalBtn,
                {borderColor: theme.border, backgroundColor: theme.bgCardAlt},
              ]}
              onPress={onClose}>
              <Text style={[styles.modalBtnText, {color: theme.textMuted}]}>
                취소
              </Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={[
                styles.modalBtn,
                {backgroundColor: theme.accent, borderColor: theme.accent},
              ]}
              onPress={() => {
                onConfirm(selHour, selMin);
                onClose();
              }}>
              <Text style={[styles.modalBtnText, {color: '#0A0A12'}]}>
                확인
              </Text>
            </TouchableOpacity>
          </View>
        </View>
      </View>
    </Modal>
  );
}

// ── 공통 Row 컴포넌트 ───────────────────────────────────────────
function SettingRow({
  icon,
  label,
  sub,
  right,
  onPress,
  borderColor,
}: {
  icon: string;
  label: string;
  sub?: string;
  right?: React.ReactNode;
  onPress?: () => void;
  borderColor: string;
}) {
  const {theme} = useStore();
  const Wrapper = onPress ? TouchableOpacity : View;
  return (
    <Wrapper
      style={[styles.row, {borderBottomColor: borderColor}]}
      onPress={onPress}
      activeOpacity={0.7}>
      <Text style={styles.rowIcon}>{icon}</Text>
      <View style={styles.rowText}>
        <Text style={[styles.rowLabel, {color: theme.text}]}>{label}</Text>
        {sub ? (
          <Text style={[styles.rowSub, {color: theme.textMuted}]}>{sub}</Text>
        ) : null}
      </View>
      {right}
    </Wrapper>
  );
}

// ── 섹션 헤더 ───────────────────────────────────────────────────
function Section({
  title,
  children,
  theme,
}: {
  title: string;
  children: React.ReactNode;
  theme: any;
}) {
  return (
    <View style={styles.section}>
      <Text style={[styles.sectionTitle, {color: theme.textMuted}]}>
        {title}
      </Text>
      <View
        style={[
          styles.sectionCard,
          {backgroundColor: theme.bgCard, borderColor: theme.border},
        ]}>
        {children}
      </View>
    </View>
  );
}

// ── 메인 화면 ───────────────────────────────────────────────────
export default function SettingsScreen() {
  const {
    theme,
    themeKey,
    setTheme,
    fontSize,
    setFontSize,
    autoBlurReveal,
    setAutoBlurReveal,
    notificationEnabled,
    notificationHour,
    notificationMinute,
    setNotification,
    studyStreak,
    totalSeen,
    favorites,
  } = useStore();

  const [timePickerVisible, setTimePickerVisible] = useState(false);

  const THEME_OPTIONS: {key: ThemeKey; emoji: string; label: string}[] = [
    {key: 'goldDark', emoji: '🌑', label: '골드 다크'},
    {key: 'sakuraDark', emoji: '🌸', label: '벚꽃 다크'},
    {key: 'oceanDark', emoji: '🌊', label: '오션 다크'},
    {key: 'washiLight', emoji: '📜', label: '화지 라이트'},
  ];

  const FONT_OPTIONS: {key: 'small' | 'normal' | 'large'; label: string; size: number}[] = [
    {key: 'small', label: '작게', size: 13},
    {key: 'normal', label: '보통', size: 16},
    {key: 'large', label: '크게', size: 20},
  ];

  const handleExportFavorites = async () => {
    if (favorites.length === 0) {
      Alert.alert('즐겨찾기 없음', '저장된 명언이 없습니다.');
      return;
    }
    const favQuotes = quotes.filter(q => favorites.includes(q.id));
    const categoryMap = Object.fromEntries(CATEGORIES.map(c => [c.id, c.label]));

    const text = favQuotes
      .map((q, i) =>
        [
          `${i + 1}. [${categoryMap[q.category] ?? q.category}] ${q.author}`,
          `   ${q.japanese}`,
          `   ${q.reading}`,
          `   ${q.korean}`,
        ].join('\n'),
      )
      .join('\n\n');

    const fullText = `言葉の宝箱 — 즐겨찾기 명언 (${favQuotes.length}개)\n${'─'.repeat(30)}\n\n${text}`;

    try {
      await Share.share({message: fullText, title: '즐겨찾기 명언 내보내기'});
    } catch (e) {
      Alert.alert('오류', '내보내기에 실패했습니다.');
    }
  };

  const handleNotificationToggle = (val: boolean) => {
    setNotification(val, notificationHour, notificationMinute);
    if (val) {
      Alert.alert(
        '알림 설정',
        `매일 ${String(notificationHour).padStart(2, '0')}:${String(notificationMinute).padStart(2, '0')}에 오늘의 명언을 알려드립니다.\n\n실제 푸시 알림은 빌드 후 기기 권한을 허용해야 작동합니다.`,
      );
    }
  };

  return (
    <SafeAreaView
      style={[styles.safeArea, {backgroundColor: theme.bg}]}
      edges={['top', 'left', 'right']}>
      <StatusBar barStyle="light-content" backgroundColor={theme.bg} />

      {/* Header */}
      <View style={[styles.header, {borderBottomColor: theme.border}]}>
        <Text style={[styles.headerTitle, {color: theme.accent}]}>
          ⚙️ 설정
        </Text>
        <Text style={[styles.headerSub, {color: theme.textMuted}]}>
          設定
        </Text>
      </View>

      <ScrollView
        contentContainerStyle={styles.scrollContent}
        showsVerticalScrollIndicator={false}>

        {/* ── 테마 ── */}
        <Section title="🎨  색상 테마" theme={theme}>
          <View style={styles.themeGrid}>
            {THEME_OPTIONS.map(opt => {
              const t = themes[opt.key];
              const isActive = themeKey === opt.key;
              return (
                <TouchableOpacity
                  key={opt.key}
                  style={[
                    styles.themeCard,
                    {
                      backgroundColor: t.bg,
                      borderColor: isActive ? t.accent : t.border,
                      borderWidth: isActive ? 2 : 1,
                    },
                  ]}
                  onPress={() => setTheme(opt.key)}
                  activeOpacity={0.8}>
                  {/* Preview swatch */}
                  <View
                    style={[styles.themeSwatch, {backgroundColor: t.accent}]}
                  />
                  <Text style={[styles.themeEmoji]}>{opt.emoji}</Text>
                  <Text style={[styles.themeLabel, {color: t.text}]}>
                    {opt.label}
                  </Text>
                  {isActive && (
                    <View
                      style={[
                        styles.themeCheck,
                        {backgroundColor: t.accent},
                      ]}>
                      <Text style={styles.themeCheckIcon}>✓</Text>
                    </View>
                  )}
                </TouchableOpacity>
              );
            })}
          </View>
        </Section>

        {/* ── 글자 크기 ── */}
        <Section title="🔡  글자 크기" theme={theme}>
          <View style={styles.fontRow}>
            {FONT_OPTIONS.map(opt => {
              const isActive = fontSize === opt.key;
              return (
                <TouchableOpacity
                  key={opt.key}
                  style={[
                    styles.fontBtn,
                    {
                      backgroundColor: isActive
                        ? theme.accent + '22'
                        : theme.bgCardAlt,
                      borderColor: isActive ? theme.accent : theme.border,
                      flex: 1,
                    },
                  ]}
                  onPress={() => setFontSize(opt.key)}
                  activeOpacity={0.75}>
                  <Text
                    style={[
                      styles.fontBtnKanji,
                      {
                        color: isActive ? theme.accent : theme.textMuted,
                        fontSize: opt.size,
                      },
                    ]}>
                    文
                  </Text>
                  <Text
                    style={[
                      styles.fontBtnLabel,
                      {color: isActive ? theme.accent : theme.textMuted},
                    ]}>
                    {opt.label}
                  </Text>
                </TouchableOpacity>
              );
            })}
          </View>
          {/* Preview */}
          <View
            style={[
              styles.fontPreview,
              {backgroundColor: theme.bgCardAlt, borderColor: theme.border},
            ]}>
            <Text
              style={[
                styles.fontPreviewText,
                {
                  color: theme.text,
                  fontSize:
                    fontSize === 'small' ? 18 : fontSize === 'large' ? 26 : 22,
                },
              ]}>
              努力は必ず報われる。
            </Text>
            <Text style={[styles.fontPreviewSub, {color: theme.textMuted}]}>
              미리보기
            </Text>
          </View>
        </Section>

        {/* ── 알림 ── */}
        <Section title="🔔  오늘의 명언 알림" theme={theme}>
          <SettingRow
            icon="🔔"
            label="매일 알림"
            sub="지정 시간에 오늘의 명언 수신"
            borderColor={theme.border}
            right={
              <Switch
                value={notificationEnabled}
                onValueChange={handleNotificationToggle}
                trackColor={{false: theme.border, true: theme.accentSoft}}
                thumbColor={notificationEnabled ? theme.accent : theme.textMuted}
              />
            }
          />
          <SettingRow
            icon="🕐"
            label="알림 시간"
            sub={`${String(notificationHour).padStart(2, '0')}:${String(notificationMinute).padStart(2, '0')}`}
            borderColor="transparent"
            onPress={() => setTimePickerVisible(true)}
            right={
              <Text style={[styles.chevron, {color: theme.accentSoft}]}>›</Text>
            }
          />
        </Section>

        {/* ── 블러 자동 해제 ── */}
        <Section title="👁  블러 설정" theme={theme}>
          <SettingRow
            icon="👁"
            label="블러 자동 해제"
            sub="5초 후 읽기·한국어 블러 자동 해제"
            borderColor="transparent"
            right={
              <Switch
                value={autoBlurReveal}
                onValueChange={setAutoBlurReveal}
                trackColor={{false: theme.border, true: theme.accentSoft}}
                thumbColor={autoBlurReveal ? theme.accent : theme.textMuted}
              />
            }
          />
        </Section>

        {/* ── 학습 통계 ── */}
        <Section title="🔥  학습 통계" theme={theme}>
          <View style={styles.statsGrid}>
            {[
              {icon: '🔥', value: studyStreak, label: '연속 학습일', unit: '일'},
              {icon: '📖', value: totalSeen, label: '누적 본 명언', unit: '개'},
              {icon: '♥', value: favorites.length, label: '즐겨찾기', unit: '개'},
              {icon: '📚', value: 90, label: '전체 명언', unit: '개'},
            ].map(stat => (
              <View
                key={stat.label}
                style={[
                  styles.statCard,
                  {backgroundColor: theme.bgCardAlt, borderColor: theme.border},
                ]}>
                <Text style={styles.statIcon}>{stat.icon}</Text>
                <Text style={[styles.statValue, {color: theme.accent}]}>
                  {stat.value}
                </Text>
                <Text style={[styles.statUnit, {color: theme.textMuted}]}>
                  {stat.unit}
                </Text>
                <Text style={[styles.statLabel, {color: theme.textMuted}]}>
                  {stat.label}
                </Text>
              </View>
            ))}
          </View>
        </Section>

        {/* ── 내보내기 ── */}
        <Section title="💾  즐겨찾기 내보내기" theme={theme}>
          <View style={styles.exportInner}>
            <Text style={[styles.exportDesc, {color: theme.textMuted}]}>
              저장한 명언 {favorites.length}개를 텍스트로 내보냅니다.{'\n'}
              메시지·메모·파일 등 원하는 곳에 공유할 수 있습니다.
            </Text>
            <TouchableOpacity
              style={[
                styles.exportBtn,
                {
                  backgroundColor:
                    favorites.length > 0 ? theme.accent : theme.bgCardAlt,
                  borderColor:
                    favorites.length > 0 ? theme.accent : theme.border,
                },
              ]}
              onPress={handleExportFavorites}
              activeOpacity={0.8}>
              <Text
                style={[
                  styles.exportBtnText,
                  {
                    color:
                      favorites.length > 0 ? '#0A0A12' : theme.textMuted,
                  },
                ]}>
                💾 내보내기{favorites.length > 0 ? ` (${favorites.length}개)` : ''}
              </Text>
            </TouchableOpacity>
          </View>
        </Section>

        {/* ── 앱 정보 ── */}
        <Section title="ℹ️  앱 정보" theme={theme}>
          <SettingRow
            icon="🈷️"
            label="言葉の宝箱"
            sub="코토바노 타카라바코 v1.0.0"
            borderColor={theme.border}
          />
          <SettingRow
            icon="📝"
            label="명언 수"
            sub="6개 카테고리 × 15개 = 90개"
            borderColor={theme.border}
          />
          <SettingRow
            icon="📖"
            label="사전 단어 수"
            sub="45개 (명언 핵심 어휘 + JLPT N2~N5)"
            borderColor="transparent"
          />
        </Section>

        <View style={{height: 40}} />
      </ScrollView>

      {/* 시간 선택 모달 */}
      <TimePickerModal
        visible={timePickerVisible}
        hour={notificationHour}
        minute={notificationMinute}
        onConfirm={(h, m) => setNotification(notificationEnabled, h, m)}
        onClose={() => setTimePickerVisible(false)}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {flex: 1},
  header: {
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
  scrollContent: {padding: 16, gap: 0},

  // Section
  section: {marginBottom: 24},
  sectionTitle: {
    fontSize: 11,
    letterSpacing: 1,
    textTransform: 'uppercase',
    marginBottom: 8,
    paddingLeft: 4,
  },
  sectionCard: {
    borderRadius: 14,
    borderWidth: 1,
    overflow: 'hidden',
  },

  // Row
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 14,
    paddingHorizontal: 16,
    borderBottomWidth: StyleSheet.hairlineWidth,
    gap: 12,
  },
  rowIcon: {fontSize: 18},
  rowText: {flex: 1},
  rowLabel: {fontSize: 15, letterSpacing: 0.2},
  rowSub: {fontSize: 12, marginTop: 2, letterSpacing: 0.2},
  chevron: {fontSize: 22, fontWeight: '300'},

  // Theme grid
  themeGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    padding: 12,
    gap: 10,
  },
  themeCard: {
    width: '47%',
    borderRadius: 12,
    padding: 14,
    alignItems: 'center',
    gap: 6,
    position: 'relative',
    overflow: 'hidden',
  },
  themeSwatch: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    height: 4,
    borderRadius: 2,
  },
  themeEmoji: {fontSize: 22, marginTop: 6},
  themeLabel: {fontSize: 12, letterSpacing: 0.3, fontWeight: '600'},
  themeCheck: {
    position: 'absolute',
    top: 8,
    right: 8,
    width: 18,
    height: 18,
    borderRadius: 9,
    alignItems: 'center',
    justifyContent: 'center',
  },
  themeCheckIcon: {color: '#0A0A12', fontSize: 11, fontWeight: '700'},

  // Font size
  fontRow: {flexDirection: 'row', gap: 8, padding: 14, paddingBottom: 8},
  fontBtn: {
    borderRadius: 10,
    borderWidth: 1,
    paddingVertical: 12,
    alignItems: 'center',
    gap: 4,
  },
  fontBtnKanji: {fontFamily: 'Georgia', fontWeight: '600'},
  fontBtnLabel: {fontSize: 11, letterSpacing: 0.3},
  fontPreview: {
    margin: 14,
    marginTop: 4,
    borderRadius: 10,
    borderWidth: 1,
    padding: 14,
    alignItems: 'center',
    gap: 4,
  },
  fontPreviewText: {fontFamily: 'Georgia', letterSpacing: 1, textAlign: 'center'},
  fontPreviewSub: {fontSize: 10, letterSpacing: 0.5},

  // Stats
  statsGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 10,
    padding: 14,
  },
  statCard: {
    width: '47%',
    borderRadius: 12,
    borderWidth: 1,
    padding: 14,
    alignItems: 'center',
    gap: 2,
  },
  statIcon: {fontSize: 20, marginBottom: 4},
  statValue: {fontSize: 28, fontWeight: '700', letterSpacing: 1},
  statUnit: {fontSize: 11, letterSpacing: 0.3},
  statLabel: {fontSize: 11, letterSpacing: 0.3, marginTop: 2},

  // Export
  exportInner: {padding: 16, gap: 14},
  exportDesc: {fontSize: 13, lineHeight: 20, letterSpacing: 0.2},
  exportBtn: {
    borderRadius: 10,
    borderWidth: 1,
    paddingVertical: 14,
    alignItems: 'center',
  },
  exportBtnText: {fontSize: 14, fontWeight: '700', letterSpacing: 0.5},

  // Time picker modal
  modalOverlay: {
    flex: 1,
    backgroundColor: '#00000088',
    alignItems: 'center',
    justifyContent: 'center',
    padding: 24,
  },
  modalBox: {
    width: '100%',
    maxWidth: 320,
    borderRadius: 20,
    borderWidth: 1,
    padding: 24,
    gap: 16,
  },
  modalTitle: {
    fontSize: 17,
    fontFamily: 'Georgia',
    letterSpacing: 0.5,
    textAlign: 'center',
    fontWeight: '600',
  },
  pickerRow: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'center',
    gap: 8,
  },
  pickerCol: {alignItems: 'center', gap: 8},
  pickerLabel: {fontSize: 12, letterSpacing: 0.5},
  pickerScroll: {
    height: 180,
    width: 72,
    borderRadius: 10,
    borderWidth: 1,
  },
  pickerItem: {
    paddingVertical: 10,
    alignItems: 'center',
    borderRadius: 8,
    marginHorizontal: 4,
    marginVertical: 2,
  },
  pickerItemText: {fontSize: 18, letterSpacing: 1, fontFamily: 'Georgia'},
  colonText: {fontSize: 28, fontWeight: '200', marginTop: 20},
  modalBtns: {flexDirection: 'row', gap: 10},
  modalBtn: {
    flex: 1,
    borderRadius: 10,
    borderWidth: 1,
    paddingVertical: 12,
    alignItems: 'center',
  },
  modalBtnText: {fontSize: 14, fontWeight: '600', letterSpacing: 0.3},
});
