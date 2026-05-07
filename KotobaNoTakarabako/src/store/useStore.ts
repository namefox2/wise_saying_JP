import {create} from 'zustand';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {ThemeKey, themes, defaultTheme, ColorTheme} from '../theme/colors';
import {getTodayQuote} from '../data/quotes';
import type {Quote, Category} from '../data/quotes';
import type {VocabWord, JLPTLevel} from '../data/vocabulary';

const STORAGE_KEYS = {
  favorites: '@kotoba_favorites',
  theme: '@kotoba_theme',
  fontSize: '@kotoba_fontsize',
  autoBlur: '@kotoba_autoblur',
  notification: '@kotoba_notification',
  activeQuotes: '@kotoba_active_quotes',
  activeVocab: '@kotoba_active_vocab',
  lastQuotesUpdate: '@kotoba_last_quotes_update',
  lastVocabUpdate: '@kotoba_last_vocab_update',
};

interface AppState {
  hydrated: boolean;
  hydrate: () => Promise<void>;

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

  notificationEnabled: boolean;
  notificationHour: number;
  notificationMinute: number;
  setNotification: (enabled: boolean, hour: number, minute: number) => void;

  studyStreak: number;
  totalSeen: number;
  markQuoteSeen: () => void;

  // Dynamic content
  activeQuotes: Quote[] | null;
  activeVocab: Record<JLPTLevel, VocabWord[]> | null;
  lastQuotesUpdate: string | null;
  lastVocabUpdate: string | null;
  isUpdatingQuotes: boolean;
  isUpdatingVocab: boolean;
  setActiveQuotes: (quotes: Quote[]) => void;
  setActiveVocab: (level: JLPTLevel, words: VocabWord[]) => void;
  setLastQuotesUpdate: (ts: string) => void;
  setLastVocabUpdate: (ts: string) => void;
  setIsUpdatingQuotes: (v: boolean) => void;
  setIsUpdatingVocab: (v: boolean) => void;
}

export const useStore = create<AppState>((set, get) => ({
  hydrated: false,

  hydrate: async () => {
    try {
      const [
        favRaw,
        themeRaw,
        fontRaw,
        blurRaw,
        notifRaw,
        quotesRaw,
        vocabRaw,
        lastQRaw,
        lastVRaw,
      ] = await Promise.all([
        AsyncStorage.getItem(STORAGE_KEYS.favorites),
        AsyncStorage.getItem(STORAGE_KEYS.theme),
        AsyncStorage.getItem(STORAGE_KEYS.fontSize),
        AsyncStorage.getItem(STORAGE_KEYS.autoBlur),
        AsyncStorage.getItem(STORAGE_KEYS.notification),
        AsyncStorage.getItem(STORAGE_KEYS.activeQuotes),
        AsyncStorage.getItem(STORAGE_KEYS.activeVocab),
        AsyncStorage.getItem(STORAGE_KEYS.lastQuotesUpdate),
        AsyncStorage.getItem(STORAGE_KEYS.lastVocabUpdate),
      ]);

      const themeKey: ThemeKey = (themeRaw as ThemeKey) ?? 'goldDark';
      const fontSize = (fontRaw as 'small' | 'normal' | 'large') ?? 'normal';
      const autoBlurReveal = blurRaw === 'true';
      const favorites: string[] = favRaw ? JSON.parse(favRaw) : [];
      const notif = notifRaw ? JSON.parse(notifRaw) : null;
      const activeQuotes: Quote[] | null = quotesRaw ? JSON.parse(quotesRaw) : null;
      const activeVocab: Record<JLPTLevel, VocabWord[]> | null = vocabRaw
        ? JSON.parse(vocabRaw)
        : null;

      set({
        hydrated: true,
        themeKey,
        theme: themes[themeKey] ?? defaultTheme,
        fontSize,
        autoBlurReveal,
        favorites,
        notificationEnabled: notif?.enabled ?? false,
        notificationHour: notif?.hour ?? 8,
        notificationMinute: notif?.minute ?? 0,
        activeQuotes,
        activeVocab,
        lastQuotesUpdate: lastQRaw ?? null,
        lastVocabUpdate: lastVRaw ?? null,
      });
    } catch {
      set({hydrated: true});
    }
  },

  theme: defaultTheme,
  themeKey: 'goldDark',
  setTheme: (key: ThemeKey) => {
    set({theme: themes[key], themeKey: key});
    AsyncStorage.setItem(STORAGE_KEYS.theme, key);
  },

  todayQuote: getTodayQuote(),

  favorites: [],
  toggleFavorite: (id: string) => {
    const next = get().favorites.includes(id)
      ? get().favorites.filter(f => f !== id)
      : [...get().favorites, id];
    set({favorites: next});
    AsyncStorage.setItem(STORAGE_KEYS.favorites, JSON.stringify(next));
  },
  isFavorite: (id: string) => get().favorites.includes(id),

  selectedCategory: 'all',
  setSelectedCategory: (cat: Category) => set({selectedCategory: cat}),

  fontSize: 'normal',
  setFontSize: (size) => {
    set({fontSize: size});
    AsyncStorage.setItem(STORAGE_KEYS.fontSize, size);
  },

  autoBlurReveal: false,
  setAutoBlurReveal: (val) => {
    set({autoBlurReveal: val});
    AsyncStorage.setItem(STORAGE_KEYS.autoBlur, String(val));
  },

  notificationEnabled: false,
  notificationHour: 8,
  notificationMinute: 0,
  setNotification: (enabled, hour, minute) => {
    set({notificationEnabled: enabled, notificationHour: hour, notificationMinute: minute});
    AsyncStorage.setItem(
      STORAGE_KEYS.notification,
      JSON.stringify({enabled, hour, minute}),
    );
  },

  studyStreak: 1,
  totalSeen: 0,
  markQuoteSeen: () => set(state => ({totalSeen: state.totalSeen + 1})),

  // Dynamic content
  activeQuotes: null,
  activeVocab: null,
  lastQuotesUpdate: null,
  lastVocabUpdate: null,
  isUpdatingQuotes: false,
  isUpdatingVocab: false,

  setActiveQuotes: (quotes: Quote[]) => {
    set({activeQuotes: quotes});
    AsyncStorage.setItem(STORAGE_KEYS.activeQuotes, JSON.stringify(quotes));
  },
  setActiveVocab: (level: JLPTLevel, words: VocabWord[]) => {
    const prev = get().activeVocab ?? ({} as Record<JLPTLevel, VocabWord[]>);
    const next = {...prev, [level]: words};
    set({activeVocab: next});
    AsyncStorage.setItem(STORAGE_KEYS.activeVocab, JSON.stringify(next));
  },
  setLastQuotesUpdate: (ts: string) => {
    set({lastQuotesUpdate: ts});
    AsyncStorage.setItem(STORAGE_KEYS.lastQuotesUpdate, ts);
  },
  setLastVocabUpdate: (ts: string) => {
    set({lastVocabUpdate: ts});
    AsyncStorage.setItem(STORAGE_KEYS.lastVocabUpdate, ts);
  },
  setIsUpdatingQuotes: (v: boolean) => set({isUpdatingQuotes: v}),
  setIsUpdatingVocab: (v: boolean) => set({isUpdatingVocab: v}),
}));
