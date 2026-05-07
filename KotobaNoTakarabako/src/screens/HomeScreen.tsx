import React, {useRef} from 'react';
import {
  View,
  Text,
  ScrollView,
  StyleSheet,
  StatusBar,
  Animated,
  TouchableOpacity,
} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {useStore} from '../store/useStore';
import TodayQuoteCard from '../components/TodayQuoteCard';
import CategoryGrid from '../components/CategoryGrid';
import type {Category} from '../data/quotes';

interface HomeScreenProps {
  navigation: any;
}

export default function HomeScreen({navigation}: HomeScreenProps) {
  const {theme, studyStreak, totalSeen} = useStore();
  const scrollY = useRef(new Animated.Value(0)).current;

  const headerOpacity = scrollY.interpolate({
    inputRange: [0, 60],
    outputRange: [1, 0.85],
    extrapolate: 'clamp',
  });

  const handleCategorySelect = (cat: Category) => {
    navigation.navigate('QuoteCard', {category: cat, startIndex: 0});
  };

  return (
    <SafeAreaView
      style={[styles.safeArea, {backgroundColor: theme.bg}]}
      edges={['top', 'left', 'right']}>
      <StatusBar
        barStyle="light-content"
        backgroundColor={theme.bg}
        translucent={false}
      />

      {/* Fixed Header */}
      <Animated.View
        style={[
          styles.header,
          {
            backgroundColor: theme.bg,
            borderBottomColor: theme.border,
            opacity: headerOpacity,
          },
        ]}>
        <View>
          <Text style={[styles.appName, {color: theme.accent}]}>
            言葉の宝箱
          </Text>
          <Text style={[styles.appNameKo, {color: theme.textMuted}]}>
            코토바노 타카라바코
          </Text>
        </View>
        <View style={styles.statsRow}>
          <View
            style={[styles.statChip, {backgroundColor: theme.bgCardAlt, borderColor: theme.border}]}>
            <Text style={[styles.statIcon]}>🔥</Text>
            <Text style={[styles.statValue, {color: theme.accent}]}>
              {studyStreak}
            </Text>
            <Text style={[styles.statLabel, {color: theme.textMuted}]}>일</Text>
          </View>
          <View
            style={[styles.statChip, {backgroundColor: theme.bgCardAlt, borderColor: theme.border}]}>
            <Text style={styles.statIcon}>📖</Text>
            <Text style={[styles.statValue, {color: theme.accent}]}>
              {totalSeen}
            </Text>
            <Text style={[styles.statLabel, {color: theme.textMuted}]}>개</Text>
          </View>
        </View>
      </Animated.View>

      <Animated.ScrollView
        contentContainerStyle={styles.scrollContent}
        showsVerticalScrollIndicator={false}
        onScroll={Animated.event(
          [{nativeEvent: {contentOffset: {y: scrollY}}}],
          {useNativeDriver: true},
        )}
        scrollEventThrottle={16}>
        {/* Decorative accent line */}
        <View
          style={[styles.accentLine, {backgroundColor: theme.accent}]}
        />

        {/* Section: Today's Quote */}
        <View style={styles.section}>
          <TodayQuoteCard />
        </View>

        {/* Divider with label */}
        <View style={styles.dividerRow}>
          <View style={[styles.divider, {backgroundColor: theme.border}]} />
          <Text style={[styles.dividerLabel, {color: theme.textMuted}]}>
            카테고리
          </Text>
          <View style={[styles.divider, {backgroundColor: theme.border}]} />
        </View>

        {/* Section: Category Grid */}
        <View style={styles.section}>
          <CategoryGrid onSelectCategory={handleCategorySelect} />
        </View>

        {/* Bottom Padding */}
        <View style={{height: 32}} />
      </Animated.ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingVertical: 12,
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  appName: {
    fontSize: 20,
    fontFamily: 'Georgia',
    letterSpacing: 2,
    fontWeight: '600',
  },
  appNameKo: {
    fontSize: 10,
    letterSpacing: 0.5,
    marginTop: 1,
  },
  statsRow: {
    flexDirection: 'row',
    gap: 8,
  },
  statChip: {
    flexDirection: 'row',
    alignItems: 'center',
    borderRadius: 8,
    borderWidth: 1,
    paddingVertical: 5,
    paddingHorizontal: 8,
    gap: 3,
  },
  statIcon: {
    fontSize: 12,
  },
  statValue: {
    fontSize: 14,
    fontWeight: '700',
  },
  statLabel: {
    fontSize: 10,
  },
  scrollContent: {
    paddingHorizontal: 20,
    paddingTop: 20,
  },
  accentLine: {
    height: 2,
    width: 40,
    borderRadius: 1,
    marginBottom: 24,
  },
  section: {
    marginBottom: 8,
  },
  dividerRow: {
    flexDirection: 'row',
    alignItems: 'center',
    marginVertical: 20,
    gap: 10,
  },
  divider: {
    flex: 1,
    height: StyleSheet.hairlineWidth,
  },
  dividerLabel: {
    fontSize: 11,
    letterSpacing: 1,
    textTransform: 'uppercase',
  },
});
