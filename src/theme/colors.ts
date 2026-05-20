export type ThemeKey = 'goldDark' | 'sakuraDark' | 'oceanDark' | 'washiLight';

export interface ColorTheme {
  key: ThemeKey;
  name: string;
  nameKo: string;
  bg: string;
  bgCard: string;
  bgCardAlt: string;
  accent: string;
  accentSoft: string;
  text: string;
  textSub: string;
  textMuted: string;
  border: string;
  blur: string;
  navBg: string;
  navActive: string;
  navInactive: string;
  categoryBg: string;
  categoryBorder: string;
  progressTrack: string;
  progressFill: string;
}

export const themes: Record<ThemeKey, ColorTheme> = {
  goldDark: {
    key: 'goldDark',
    name: 'Gold Dark',
    nameKo: '골드 다크',
    bg: '#0A0A12',
    bgCard: '#13131F',
    bgCardAlt: '#1A1A28',
    accent: '#C8A96E',
    accentSoft: '#9E7A45',
    text: '#F0E6D3',
    textSub: '#C8A96E',
    textMuted: '#6B6680',
    border: '#2A2A3F',
    blur: '#0A0A12CC',
    navBg: '#0D0D1A',
    navActive: '#C8A96E',
    navInactive: '#4A4460',
    categoryBg: '#13131F',
    categoryBorder: '#2A2A3F',
    progressTrack: '#1E1E30',
    progressFill: '#C8A96E',
  },
  sakuraDark: {
    key: 'sakuraDark',
    name: 'Sakura Dark',
    nameKo: '벚꽃 다크',
    bg: '#12080F',
    bgCard: '#1F1018',
    bgCardAlt: '#271520',
    accent: '#E8A0B4',
    accentSoft: '#B87090',
    text: '#F5E0E8',
    textSub: '#E8A0B4',
    textMuted: '#7A5068',
    border: '#3A1E2E',
    blur: '#12080FCC',
    navBg: '#150A12',
    navActive: '#E8A0B4',
    navInactive: '#5A304A',
    categoryBg: '#1F1018',
    categoryBorder: '#3A1E2E',
    progressTrack: '#251020',
    progressFill: '#E8A0B4',
  },
  oceanDark: {
    key: 'oceanDark',
    name: 'Ocean Dark',
    nameKo: '오션 다크',
    bg: '#060E18',
    bgCard: '#0D1A28',
    bgCardAlt: '#122035',
    accent: '#5BBFDE',
    accentSoft: '#3A8AAE',
    text: '#D0EAF5',
    textSub: '#5BBFDE',
    textMuted: '#3A5568',
    border: '#1A3048',
    blur: '#060E18CC',
    navBg: '#080F1A',
    navActive: '#5BBFDE',
    navInactive: '#204060',
    categoryBg: '#0D1A28',
    categoryBorder: '#1A3048',
    progressTrack: '#0F2038',
    progressFill: '#5BBFDE',
  },
  washiLight: {
    key: 'washiLight',
    name: 'Washi Light',
    nameKo: '화지 라이트',
    bg: '#F5F0E8',
    bgCard: '#FFFEF8',
    bgCardAlt: '#F0EBE0',
    accent: '#8B5E3C',
    accentSoft: '#B8864E',
    text: '#2C1F0E',
    textSub: '#8B5E3C',
    textMuted: '#A09080',
    border: '#DDD5C8',
    blur: '#F5F0E8CC',
    navBg: '#F0EAE0',
    navActive: '#8B5E3C',
    navInactive: '#C0B0A0',
    categoryBg: '#FFFEF8',
    categoryBorder: '#DDD5C8',
    progressTrack: '#E8E0D4',
    progressFill: '#8B5E3C',
  },
};

export const defaultTheme = themes.goldDark;
