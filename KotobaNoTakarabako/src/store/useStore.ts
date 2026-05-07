import {create} from 'zustand';
import {ThemeKey, themes, defaultTheme, ColorTheme} from '../theme/colors';
import {getTodayQuote} from '../data/quotes';
import type {Quote, Category} from '../data/quotes';

interface AppState {
  theme: ColorTheme;
  themeKey: ThemeKey;
  setTheme: (key: ThemeKey) => void;

  todayQuote: Quote;

  favorites: string[];
  toggleFavorite: (id: string) => void;
  isFavorite: (id: string) => boolean;

  selectedCategory: Category;
  setSelectedCategory: (cat: Category) => void;

  fontSize: 'small' | 'normal' | 'large';
  setFontSize: (size: 'small' | 'normal' | 'large') => void;

  autoBlurReveal: boolean;
  setAutoBlurReveal: (val: boolean) => void;

  studyStreak: number;
  totalSeen: number;
  markQuoteSeen: () => void;
}

export const useStore = create<AppState>((set, get) => ({
  theme: defaultTheme,
  themeKey: 'goldDark',
  setTheme: (key: ThemeKey) => set({theme: themes[key], themeKey: key}),

  todayQuote: getTodayQuote(),

  favorites: [],
  toggleFavorite: (id: string) =>
    set(state => ({
      favorites: state.favorites.includes(id)
        ? state.favorites.filter(f => f !== id)
        : [...state.favorites, id],
    })),
  isFavorite: (id: string) => get().favorites.includes(id),

  selectedCategory: 'all',
  setSelectedCategory: (cat: Category) => set({selectedCategory: cat}),

  fontSize: 'normal',
  setFontSize: (size) => set({fontSize: size}),

  autoBlurReveal: false,
  setAutoBlurReveal: (val) => set({autoBlurReveal: val}),

  studyStreak: 1,
  totalSeen: 0,
  markQuoteSeen: () => set(state => ({totalSeen: state.totalSeen + 1})),
}));
