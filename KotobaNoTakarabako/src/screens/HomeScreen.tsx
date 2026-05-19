import React from 'react';
import {
  View,
  Text,
  ScrollView,
  StyleSheet,
  StatusBar,
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
  const {theme} = useStore();

  const handleCategorySelect = (cat: Category) => {
    navigation.navigate('QuoteCard', {category: cat, startIndex: 0});
  };

  return (
    <SafeAreaView
      style={[styles.safeArea, {backgroundColor: theme.bg}]}
      edges={['top', 'left', 'right']}>
      <StatusBar barStyle="light-content" backgroundColor={theme.bg} translucent={false} />

      {/* Header */}
      <View style={[styles.header, {borderBottomColor: theme.border}]}>
        <View>
          <Text style={[styles.appName, {color: theme.accent}]}>言葉の宝箱</Text>
          <Text style={[styles.appSub, {color: theme.textMuted}]}>일본어 명언집</Text>
        </View>
        <TouchableOpacity
          style={[styles.gearBtn, {backgroundColor: theme.bgCard, borderColor: theme.border}]}
          onPress={() => navigation.navigate('Settings')}
          activeOpacity={0.7}>
          <Text style={styles.gearIcon}>⚙</Text>
        </TouchableOpacity>
      </View>

      <ScrollView
        contentContainerStyle={styles.scrollContent}
        showsVerticalScrollIndicator={false}>

        {/* 오늘의 명언 */}
        <TodayQuoteCard />

        {/* 카테고리 */}
        <Text style={[styles.sectionTitle, {color: theme.text}]}>카테고리</Text>
        <CategoryGrid onSelectCategory={handleCategorySelect} />

        <View style={{height: 32}} />
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {flex: 1},
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingHorizontal: 20,
    paddingVertical: 14,
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  appName: {
    fontSize: 22,
    fontFamily: 'Georgia',
    letterSpacing: 1.5,
    fontWeight: '700',
  },
  appSub: {
    fontSize: 11,
    letterSpacing: 0.5,
    marginTop: 3,
  },
  gearBtn: {
    width: 38,
    height: 38,
    borderRadius: 19,
    borderWidth: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  gearIcon: {
    fontSize: 18,
    color: '#C8A96E',
  },
  scrollContent: {
    paddingHorizontal: 16,
    paddingTop: 20,
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: '600',
    letterSpacing: 0.3,
    marginBottom: 14,
    marginTop: 4,
  },
});
