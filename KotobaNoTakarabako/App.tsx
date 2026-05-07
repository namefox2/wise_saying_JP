import React, {useEffect} from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {SafeAreaProvider} from 'react-native-safe-area-context';
import AppNavigator from './src/navigation/AppNavigator';
import {useStore} from './src/store/useStore';

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
            regular: {fontFamily: 'Georgia', fontWeight: '400'},
            medium: {fontFamily: 'Georgia', fontWeight: '500'},
            bold: {fontFamily: 'Georgia', fontWeight: '700'},
            heavy: {fontFamily: 'Georgia', fontWeight: '900'},
          },
        }}>
        <AppNavigator />
      </NavigationContainer>
    </SafeAreaProvider>
  );
}
