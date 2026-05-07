import React, {useRef, useState} from 'react';
import {
  View,
  Text,
  StyleSheet,
  FlatList,
  TouchableOpacity,
  Animated,
  StatusBar,
  Alert,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {useStore} from '../store/useStore';
import quotes from '../data/quotes';
import {CATEGORIES} from '../data/quotes';
import type {Quote} from '../data/quotes';

function FavoriteItem({
  item,
  onPress,
  onRemove,
}: {
  item: Quote;
  onPress: () => void;
  onRemove: () => void;
}) {
  const {theme} = useStore();
  const slideX = useRef(new Animated.Value(0)).current;
  const opacity = useRef(new Animated.Value(1)).current;
  const [swiped, setSwiped] = useState(false);

  const categoryInfo = CATEGORIES.find(c => c.id === item.category);

  const handleRemove = () => {
    Alert.alert('즐겨찾기 삭제', '이 명언을 즐겨찾기에서 삭제할까요?', [
      {text: '취소', style: 'cancel'},
      {
        text: '삭제',
        style: 'destructive',
        onPress: () => {
          Animated.parallel([
            Animated.timing(slideX, {
              toValue: 400,
              duration: 250,
              useNativeDriver: true,
            }),
            Animated.timing(opacity, {
              toValue: 0,
              duration: 250,
              useNativeDriver: true,
            }),
          ]).start(() => onRemove());
        },
      },
    ]);
  };

  return (
    <Animated.View
      style={[
        styles.itemWrapper,
        {transform: [{translateX: slideX}], opacity},
      ]}>
      <TouchableOpacity
        style={[
          styles.card,
          {
            backgroundColor: theme.bgCard,
            borderColor: theme.border,
            shadowColor: theme.accent,
          },
        ]}
        onPress={onPress}
        activeOpacity={0.8}>
        {/* Category badge */}
        <View style={styles.cardHeader}>
          <View
            style={[
              styles.categoryBadge,
              {
                backgroundColor: theme.bgCardAlt,
                borderColor: theme.accentSoft,
              },
            ]}>
            <Text style={styles.categoryEmoji}>{categoryInfo?.emoji}</Text>
            <Text style={[styles.categoryText, {color: theme.accentSoft}]}>
              {categoryInfo?.kanji}
            </Text>
          </View>
          <TouchableOpacity onPress={handleRemove} hitSlop={12}>
            <Text style={[styles.removeIcon, {color: theme.textMuted}]}>✕</Text>
          </TouchableOpacity>
        </View>

        {/* Quote */}
        <Text
          style={[styles.quoteText, {color: theme.text}]}
          numberOfLines={3}>
          {item.japanese}
        </Text>

        {/* Author */}
        <Text style={[styles.author, {color: theme.accentSoft}]}>
          — {item.author}
        </Text>

        {/* Korean preview */}
        <Text
          style={[styles.koreanPreview, {color: theme.textMuted}]}
          numberOfLines={2}>
          {item.korean}
        </Text>

        {/* Tap hint */}
        <View style={styles.cardFooter}>
          <Text style={[styles.tapHint, {color: theme.textMuted}]}>
            탭하여 카드로 보기 →
          </Text>
          <Text style={[styles.heartFilled, {color: '#E8507A'}]}>♥</Text>
        </View>
      </TouchableOpacity>
    </Animated.View>
  );
}

interface FavoritesScreenProps {
  navigation: any;
}

export default function FavoritesScreen({navigation}: FavoritesScreenProps) {
  const {theme, favorites, toggleFavorite} = useStore();

  const favoriteQuotes = quotes.filter(q => favorites.includes(q.id));

  const handlePressItem = (quote: Quote) => {
    navigation.navigate('QuoteCard', {
      category: quote.category,
      startIndex: 0,
      highlightId: quote.id,
    });
  };

  const renderEmpty = () => (
    <View style={styles.emptyContainer}>
      <Text style={styles.emptyEmoji}>♡</Text>
      <Text style={[styles.emptyTitle, {color: theme.text}]}>
        즐겨찾기가 비어 있어요
      </Text>
      <Text style={[styles.emptyDesc, {color: theme.textMuted}]}>
        명언 카드의 하트(♡)를 탭하면{'\n'}여기에 저장됩니다.
      </Text>
      <TouchableOpacity
        style={[
          styles.goExploreBtn,
          {backgroundColor: theme.bgCardAlt, borderColor: theme.accentSoft},
        ]}
        onPress={() => navigation.navigate('홈')}>
        <Text style={[styles.goExploreBtnText, {color: theme.accent}]}>
          명언 둘러보기 →
        </Text>
      </TouchableOpacity>
    </View>
  );

  return (
    <SafeAreaView
      style={[styles.safeArea, {backgroundColor: theme.bg}]}
      edges={['top', 'left', 'right']}>
      <StatusBar barStyle="light-content" backgroundColor={theme.bg} />

      {/* Header */}
      <View
        style={[styles.header, {borderBottomColor: theme.border}]}>
        <View>
          <Text style={[styles.headerTitle, {color: theme.accent}]}>
            ♥ 즐겨찾기
          </Text>
          <Text style={[styles.headerSub, {color: theme.textMuted}]}>
            お気に入り
          </Text>
        </View>
        {favoriteQuotes.length > 0 && (
          <View
            style={[
              styles.countBadge,
              {backgroundColor: theme.bgCardAlt, borderColor: theme.border},
            ]}>
            <Text style={[styles.countText, {color: theme.accent}]}>
              {favoriteQuotes.length}개
            </Text>
          </View>
        )}
      </View>

      <FlatList
        data={favoriteQuotes}
        keyExtractor={item => item.id}
        renderItem={({item}) => (
          <FavoriteItem
            item={item}
            onPress={() => handlePressItem(item)}
            onRemove={() => toggleFavorite(item.id)}
          />
        )}
        ListEmptyComponent={renderEmpty}
        contentContainerStyle={[
          styles.listContent,
          favoriteQuotes.length === 0 && styles.listContentEmpty,
        ]}
        showsVerticalScrollIndicator={false}
        ItemSeparatorComponent={() => <View style={{height: 12}} />}
      />
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
  headerSub: {
    fontSize: 10,
    letterSpacing: 0.5,
    marginTop: 2,
  },
  countBadge: {
    borderRadius: 10,
    borderWidth: 1,
    paddingVertical: 6,
    paddingHorizontal: 12,
  },
  countText: {
    fontSize: 13,
    fontWeight: '600',
    letterSpacing: 0.3,
  },
  listContent: {
    padding: 16,
  },
  listContentEmpty: {
    flex: 1,
  },
  itemWrapper: {
    width: '100%',
  },
  card: {
    borderRadius: 16,
    borderWidth: 1,
    padding: 18,
    shadowOffset: {width: 0, height: 3},
    shadowOpacity: 0.12,
    shadowRadius: 8,
    elevation: 5,
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 12,
  },
  categoryBadge: {
    flexDirection: 'row',
    alignItems: 'center',
    borderRadius: 6,
    borderWidth: 1,
    paddingVertical: 3,
    paddingHorizontal: 8,
    gap: 4,
  },
  categoryEmoji: {
    fontSize: 11,
  },
  categoryText: {
    fontSize: 11,
    letterSpacing: 0.5,
    fontFamily: 'Georgia',
  },
  removeIcon: {
    fontSize: 14,
    lineHeight: 20,
  },
  quoteText: {
    fontFamily: 'Georgia',
    fontSize: 17,
    lineHeight: 30,
    letterSpacing: 1.5,
    marginBottom: 8,
  },
  author: {
    fontSize: 12,
    letterSpacing: 0.5,
    fontStyle: 'italic',
    textAlign: 'right',
    marginBottom: 10,
  },
  koreanPreview: {
    fontSize: 13,
    lineHeight: 20,
    letterSpacing: 0.2,
    marginBottom: 12,
  },
  cardFooter: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  tapHint: {
    fontSize: 11,
    letterSpacing: 0.3,
  },
  heartFilled: {
    fontSize: 14,
  },
  emptyContainer: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    paddingHorizontal: 32,
    gap: 12,
  },
  emptyEmoji: {
    fontSize: 52,
    marginBottom: 8,
  },
  emptyTitle: {
    fontSize: 18,
    fontFamily: 'Georgia',
    letterSpacing: 0.5,
    textAlign: 'center',
  },
  emptyDesc: {
    fontSize: 14,
    lineHeight: 22,
    textAlign: 'center',
    letterSpacing: 0.2,
  },
  goExploreBtn: {
    marginTop: 8,
    borderRadius: 10,
    borderWidth: 1,
    paddingVertical: 12,
    paddingHorizontal: 24,
  },
  goExploreBtnText: {
    fontSize: 14,
    letterSpacing: 0.3,
    fontWeight: '600',
  },
});
