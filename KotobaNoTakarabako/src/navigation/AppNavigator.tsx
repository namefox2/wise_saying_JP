import React from 'react';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import {Text} from 'react-native';
import {useStore} from '../store/useStore';

import HomeScreen from '../screens/HomeScreen';
import QuoteListScreen from '../screens/QuoteListScreen';
import QuoteCardScreen from '../screens/QuoteCardScreen';
import FavoritesScreen from '../screens/FavoritesScreen';
import DictionaryScreen from '../screens/DictionaryScreen';
import VocabHomeScreen from '../screens/VocabHomeScreen';
import VocabCardScreen from '../screens/VocabCardScreen';
import SettingsScreen from '../screens/SettingsScreen';

const Tab = createBottomTabNavigator();
const HomeStack = createNativeStackNavigator();
const FavStack = createNativeStackNavigator();
const DictStack = createNativeStackNavigator();
const VocabStack = createNativeStackNavigator();

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
      <HomeStack.Screen name="QuoteCard" component={QuoteCardScreen} />
    </HomeStack.Navigator>
  );
}

function FavStackNavigator() {
  const {theme} = useStore();
  return (
    <FavStack.Navigator
      screenOptions={{
        headerShown: false,
        contentStyle: {backgroundColor: theme.bg},
      }}>
      <FavStack.Screen name="FavoritesList" component={FavoritesScreen} />
      <FavStack.Screen name="QuoteCard" component={QuoteCardScreen} />
    </FavStack.Navigator>
  );
}

function DictStackNavigator() {
  const {theme} = useStore();
  return (
    <DictStack.Navigator
      screenOptions={{
        headerShown: false,
        contentStyle: {backgroundColor: theme.bg},
      }}>
      <DictStack.Screen name="DictionaryMain" component={DictionaryScreen} />
      <DictStack.Screen name="QuoteCard" component={QuoteCardScreen} />
    </DictStack.Navigator>
  );
}

function VocabStackNavigator() {
  const {theme} = useStore();
  return (
    <VocabStack.Navigator
      screenOptions={{
        headerShown: false,
        contentStyle: {backgroundColor: theme.bg},
      }}>
      <VocabStack.Screen name="VocabHome" component={VocabHomeScreen} />
      <VocabStack.Screen name="VocabCard" component={VocabCardScreen} />
    </VocabStack.Navigator>
  );
}

const TAB_ICONS: Record<string, string> = {
  홈: '🏠',
  단어: '🃏',
  즐겨찾기: '♥',
  사전: '📖',
  설정: '⚙️',
};

export default function AppNavigator() {
  const {theme, favorites} = useStore();

  return (
    <Tab.Navigator
      screenOptions={({route}) => ({
        headerShown: false,
        tabBarStyle: {
          backgroundColor: theme.navBg,
          borderTopColor: theme.border,
          borderTopWidth: 1,
          height: 64,
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
      <Tab.Screen name="단어" component={VocabStackNavigator} />
      <Tab.Screen
        name="즐겨찾기"
        component={FavStackNavigator}
        options={{
          tabBarBadge: favorites.length > 0 ? favorites.length : undefined,
          tabBarBadgeStyle: {
            backgroundColor: '#E8507A',
            fontSize: 9,
            minWidth: 16,
            height: 16,
            lineHeight: 16,
          },
        }}
      />
      <Tab.Screen name="사전" component={DictStackNavigator} />
      <Tab.Screen name="설정" component={SettingsScreen} />
    </Tab.Navigator>
  );
}
