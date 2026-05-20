import React from 'react';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import {Text, View, StyleSheet} from 'react-native';
import {useStore} from '../store/useStore';

import HomeScreen from '../screens/HomeScreen';
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

function HomeStackNavigator() {
  const {theme} = useStore();
  return (
    <HomeStack.Navigator
      screenOptions={{
        headerShown: false,
        contentStyle: {backgroundColor: theme.bg},
      }}>
      <HomeStack.Screen name="Home" component={HomeScreen} />
      <HomeStack.Screen name="QuoteCard" component={QuoteCardScreen} />
      <HomeStack.Screen name="Settings" component={SettingsScreen} />
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
      <DictStack.Screen name="VocabHome" component={VocabHomeScreen} />
      <DictStack.Screen name="VocabCard" component={VocabCardScreen} />
    </DictStack.Navigator>
  );
}

// Tab icon components
function TabIcon({icon, focused, color}: {icon: string; focused: boolean; color: string}) {
  return (
    <View style={tabIconStyles.wrap}>
      <Text style={[tabIconStyles.icon, {opacity: focused ? 1 : 0.45, color}]}>
        {icon}
      </Text>
      {focused && <View style={[tabIconStyles.dot, {backgroundColor: color}]} />}
    </View>
  );
}

const tabIconStyles = StyleSheet.create({
  wrap: {alignItems: 'center', gap: 3},
  icon: {fontSize: 22},
  dot: {width: 4, height: 4, borderRadius: 2},
});

export default function AppNavigator() {
  const {theme, favorites} = useStore();

  return (
    <Tab.Navigator
      screenOptions={{
        headerShown: false,
        tabBarStyle: {
          backgroundColor: theme.navBg,
          borderTopColor: theme.border,
          borderTopWidth: StyleSheet.hairlineWidth,
          height: 62,
          paddingBottom: 8,
          paddingTop: 6,
        },
        tabBarActiveTintColor: theme.navActive,
        tabBarInactiveTintColor: theme.navInactive,
        tabBarLabelStyle: {
          fontSize: 10,
          letterSpacing: 0.3,
        },
      }}>
      <Tab.Screen
        name="홈"
        component={HomeStackNavigator}
        options={{
          tabBarIcon: ({focused, color}) => (
            <TabIcon icon="⊞" focused={focused} color={color} />
          ),
        }}
      />
      <Tab.Screen
        name="즐겨찾기"
        component={FavStackNavigator}
        options={{
          tabBarIcon: ({focused, color}) => (
            <TabIcon icon="♡" focused={focused} color={color} />
          ),
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
      <Tab.Screen
        name="사전"
        component={DictStackNavigator}
        options={{
          tabBarIcon: ({focused, color}) => (
            <TabIcon icon="📖" focused={focused} color={color} />
          ),
        }}
      />
    </Tab.Navigator>
  );
}
