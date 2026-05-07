import React from 'react';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import {Text} from 'react-native';
import {useStore} from '../store/useStore';

import HomeScreen from '../screens/HomeScreen';
import QuoteListScreen from '../screens/QuoteListScreen';
import FavoritesScreen from '../screens/FavoritesScreen';
import DictionaryScreen from '../screens/DictionaryScreen';
import SettingsScreen from '../screens/SettingsScreen';

const Tab = createBottomTabNavigator();
const HomeStack = createNativeStackNavigator();

function HomeStackNavigator() {
  const {theme} = useStore();
  return (
    <HomeStack.Navigator
      screenOptions={{
        headerShown: false,
        contentStyle: {backgroundColor: theme.bg},
      }}>
      <HomeStack.Screen name="Home" component={HomeScreen} />
      <HomeStack.Screen name="QuoteList" component={QuoteListScreen} />
    </HomeStack.Navigator>
  );
}

const TAB_ICONS: Record<string, string> = {
  홈: '🏠',
  즐겨찾기: '♥',
  사전: '📖',
  설정: '⚙️',
};

export default function AppNavigator() {
  const {theme} = useStore();

  return (
    <Tab.Navigator
      screenOptions={({route}) => ({
        headerShown: false,
        tabBarStyle: {
          backgroundColor: theme.navBg,
          borderTopColor: theme.border,
          borderTopWidth: 1,
          height: 60,
          paddingBottom: 8,
        },
        tabBarActiveTintColor: theme.navActive,
        tabBarInactiveTintColor: theme.navInactive,
        tabBarLabelStyle: {
          fontSize: 10,
          letterSpacing: 0.3,
          marginTop: -2,
        },
        tabBarIcon: ({focused}) => (
          <Text
            style={{
              fontSize: 20,
              opacity: focused ? 1 : 0.5,
            }}>
            {TAB_ICONS[route.name] ?? '•'}
          </Text>
        ),
      })}>
      <Tab.Screen name="홈" component={HomeStackNavigator} />
      <Tab.Screen name="즐겨찾기" component={FavoritesScreen} />
      <Tab.Screen name="사전" component={DictionaryScreen} />
      <Tab.Screen name="설정" component={SettingsScreen} />
    </Tab.Navigator>
  );
}
