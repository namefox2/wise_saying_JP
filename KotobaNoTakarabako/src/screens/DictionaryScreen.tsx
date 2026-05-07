import React, {useState, useRef, useCallback} from 'react';
import {
  View,
  Text,
  StyleSheet,
  TextInput,
  FlatList,
  TouchableOpacity,
  Animated,
  StatusBar,
  Keyboard,
  Linking,
  Alert,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {useStore} from '../store/useStore';
import {searchDictionary} from '../data/dictionary';
import type {DictEntry} from '../data/dictionary';
import FuriganaText from '../components/FuriganaText';

const POS_COLORS: Record<string, string> = {
  명사: '#5B9BD5',
  동사: '#70AD47',
  형용사: '#ED7D31',
  'な형용사': '#FFC000',
  부사: '#A061C5',
  관용구: '#C00000',
  사자성어: '#C8A96E',
};

const JISHO_URL = (word: string) =>
  `https://jisho.org/search/${encodeURIComponent(word)}`;

const WEBLIO_URL = (word: string) =>
  `https://www.weblio.jp/content/${encodeURIComponent(word)}`;

async function openDictionary(word: string) {
  const url = JISHO_URL(word);
  const supported = await Linking.canOpenURL(url);
  if (supported) {
    await Linking.openURL(url);
  } else {
    Alert.alert('오류', '브라우저를 열 수 없습니다.');
  }
}

function DictCard({entry}: {entry: DictEntry}) {
  const {theme} = useStore();
  const [exampleExpanded, setExampleExpanded] = useState(false);
  const rotateAnim = useRef(new Animated.Value(0)).current;

  const toggleExample = () => {
    Animated.timing(rotateAnim, {
      toValue: exampleExpanded ? 0 : 1,
      duration: 200,
      useNativeDriver: true,
    }).start();
    setExampleExpanded(p => !p);
  };

  const arrowRotation = rotateAnim.interpolate({
    inputRange: [0, 1],
    outputRange: ['0deg', '180deg'],
  });

  const posColor = POS_COLORS[entry.pos] ?? theme.accentSoft;

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
      {/* ── Word block: furigana + meta ── */}
      <View style={styles.wordRow}>
        {/* Furigana (読み仮名) — yomigana above kanji */}
        <View style={styles.furiganaBlock}>
          <FuriganaText
            word={entry.word}
            reading={entry.reading}
            wordFontSize={28}
            wordColor={theme.text}
            readingColor={theme.accentSoft}
          />
          <Text style={[styles.romajiText, {color: theme.textMuted}]}>
            {entry.romaji}
          </Text>
        </View>

        {/* Right: pos badge + web link button */}
        <View style={styles.metaBlock}>
          <View
            style={[
              styles.posBadge,
              {
                backgroundColor: posColor + '22',
                borderColor: posColor,
              },
            ]}>
            <Text style={[styles.posText, {color: posColor}]}>{entry.pos}</Text>
          </View>

          {/* 웹사전 열기 버튼 */}
          <TouchableOpacity
            style={[
              styles.webBtn,
              {
                backgroundColor: theme.bgCardAlt,
                borderColor: theme.accentSoft,
              },
            ]}
            onPress={() => openDictionary(entry.word)}
            activeOpacity={0.75}>
            <Text style={[styles.webBtnIcon, {color: theme.accentSoft}]}>
              🌐
            </Text>
            <Text style={[styles.webBtnText, {color: theme.accentSoft}]}>
              Jisho
            </Text>
          </TouchableOpacity>
        </View>
      </View>

      {/* ── Meanings ── */}
      <View style={styles.meaningsRow}>
        {entry.meanings.map((m, i) => (
          <View
            key={i}
            style={[
              styles.meaningChip,
              {backgroundColor: theme.bgCardAlt, borderColor: theme.border},
            ]}>
            <Text style={[styles.meaningText, {color: theme.text}]}>{m}</Text>
          </View>
        ))}
      </View>

      {/* ── Tags ── */}
      {entry.tags && entry.tags.length > 0 && (
        <View style={styles.tagsRow}>
          {entry.tags.map(tag => (
            <Text key={tag} style={[styles.tagText, {color: theme.textMuted}]}>
              #{tag}
            </Text>
          ))}
        </View>
      )}

      {/* ── Example toggle ── */}
      {entry.examples.length > 0 && (
        <>
          <TouchableOpacity
            style={[styles.exampleToggle, {borderTopColor: theme.border}]}
            onPress={toggleExample}
            activeOpacity={0.75}>
            <Text style={[styles.exampleToggleLabel, {color: theme.textMuted}]}>
              예문 보기
            </Text>
            <Animated.Text
              style={[
                styles.exampleArrow,
                {
                  color: theme.accentSoft,
                  transform: [{rotate: arrowRotation}],
                },
              ]}>
              ▼
            </Animated.Text>
          </TouchableOpacity>

          {exampleExpanded &&
            entry.examples.map((ex, i) => (
              <View
                key={i}
                style={[
                  styles.exampleBlock,
                  {
                    backgroundColor: theme.bgCardAlt,
                    borderColor: theme.border,
                  },
                ]}>
                <Text style={[styles.exampleJP, {color: theme.text}]}>
                  {ex.japanese}
                </Text>
                <Text style={[styles.exampleReading, {color: theme.textSub}]}>
                  {ex.reading}
                </Text>
                <Text style={[styles.exampleKO, {color: theme.textMuted}]}>
                  {ex.korean}
                </Text>
              </View>
            ))}
        </>
      )}

      {/* ── 외부 사전 링크 줄 ── */}
      <View style={[styles.linksRow, {borderTopColor: theme.border}]}>
        <Text style={[styles.linksLabel, {color: theme.textMuted}]}>
          더 찾아보기:
        </Text>
        <TouchableOpacity
          onPress={() => openDictionary(entry.word)}
          activeOpacity={0.7}>
          <Text style={[styles.linkText, {color: theme.accent}]}>
            Jisho.org ↗
          </Text>
        </TouchableOpacity>
        <TouchableOpacity
          onPress={() => Linking.openURL(WEBLIO_URL(entry.word))}
          activeOpacity={0.7}>
          <Text style={[styles.linkText, {color: theme.accent}]}>
            Weblio ↗
          </Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

interface DictionaryScreenProps {
  navigation: any;
}

export default function DictionaryScreen({navigation}: DictionaryScreenProps) {
  const {theme} = useStore();
  const [query, setQuery] = useState('');
  const [results, setResults] = useState<DictEntry[]>([]);
  const [searched, setSearched] = useState(false);
  const [recentSearches, setRecentSearches] = useState<string[]>([]);
  const inputRef = useRef<TextInput>(null);
  const fadeAnim = useRef(new Animated.Value(0)).current;

  const QUICK_SEARCHES = ['努力', '愛', '心', '夢', '勉強', '幸せ', '継続', '感謝'];

  const doSearch = useCallback(
    (q: string) => {
      if (!q.trim()) return;
      Keyboard.dismiss();
      const found = searchDictionary(q.trim());
      setResults(found);
      setSearched(true);
      setRecentSearches(prev =>
        [q.trim(), ...prev.filter(r => r !== q.trim())].slice(0, 8),
      );
      fadeAnim.setValue(0);
      Animated.timing(fadeAnim, {
        toValue: 1,
        duration: 300,
        useNativeDriver: true,
      }).start();
    },
    [fadeAnim],
  );

  const clearSearch = () => {
    setQuery('');
    setResults([]);
    setSearched(false);
    inputRef.current?.focus();
  };

  return (
    <SafeAreaView
      style={[styles.safeArea, {backgroundColor: theme.bg}]}
      edges={['top', 'left', 'right']}>
      <StatusBar barStyle="light-content" backgroundColor={theme.bg} />

      {/* Header */}
      <View style={[styles.header, {borderBottomColor: theme.border}]}>
        <View>
          <Text style={[styles.headerTitle, {color: theme.accent}]}>
            🔍 사전 검색
          </Text>
          <Text style={[styles.headerSub, {color: theme.textMuted}]}>
            辞書検索
          </Text>
        </View>
      </View>

      {/* Search bar */}
      <View style={[styles.searchBarWrapper, {backgroundColor: theme.bg}]}>
        <View
          style={[
            styles.searchBar,
            {
              backgroundColor: theme.bgCard,
              borderColor: query ? theme.accent : theme.border,
            },
          ]}>
          <Text style={[styles.searchIcon, {color: theme.textMuted}]}>🔍</Text>
          <TextInput
            ref={inputRef}
            style={[styles.searchInput, {color: theme.text}]}
            placeholder="한자·히라가나·로마자·한국어로 검색"
            placeholderTextColor={theme.textMuted}
            value={query}
            onChangeText={setQuery}
            onSubmitEditing={() => doSearch(query)}
            returnKeyType="search"
            autoCapitalize="none"
            autoCorrect={false}
          />
          {query.length > 0 && (
            <TouchableOpacity onPress={clearSearch} hitSlop={10}>
              <Text style={[styles.clearIcon, {color: theme.textMuted}]}>✕</Text>
            </TouchableOpacity>
          )}
        </View>
        <TouchableOpacity
          style={[styles.searchBtn, {backgroundColor: theme.accent}]}
          onPress={() => doSearch(query)}
          activeOpacity={0.8}>
          <Text style={styles.searchBtnText}>검색</Text>
        </TouchableOpacity>
      </View>

      <FlatList
        data={results}
        keyExtractor={item => item.id}
        keyboardShouldPersistTaps="handled"
        showsVerticalScrollIndicator={false}
        contentContainerStyle={styles.listContent}
        renderItem={({item}) => (
          <Animated.View style={{opacity: fadeAnim}}>
            <DictCard entry={item} />
          </Animated.View>
        )}
        ItemSeparatorComponent={() => <View style={{height: 12}} />}
        ListHeaderComponent={
          !searched ? (
            <View>
              <Text style={[styles.sectionLabel, {color: theme.textMuted}]}>
                자주 찾는 단어
              </Text>
              <View style={styles.chipsRow}>
                {QUICK_SEARCHES.map(w => (
                  <TouchableOpacity
                    key={w}
                    style={[
                      styles.chip,
                      {
                        backgroundColor: theme.bgCardAlt,
                        borderColor: theme.accentSoft,
                      },
                    ]}
                    onPress={() => {
                      setQuery(w);
                      doSearch(w);
                    }}>
                    <Text style={[styles.chipText, {color: theme.accent}]}>
                      {w}
                    </Text>
                  </TouchableOpacity>
                ))}
              </View>

              {recentSearches.length > 0 && (
                <>
                  <Text
                    style={[
                      styles.sectionLabel,
                      {color: theme.textMuted, marginTop: 20},
                    ]}>
                    최근 검색
                  </Text>
                  {recentSearches.map(r => (
                    <TouchableOpacity
                      key={r}
                      style={[
                        styles.recentItem,
                        {borderBottomColor: theme.border},
                      ]}
                      onPress={() => {
                        setQuery(r);
                        doSearch(r);
                      }}>
                      <Text
                        style={[styles.recentIcon, {color: theme.textMuted}]}>
                        🕐
                      </Text>
                      <Text style={[styles.recentText, {color: theme.text}]}>
                        {r}
                      </Text>
                      <Text
                        style={[
                          styles.recentArrow,
                          {color: theme.textMuted},
                        ]}>
                        →
                      </Text>
                    </TouchableOpacity>
                  ))}
                </>
              )}

              <View
                style={[
                  styles.tipBox,
                  {
                    backgroundColor: theme.bgCardAlt,
                    borderColor: theme.border,
                  },
                ]}>
                <Text style={[styles.tipTitle, {color: theme.accentSoft}]}>
                  💡 검색 팁
                </Text>
                <Text style={[styles.tipText, {color: theme.textMuted}]}>
                  • 한자: 努力, 愛, 心{'\n'}
                  • 히라가나: どりょく, あい{'\n'}
                  • 로마자: doryoku, ai{'\n'}
                  • 한국어: 노력, 사랑, 마음{'\n'}
                  • 단어 카드의 Jisho 버튼으로 웹사전 이동
                </Text>
              </View>
            </View>
          ) : results.length > 0 ? (
            <Text style={[styles.resultCount, {color: theme.textMuted}]}>
              「{query}」— {results.length}개 검색됨
            </Text>
          ) : null
        }
        ListEmptyComponent={
          searched ? (
            <View style={styles.emptyContainer}>
              <Text style={styles.emptyEmoji}>🔍</Text>
              <Text style={[styles.emptyTitle, {color: theme.text}]}>
                검색 결과가 없어요
              </Text>
              <Text style={[styles.emptyDesc, {color: theme.textMuted}]}>
                다른 단어나 히라가나로{'\n'}다시 검색해 보세요.
              </Text>
              <TouchableOpacity
                style={[
                  styles.webSearchBtn,
                  {
                    backgroundColor: theme.bgCardAlt,
                    borderColor: theme.accentSoft,
                  },
                ]}
                onPress={() => query && openDictionary(query)}
                activeOpacity={0.8}>
                <Text style={[styles.webSearchBtnText, {color: theme.accent}]}>
                  🌐 Jisho.org에서 직접 검색 ↗
                </Text>
              </TouchableOpacity>
            </View>
          ) : null
        }
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
  headerSub: {
    fontSize: 10,
    letterSpacing: 0.5,
    marginTop: 2,
  },
  searchBarWrapper: {
    flexDirection: 'row',
    paddingHorizontal: 16,
    paddingVertical: 12,
    gap: 8,
    alignItems: 'center',
  },
  searchBar: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    borderRadius: 12,
    borderWidth: 1,
    paddingHorizontal: 12,
    paddingVertical: 10,
    gap: 8,
  },
  searchIcon: {fontSize: 14},
  searchInput: {
    flex: 1,
    fontSize: 15,
    letterSpacing: 0.3,
    paddingVertical: 0,
  },
  clearIcon: {fontSize: 13, paddingHorizontal: 4},
  searchBtn: {
    borderRadius: 10,
    paddingVertical: 11,
    paddingHorizontal: 16,
    alignItems: 'center',
    justifyContent: 'center',
  },
  searchBtnText: {
    color: '#0A0A12',
    fontSize: 13,
    fontWeight: '700',
    letterSpacing: 0.5,
  },
  listContent: {
    paddingHorizontal: 16,
    paddingBottom: 32,
  },
  sectionLabel: {
    fontSize: 11,
    letterSpacing: 1,
    textTransform: 'uppercase',
    marginBottom: 10,
    marginTop: 4,
  },
  chipsRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 8,
  },
  chip: {
    borderRadius: 20,
    borderWidth: 1,
    paddingVertical: 6,
    paddingHorizontal: 14,
  },
  chipText: {
    fontSize: 14,
    fontFamily: 'Georgia',
    letterSpacing: 0.5,
  },
  recentItem: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 12,
    borderBottomWidth: StyleSheet.hairlineWidth,
    gap: 10,
  },
  recentIcon: {fontSize: 13},
  recentText: {flex: 1, fontSize: 14, letterSpacing: 0.3},
  recentArrow: {fontSize: 12},
  tipBox: {
    borderRadius: 12,
    borderWidth: 1,
    padding: 16,
    marginTop: 20,
    gap: 8,
  },
  tipTitle: {fontSize: 13, fontWeight: '600', letterSpacing: 0.3},
  tipText: {fontSize: 13, lineHeight: 22, letterSpacing: 0.2},
  resultCount: {
    fontSize: 12,
    letterSpacing: 0.3,
    marginBottom: 12,
    marginTop: 4,
  },
  // ── DictCard ──────────────────────────────────────────────────
  card: {
    borderRadius: 16,
    borderWidth: 1,
    padding: 18,
    shadowOffset: {width: 0, height: 3},
    shadowOpacity: 0.12,
    shadowRadius: 8,
    elevation: 5,
  },
  wordRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'flex-start',
    marginBottom: 14,
  },
  furiganaBlock: {
    flex: 1,
    gap: 4,
  },
  romajiText: {
    fontSize: 11,
    letterSpacing: 0.4,
    fontStyle: 'italic',
    marginTop: 2,
  },
  metaBlock: {
    alignItems: 'flex-end',
    gap: 8,
    paddingLeft: 12,
  },
  posBadge: {
    borderRadius: 6,
    borderWidth: 1,
    paddingVertical: 3,
    paddingHorizontal: 8,
  },
  posText: {fontSize: 11, fontWeight: '600', letterSpacing: 0.3},
  webBtn: {
    flexDirection: 'row',
    alignItems: 'center',
    borderRadius: 8,
    borderWidth: 1,
    paddingVertical: 5,
    paddingHorizontal: 9,
    gap: 4,
  },
  webBtnIcon: {fontSize: 11},
  webBtnText: {fontSize: 11, fontWeight: '600', letterSpacing: 0.3},
  meaningsRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 6,
    marginBottom: 10,
  },
  meaningChip: {
    borderRadius: 6,
    borderWidth: 1,
    paddingVertical: 4,
    paddingHorizontal: 10,
  },
  meaningText: {fontSize: 13, letterSpacing: 0.2},
  tagsRow: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: 6,
    marginBottom: 6,
  },
  tagText: {fontSize: 10, letterSpacing: 0.3},
  exampleToggle: {
    flexDirection: 'row',
    alignItems: 'center',
    justifyContent: 'space-between',
    paddingTop: 12,
    marginTop: 6,
    borderTopWidth: StyleSheet.hairlineWidth,
  },
  exampleToggleLabel: {fontSize: 12, letterSpacing: 0.3},
  exampleArrow: {fontSize: 10},
  exampleBlock: {
    borderRadius: 10,
    borderWidth: 1,
    padding: 14,
    marginTop: 10,
    gap: 4,
  },
  exampleJP: {
    fontSize: 15,
    fontFamily: 'Georgia',
    letterSpacing: 1,
    lineHeight: 26,
  },
  exampleReading: {fontSize: 12, letterSpacing: 0.4, fontStyle: 'italic'},
  exampleKO: {
    fontSize: 13,
    lineHeight: 20,
    letterSpacing: 0.2,
    marginTop: 2,
  },
  linksRow: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 12,
    paddingTop: 12,
    marginTop: 10,
    borderTopWidth: StyleSheet.hairlineWidth,
    flexWrap: 'wrap',
  },
  linksLabel: {fontSize: 11, letterSpacing: 0.3},
  linkText: {
    fontSize: 12,
    fontWeight: '600',
    letterSpacing: 0.3,
    textDecorationLine: 'underline',
  },
  // ── Empty state ───────────────────────────────────────────────
  emptyContainer: {
    alignItems: 'center',
    paddingTop: 48,
    gap: 10,
  },
  emptyEmoji: {fontSize: 40, marginBottom: 8},
  emptyTitle: {
    fontSize: 17,
    fontFamily: 'Georgia',
    letterSpacing: 0.5,
  },
  emptyDesc: {
    fontSize: 14,
    lineHeight: 22,
    textAlign: 'center',
  },
  webSearchBtn: {
    marginTop: 16,
    borderRadius: 10,
    borderWidth: 1,
    paddingVertical: 12,
    paddingHorizontal: 20,
  },
  webSearchBtnText: {
    fontSize: 14,
    fontWeight: '600',
    letterSpacing: 0.3,
  },
});
