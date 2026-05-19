import React from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  Dimensions,
} from 'react-native';
import {useStore} from '../store/useStore';
import {CATEGORIES, getQuotesByCategory} from '../data/quotes';
import type {Category} from '../data/quotes';

const {width} = Dimensions.get('window');
const CARD_GAP = 10;
const HORIZONTAL_PADDING = 16;
const NUM_COLS = 2;
const CARD_WIDTH =
  (width - HORIZONTAL_PADDING * 2 - CARD_GAP * (NUM_COLS - 1)) / NUM_COLS;

interface CategoryGridProps {
  onSelectCategory: (cat: Category) => void;
}

export default function CategoryGrid({onSelectCategory}: CategoryGridProps) {
  const {theme, selectedCategory, setSelectedCategory, activeQuotes} = useStore();

  const handlePress = (cat: Category) => {
    setSelectedCategory(cat);
    onSelectCategory(cat);
  };

  return (
    <View style={styles.grid}>
      {CATEGORIES.map(cat => {
        const isActive = selectedCategory === cat.id;
        const count = getQuotesByCategory(cat.id, activeQuotes ?? undefined).length;

        return (
          <TouchableOpacity
            key={cat.id}
            style={[
              styles.card,
              {
                width: CARD_WIDTH,
                backgroundColor: isActive ? theme.bgCardAlt : theme.bgCard,
                borderColor: isActive ? theme.accent : theme.border,
              },
            ]}
            onPress={() => handlePress(cat.id)}
            activeOpacity={0.75}>

            <Text style={styles.cardEmoji}>{cat.emoji}</Text>
            <Text style={[styles.cardLabel, {color: theme.text}]}>{cat.label}</Text>
            <Text style={[styles.cardCount, {color: theme.textMuted}]}>
              명언 {count}개
            </Text>

            {isActive && (
              <View style={[styles.activeBar, {backgroundColor: theme.accent}]} />
            )}
          </TouchableOpacity>
        );
      })}
    </View>
  );
}

const styles = StyleSheet.create({
  grid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    gap: CARD_GAP,
  },
  card: {
    width: CARD_WIDTH,
    borderRadius: 14,
    borderWidth: 1,
    padding: 16,
    minHeight: 100,
    overflow: 'hidden',
    justifyContent: 'flex-start',
  },
  cardEmoji: {
    fontSize: 28,
    marginBottom: 8,
  },
  cardLabel: {
    fontSize: 16,
    fontWeight: '700',
    letterSpacing: 0.2,
    marginBottom: 4,
  },
  cardCount: {
    fontSize: 12,
    letterSpacing: 0.2,
  },
  activeBar: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    height: 2,
  },
});
