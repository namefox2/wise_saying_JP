import React from 'react';
import {View, Text, StyleSheet} from 'react-native';
import {SafeAreaView} from 'react-native-safe-area-context';
import {useStore} from '../store/useStore';

export default function SettingsScreen() {
  const {theme} = useStore();
  return (
    <SafeAreaView style={[styles.container, {backgroundColor: theme.bg}]}>
      <View style={styles.center}>
        <Text style={[styles.text, {color: theme.accent}]}>⚙️ 설정</Text>
        <Text style={[styles.sub, {color: theme.textMuted}]}>
          화면 ⑤는 다음 단계에서 구현됩니다.
        </Text>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {flex: 1},
  center: {flex: 1, alignItems: 'center', justifyContent: 'center', padding: 20},
  text: {fontSize: 18, fontFamily: 'Georgia', letterSpacing: 1},
  sub: {fontSize: 13, marginTop: 8},
});
