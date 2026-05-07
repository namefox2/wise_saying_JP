import React, {useRef, useEffect} from 'react';
import {
  Animated,
  TouchableOpacity,
  View,
  Text,
  StyleSheet,
  ViewStyle,
} from 'react-native';
import {useStore} from '../store/useStore';

interface BlurRevealProps {
  children: React.ReactNode;
  revealed: boolean;
  onToggle: () => void;
  label?: string;
  closeLabel?: string;
  style?: ViewStyle;
}

export default function BlurReveal({
  children,
  revealed,
  onToggle,
  label = '탭하여 보기',
  closeLabel = '닫기',
  style,
}: BlurRevealProps) {
  const {theme} = useStore();
  const opacity = useRef(new Animated.Value(revealed ? 1 : 0)).current;

  useEffect(() => {
    Animated.timing(opacity, {
      toValue: revealed ? 1 : 0,
      duration: 300,
      useNativeDriver: true,
    }).start();
  }, [revealed, opacity]);

  return (
    <TouchableOpacity
      activeOpacity={0.85}
      onPress={onToggle}
      style={[styles.wrapper, style]}>
      {/* Blurred placeholder */}
      {!revealed && (
        <View
          style={[
            styles.blurPlaceholder,
            {backgroundColor: theme.bgCardAlt, borderColor: theme.border},
          ]}>
          <Text style={[styles.blurLabel, {color: theme.textMuted}]}>
            {label}
          </Text>
        </View>
      )}

      {/* Revealed content */}
      {revealed && (
        <Animated.View style={[styles.revealedContainer, {opacity}]}>
          <View style={styles.contentRow}>
            <View style={styles.contentFlex}>{children}</View>
            <Text style={[styles.closeLabel, {color: theme.accentSoft}]}>
              {closeLabel}
            </Text>
          </View>
        </Animated.View>
      )}
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  wrapper: {
    width: '100%',
  },
  blurPlaceholder: {
    borderRadius: 10,
    borderWidth: 1,
    paddingVertical: 14,
    paddingHorizontal: 16,
    alignItems: 'center',
    justifyContent: 'center',
    minHeight: 50,
  },
  blurLabel: {
    fontSize: 13,
    letterSpacing: 0.5,
  },
  revealedContainer: {
    width: '100%',
  },
  contentRow: {
    flexDirection: 'row',
    alignItems: 'flex-start',
    justifyContent: 'space-between',
  },
  contentFlex: {
    flex: 1,
  },
  closeLabel: {
    fontSize: 11,
    marginLeft: 8,
    marginTop: 2,
    letterSpacing: 0.3,
  },
});
