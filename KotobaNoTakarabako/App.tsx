import React, {useEffect} from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {SafeAreaProvider} from 'react-native-safe-area-context';
import AppNavigator from './src/navigation/AppNavigator';
import {useStore} from './src/store/useStore';
import {SERIF_FONT} from './src/theme/fonts';

export default function App() {
  const {theme, hydrate} = useStore();

  useEffect(() => {
    hydrate();
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <SafeAreaProvider>
      <NavigationContainer
        theme={{
          dark: theme.key !== 'washiLight',
          colors: {
            primary: theme.accent,
            background: theme.bg,
            card: theme.navBg,
            text: theme.text,
            border: theme.border,
            notification: theme.accent,
          },
          fonts: {
            regular: {fontFamily: SERIF_FONT, fontWeight: '400'},
            medium: {fontFamily: SERIF_FONT, fontWeight: '500'},
            bold: {fontFamily: SERIF_FONT, fontWeight: '700'},
            heavy: {fontFamily: SERIF_FONT, fontWeight: '900'},
          },
        }}>
        <AppNavigator />
      </NavigationContainer>
    </SafeAreaProvider>
  );
}
