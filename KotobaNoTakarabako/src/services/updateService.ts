import type {VocabWord, JLPTLevel, VocabPos} from '../data/vocabulary';
import {getAllVocab} from '../data/vocabulary';
import {pickRandomQuotes} from '../data/quotes';
import type {Quote} from '../data/quotes';

const JISHO_BASE = 'https://jisho.org/api/v1/search/words';

function mapPos(parts: string[]): VocabPos {
  const joined = parts.join(' ').toLowerCase();
  if (joined.includes('noun')) return '명사';
  if (joined.includes('adverb')) return '부사';
  if (joined.includes('conjunction')) return '접속사';
  if (joined.includes('i-adjective') || joined.includes('い-adjective')) return 'い형용사';
  if (joined.includes('na-adjective') || joined.includes('な-adjective')) return 'な형용사';
  if (
    joined.includes('ichidan') ||
    joined.includes('godan') ||
    joined.includes('suru verb') ||
    joined.includes('irregular verb') ||
    joined.includes('verb')
  ) return '동사';
  return '명사';
}

interface JishoEntry {
  slug: string;
  jlpt: string[];
  japanese: {word?: string; reading: string}[];
  senses: {
    english_definitions: string[];
    parts_of_speech: string[];
  }[];
}

export async function fetchVocabFromJisho(
  level: JLPTLevel,
  favoriteIds: string[],
  targetCount = 30,
): Promise<VocabWord[]> {
  const tag = `%23jlpt-${level.toLowerCase()}`;
  const pages = [1, 2, 3];
  const fetched: VocabWord[] = [];
  const seen = new Set<string>();

  for (const page of pages) {
    if (fetched.length >= targetCount * 2) break;
    try {
      const res = await fetch(`${JISHO_BASE}?keyword=${tag}&page=${page}`);
      if (!res.ok) break;
      const json = await res.json();
      const data: JishoEntry[] = json?.data ?? [];

      for (const entry of data) {
        const jp = entry.japanese[0];
        if (!jp) continue;
        const kanji = jp.word ?? jp.reading;
        const reading = jp.reading;
        if (!kanji || !reading || seen.has(kanji)) continue;
        seen.add(kanji);

        const sense = entry.senses[0];
        const meanings = sense?.english_definitions?.slice(0, 3) ?? [];
        const pos = mapPos(sense?.parts_of_speech ?? []);

        fetched.push({
          id: `jisho_${level}_${entry.slug}`,
          kanji,
          reading,
          korean: meanings.length > 0 ? meanings : ['—'],
          pos,
          level,
        });
      }
    } catch {
      break;
    }
  }

  // Preserve favorited words from existing vocab
  const allExisting = getAllVocab().filter(w => w.level === level);
  const favored = allExisting.filter(w => favoriteIds.includes(w.id));
  const nonFavExisting = allExisting.filter(w => !favoriteIds.includes(w.id));

  // Fill with fetched; fallback to existing non-favorites if fetch is thin
  const newPool = fetched.length >= 10 ? fetched : [...fetched, ...nonFavExisting];
  const shuffled = newPool.sort(() => Math.random() - 0.5);
  const slots = Math.max(0, targetCount - favored.length);
  const result = [...favored, ...shuffled.slice(0, slots)];

  return result.slice(0, targetCount);
}

export function refreshQuotes(
  favoriteIds: string[],
  targetCount = 90,
): Quote[] {
  return pickRandomQuotes(targetCount, favoriteIds);
}
