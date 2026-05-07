import React from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  StyleSheet,
  Dimensions,
} from 'react-native';
import {useStore} from '../store/useStore';
import {CATEGORIES} from '../data/quotes';
import type {Category} from '../data/quotes';

const {width} = Dimensions.get('window');
const CARD_GAP = 10;
const HORIZONTAL_PADDING = 20;
const NUM_COLS = 2;
const CARD_WIDTH =
  (width - HORIZONTAL_PADDING * 2 - CARD_GAP * (NUM_COLS - 1)) / NUM_COLS;

interface CategoryGridProps {
  onSelectCategory: (cat: Category) => void;
}

export default function CategoryGrid({onSelectCategory}: CategoryGridProps) {
  const {theme, selectedCategory, setSelectedCategory} = useStore();

  const handlePress = (cat: Category) => {
    setSelectedCategory(cat);
    onSelectCategory(cat);
  };

  return (
    <View>
      <Text style={[styles.sectionTitle, {color: theme.textMuted}]}>
        카테고리 선택
      </Text>
      <View style={styles.grid}>
        {CATEGORIES.map(cat => {
          const isActive = selectedCategory === cat.id;
          return (
            <TouchableOpacity
              key={cat.id}
              style={[
                styles.card,
                {
                  width: CARD_WIDTH,
                  backgroundColor: isActive
                    ? theme.bgCardAlt
                    : theme.categoryBg,
                  borderColor: isActive ? theme.accent : theme.categoryBorder,
                  shadowColor: isActive ? theme.accent : 'transparent',
                },
              ]}
              onPress={() => handlePress(cat.id)}
              activeOpacity={0.75}>
              <Text style={styles.cardEmoji}>{cat.emoji}</Text>
              <Text style={[styles.cardKanji, {color: theme.text}]}>
                {cat.kanji}
              </Text>
              <Text style={[styles.cardLabel, {color: theme.textSub}]}>
                {cat.label}
              </Text>
              {isActive && (
                <View
                  style={[
                    styles.activeIndicator,
                    {backgroundColor: theme.accent},
                  ]}
                />
              )}
            </TouchableOpacity>
          );
        })}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  sectionTitle: {
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
  card: {
    borderRadius: 14,
    borderWidth: 1,
    padding: 16,
    alignItems: 'flex-start',
    justifyContent: 'flex-start',
    minHeight: 90,
    shadowOffset: {width: 0, height: 2},
    shadowOpacity: 0.2,
    shadowRadius: 6,
    elevation: 4,
    overflow: 'hidden',
  },
  cardEmoji: {
    fontSize: 22,
    marginBottom: 6,
  },
  cardKanji: {
    fontSize: 16,
    fontFamily: 'Georgia',
    fontWeight: '600',
    letterSpacing: 1,
  },
  cardLabel: {
    fontSize: 11,
    letterSpacing: 0.3,
    marginTop: 2,
  },
  activeIndicator: {
    position: 'absolute',
    bottom: 0,
    left: 0,
    right: 0,
    height: 2,
    borderRadius: 1,
  },
});
