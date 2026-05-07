import React from 'react';
import {View, Text, StyleSheet} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {useStore} from '../store/useStore';

export default function QuoteListScreen({route}: any) {
  const {theme} = useStore();
  const category = route?.params?.category ?? 'all';

  return (
    <SafeAreaView style={[styles.container, {backgroundColor: theme.bg}]}>
      <View style={styles.center}>
        <Text style={[styles.text, {color: theme.accent}]}>
          명언 카드 화면 — {category}
        </Text>
        <Text style={[styles.sub, {color: theme.textMuted}]}>
          화면 ②는 다음 단계에서 구현됩니다.
        </Text>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {flex: 1},
  center: {flex: 1, alignItems: 'center', justifyContent: 'center', padding: 20},
  text: {fontSize: 18, fontFamily: 'Georgia', letterSpacing: 1, textAlign: 'center'},
  sub: {fontSize: 13, marginTop: 8, textAlign: 'center'},
});
