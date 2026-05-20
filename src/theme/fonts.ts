import {Platform} from 'react-native';

// Georgia (iOS) → serif / NotoSerif (Android)
export const SERIF_FONT = Platform.OS === 'android' ? 'serif' : 'Georgia';
