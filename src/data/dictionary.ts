export type PartOfSpeech =
  | '명사'
  | '동사'
  | '형용사'
  | 'な형용사'
  | '부사'
  | '관용구'
  | '사자성어';

export interface DictEntry {
  id: string;
  word: string;
  reading: string;
  romaji: string;
  pos: PartOfSpeech;
  meanings: string[];
  examples: {japanese: string; reading: string; korean: string}[];
  tags?: string[];
}

const dictionary: DictEntry[] = [
  // ── 努力 관련 ──────────────────────────────────────────────────
  {
    id: 'd001',
    word: '努力',
    reading: 'どりょく',
    romaji: 'doryoku',
    pos: '명사',
    meanings: ['노력', '분발'],
    examples: [
      {
        japanese: '努力は必ず報われる。',
        reading: 'どりょくはかならずむくわれる。',
        korean: '노력은 반드시 보상받는다.',
      },
    ],
    tags: ['노력', 'JLPT N3'],
  },
  {
    id: 'd002',
    word: '継続',
    reading: 'けいぞく',
    romaji: 'keizoku',
    pos: '명사',
    meanings: ['계속', '지속', '연속'],
    examples: [
      {
        japanese: '継続は力なり。',
        reading: 'けいぞくはちからなり。',
        korean: '지속은 힘이다.',
      },
    ],
    tags: ['노력', 'JLPT N3'],
  },
  {
    id: 'd003',
    word: '報われる',
    reading: 'むくわれる',
    romaji: 'mukuwareru',
    pos: '동사',
    meanings: ['보상받다', '보람이 있다', '결실을 맺다'],
    examples: [
      {
        japanese: '努力は必ず報われる。',
        reading: 'どりょくはかならずむくわれる。',
        korean: '노력은 반드시 보상받는다.',
      },
    ],
    tags: ['노력', 'JLPT N2'],
  },
  {
    id: 'd004',
    word: '千里',
    reading: 'せんり',
    romaji: 'senri',
    pos: '명사',
    meanings: ['천 리', '매우 먼 거리'],
    examples: [
      {
        japanese: '千里の道も一歩から。',
        reading: 'せんりのみちもいっぽから。',
        korean: '천 리 길도 한 걸음부터.',
      },
    ],
    tags: ['거리', '관용어'],
  },
  {
    id: 'd005',
    word: '汗',
    reading: 'あせ',
    romaji: 'ase',
    pos: '명사',
    meanings: ['땀'],
    examples: [
      {
        japanese: '汗は嘘をつかない。',
        reading: 'あせはうそをつかない。',
        korean: '땀은 거짓말하지 않는다.',
      },
    ],
    tags: ['신체', 'JLPT N4'],
  },
  {
    id: 'd006',
    word: '本気',
    reading: 'ほんき',
    romaji: 'honki',
    pos: '명사',
    meanings: ['진심', '본심', '진지함'],
    examples: [
      {
        japanese: '本気でやれば大抵のことはできる。',
        reading: 'ほんきでやればたいていのことはできる。',
        korean: '진심으로 하면 웬만한 일은 다 할 수 있다.',
      },
    ],
    tags: ['마음', 'JLPT N3'],
  },

  // ── 成功 관련 ──────────────────────────────────────────────────
  {
    id: 'd007',
    word: '成功',
    reading: 'せいこう',
    romaji: 'seikou',
    pos: '명사',
    meanings: ['성공'],
    examples: [
      {
        japanese: '成功の秘訣は、始めることだ。',
        reading: 'せいこうのひけつは、はじめることだ。',
        korean: '성공의 비결은 시작하는 것이다.',
      },
    ],
    tags: ['성공', 'JLPT N4'],
  },
  {
    id: 'd008',
    word: '失敗',
    reading: 'しっぱい',
    romaji: 'shippai',
    pos: '명사',
    meanings: ['실패', '실수'],
    examples: [
      {
        japanese: '失敗は成功のもと。',
        reading: 'しっぱいはせいこうのもと。',
        korean: '실패는 성공의 어머니.',
      },
    ],
    tags: ['성공', 'JLPT N4'],
  },
  {
    id: 'd009',
    word: '秘訣',
    reading: 'ひけつ',
    romaji: 'hiketsu',
    pos: '명사',
    meanings: ['비결', '비법', '요령'],
    examples: [
      {
        japanese: '成功の秘訣は、始めることだ。',
        reading: 'せいこうのひけつは、はじめることだ。',
        korean: '성공의 비결은 시작하는 것이다.',
      },
    ],
    tags: ['성공', 'JLPT N2'],
  },
  {
    id: 'd010',
    word: '目標',
    reading: 'もくひょう',
    romaji: 'mokuhyou',
    pos: '명사',
    meanings: ['목표', '목적', '타깃'],
    examples: [
      {
        japanese: '目標のない努力は、行き先のない船と同じだ。',
        reading: 'もくひょうのないどりょくは、ゆきさきのないふねとおなじだ。',
        korean: '목표 없는 노력은 목적지 없는 배와 같다.',
      },
    ],
    tags: ['성공', 'JLPT N3'],
  },
  {
    id: 'd011',
    word: '勇気',
    reading: 'ゆうき',
    romaji: 'yuuki',
    pos: '명사',
    meanings: ['용기', '담력'],
    examples: [
      {
        japanese: '大切なのは続ける勇気だ。',
        reading: 'たいせつなのはつづけるゆうきだ。',
        korean: '중요한 것은 계속할 용기다.',
      },
    ],
    tags: ['마음', 'JLPT N4'],
  },

  // ── 愛 관련 ──────────────────────────────────────────────────
  {
    id: 'd012',
    word: '愛',
    reading: 'あい',
    romaji: 'ai',
    pos: '명사',
    meanings: ['사랑', '애정'],
    examples: [
      {
        japanese: '愛することは、愛されることより幸福だ。',
        reading: 'あいすることは、あいされることよりこうふくだ。',
        korean: '사랑하는 것은 사랑받는 것보다 행복하다.',
      },
    ],
    tags: ['사랑', 'JLPT N4'],
  },
  {
    id: 'd013',
    word: '幸福',
    reading: 'こうふく',
    romaji: 'koufuku',
    pos: '명사',
    meanings: ['행복', '행운'],
    examples: [
      {
        japanese: '愛することは、愛されることより幸福だ。',
        reading: 'あいすることは、あいされることよりこうふくだ。',
        korean: '사랑하는 것은 사랑받는 것보다 행복하다.',
      },
    ],
    tags: ['마음', 'JLPT N3'],
  },
  {
    id: 'd014',
    word: '理解',
    reading: 'りかい',
    romaji: 'rikai',
    pos: '명사',
    meanings: ['이해', '납득'],
    examples: [
      {
        japanese: '愛は理解から生まれ、理解は愛から生まれる。',
        reading: 'あいはりかいからうまれ、りかいはあいからうまれる。',
        korean: '사랑은 이해에서 태어나고, 이해는 사랑에서 태어난다.',
      },
    ],
    tags: ['사랑', 'JLPT N4'],
  },
  {
    id: 'd015',
    word: '恋',
    reading: 'こい',
    romaji: 'koi',
    pos: '명사',
    meanings: ['연애', '사랑(낭만적)', '그리움'],
    examples: [
      {
        japanese: '恋は盲目、されど愛は真実を見る。',
        reading: 'こいはもうもく、されどあいはしんじつをみる。',
        korean: '연애는 맹목적이지만, 사랑은 진실을 본다.',
      },
    ],
    tags: ['사랑', 'JLPT N3'],
  },

  // ── 人生 관련 ──────────────────────────────────────────────────
  {
    id: 'd016',
    word: '人生',
    reading: 'じんせい',
    romaji: 'jinsei',
    pos: '명사',
    meanings: ['인생', '삶', '생애'],
    examples: [
      {
        japanese: '人生は一度きり。',
        reading: 'じんせいはいちどきり。',
        korean: '인생은 한 번뿐.',
      },
    ],
    tags: ['인생', 'JLPT N4'],
  },
  {
    id: 'd017',
    word: '一期一会',
    reading: 'いちごいちえ',
    romaji: 'ichigo ichie',
    pos: '사자성어',
    meanings: ['일기일회', '일생에 한 번뿐인 만남', '매 순간을 소중히 여기는 마음'],
    examples: [
      {
        japanese: '一期一会。',
        reading: 'いちごいちえ。',
        korean: '일생에 한 번뿐인 만남 — 매 순간을 소중히 여겨라.',
      },
    ],
    tags: ['인생', '사자성어', '다도'],
  },
  {
    id: 'd018',
    word: '幸せ',
    reading: 'しあわせ',
    romaji: 'shiawase',
    pos: '명사',
    meanings: ['행복', '행운', '운'],
    examples: [
      {
        japanese: '幸せは自分の心が決める。',
        reading: 'しあわせはじぶんのこころがきめる。',
        korean: '행복은 자신의 마음이 결정한다.',
      },
    ],
    tags: ['마음', 'JLPT N4'],
  },
  {
    id: 'd019',
    word: '過去',
    reading: 'かこ',
    romaji: 'kako',
    pos: '명사',
    meanings: ['과거'],
    examples: [
      {
        japanese: '過去を変えることはできないが、未来を作ることはできる。',
        reading: 'かこをかえることはできないが、みらいをつくることはできる。',
        korean: '과거를 바꿀 수는 없지만, 미래를 만들 수는 있다.',
      },
    ],
    tags: ['인생', 'JLPT N4'],
  },
  {
    id: 'd020',
    word: '未来',
    reading: 'みらい',
    romaji: 'mirai',
    pos: '명사',
    meanings: ['미래', '장래'],
    examples: [
      {
        japanese: '過去を変えることはできないが、未来を作ることはできる。',
        reading: 'かこをかえることはできないが、みらいをつくることはできる。',
        korean: '과거를 바꿀 수는 없지만, 미래를 만들 수는 있다.',
      },
    ],
    tags: ['인생', 'JLPT N4'],
  },

  // ── 学び 관련 ──────────────────────────────────────────────────
  {
    id: 'd021',
    word: '知識',
    reading: 'ちしき',
    romaji: 'chishiki',
    pos: '명사',
    meanings: ['지식', '아는 것'],
    examples: [
      {
        japanese: '知識は力なり。',
        reading: 'ちしきはちからなり。',
        korean: '지식은 힘이다.',
      },
    ],
    tags: ['학습', 'JLPT N3'],
  },
  {
    id: 'd022',
    word: '温故知新',
    reading: 'おんこちしん',
    romaji: 'onko chishin',
    pos: '사자성어',
    meanings: ['온고지신', '옛것을 익히고 새것을 앎', '고전을 통해 새로운 지혜를 얻음'],
    examples: [
      {
        japanese: '温故知新。',
        reading: 'おんこちしん。',
        korean: '옛것을 익히고 새것을 안다.',
      },
    ],
    tags: ['학습', '사자성어', '공자'],
  },
  {
    id: 'd023',
    word: '好き',
    reading: 'すき',
    romaji: 'suki',
    pos: 'な형용사',
    meanings: ['좋아함', '애호'],
    examples: [
      {
        japanese: '好きこそものの上手なれ。',
        reading: 'すきこそもののじょうずなれ。',
        korean: '좋아하는 것이야말로 잘하게 된다.',
      },
    ],
    tags: ['학습', 'JLPT N5'],
  },
  {
    id: 'd024',
    word: '読書',
    reading: 'どくしょ',
    romaji: 'dokusho',
    pos: '명사',
    meanings: ['독서', '책 읽기'],
    examples: [
      {
        japanese: '読書は脳の運動だ。',
        reading: 'どくしょはのうのうんどうだ。',
        korean: '독서는 두뇌의 운동이다.',
      },
    ],
    tags: ['학습', 'JLPT N4'],
  },
  {
    id: 'd025',
    word: '謙虚',
    reading: 'けんきょ',
    romaji: 'kenkyo',
    pos: 'な형용사',
    meanings: ['겸손', '겸허'],
    examples: [
      {
        japanese: '人は学ぶことで謙虚になる。',
        reading: 'ひとはまなぶことでけんきょになる。',
        korean: '사람은 배움으로 겸손해진다.',
      },
    ],
    tags: ['마음', 'JLPT N2'],
  },

  // ── 心 관련 ──────────────────────────────────────────────────
  {
    id: 'd026',
    word: '心',
    reading: 'こころ',
    romaji: 'kokoro',
    pos: '명사',
    meanings: ['마음', '정신', '心(심)'],
    examples: [
      {
        japanese: '心が変われば行動が変わる。',
        reading: 'こころがかわればこうどうがかわる。',
        korean: '마음이 바뀌면 행동이 바뀐다.',
      },
    ],
    tags: ['마음', 'JLPT N4'],
  },
  {
    id: 'd027',
    word: '感謝',
    reading: 'かんしゃ',
    romaji: 'kansha',
    pos: '명사',
    meanings: ['감사', '고마움'],
    examples: [
      {
        japanese: '感謝の心が幸せを呼ぶ。',
        reading: 'かんしゃのこころがしあわせをよぶ。',
        korean: '감사하는 마음이 행복을 부른다.',
      },
    ],
    tags: ['마음', 'JLPT N3'],
  },
  {
    id: 'd028',
    word: '思いやり',
    reading: 'おもいやり',
    romaji: 'omoiyari',
    pos: '명사',
    meanings: ['배려', '동정심', '타인을 헤아리는 마음'],
    examples: [
      {
        japanese: '思いやりは最強の武器だ。',
        reading: 'おもいやりはさいきょうのぶきだ。',
        korean: '배려는 가장 강력한 무기다.',
      },
    ],
    tags: ['마음', 'JLPT N2'],
  },
  {
    id: 'd029',
    word: '平和',
    reading: 'へいわ',
    romaji: 'heiwa',
    pos: '명사',
    meanings: ['평화', '평온'],
    examples: [
      {
        japanese: '心の平和は、行動の結果から得られる。',
        reading: 'こころのへいわは、こうどうのけっかからえられる。',
        korean: '마음의 평화는 행동의 결과로 얻어진다.',
      },
    ],
    tags: ['마음', 'JLPT N4'],
  },
  {
    id: 'd030',
    word: '微笑み',
    reading: 'ほほえみ',
    romaji: 'hohoemi',
    pos: '명사',
    meanings: ['미소', '방긋 웃음'],
    examples: [
      {
        japanese: '微笑みは世界共通の言語だ。',
        reading: 'ほほえみはせかいきょうつうのげんごだ。',
        korean: '미소는 세계 공통의 언어다.',
      },
    ],
    tags: ['마음', 'JLPT N2'],
  },

  // ── 자주 쓰이는 기본 어휘 ────────────────────────────────────
  {
    id: 'd031',
    word: '言葉',
    reading: 'ことば',
    romaji: 'kotoba',
    pos: '명사',
    meanings: ['말', '언어', '단어', '말씀'],
    examples: [
      {
        japanese: '言葉は心の鏡だ。',
        reading: 'ことばはこころのかがみだ。',
        korean: '말은 마음의 거울이다.',
      },
    ],
    tags: ['언어', 'JLPT N4'],
  },
  {
    id: 'd032',
    word: '宝',
    reading: 'たから',
    romaji: 'takara',
    pos: '명사',
    meanings: ['보물', '보배'],
    examples: [
      {
        japanese: '言葉の宝箱',
        reading: 'ことばのたからばこ',
        korean: '말의 보물 상자',
      },
    ],
    tags: ['기본어휘', 'JLPT N3'],
  },
  {
    id: 'd033',
    word: '道',
    reading: 'みち',
    romaji: 'michi',
    pos: '명사',
    meanings: ['길', '도(道)', '방법', '방향'],
    examples: [
      {
        japanese: '千里の道も一歩から。',
        reading: 'せんりのみちもいっぽから。',
        korean: '천 리 길도 한 걸음부터.',
      },
    ],
    tags: ['기본어휘', 'JLPT N5'],
  },
  {
    id: 'd034',
    word: '力',
    reading: 'ちから',
    romaji: 'chikara',
    pos: '명사',
    meanings: ['힘', '능력', '세기'],
    examples: [
      {
        japanese: '継続は力なり。',
        reading: 'けいぞくはちからなり。',
        korean: '지속은 힘이다.',
      },
    ],
    tags: ['기본어휘', 'JLPT N5'],
  },
  {
    id: 'd035',
    word: '夢',
    reading: 'ゆめ',
    romaji: 'yume',
    pos: '명사',
    meanings: ['꿈', '희망'],
    examples: [
      {
        japanese: 'あなたの夢が本物なら、それは実現可能だ。',
        reading: 'あなたのゆめがほんものなら、それはじつげんかのうだ。',
        korean: '당신의 꿈이 진짜라면 그것은 실현 가능하다.',
      },
    ],
    tags: ['성공', 'JLPT N4'],
  },
  {
    id: 'd036',
    word: '希望',
    reading: 'きぼう',
    romaji: 'kibou',
    pos: '명사',
    meanings: ['희망', '바람', '기대'],
    examples: [
      {
        japanese: '希望を持ち続けることが大切だ。',
        reading: 'きぼうをもちつづけることがたいせつだ。',
        korean: '희망을 계속 갖는 것이 중요하다.',
      },
    ],
    tags: ['마음', 'JLPT N4'],
  },
  {
    id: 'd037',
    word: '勉強',
    reading: 'べんきょう',
    romaji: 'benkyou',
    pos: '명사',
    meanings: ['공부', '학습', '노력'],
    examples: [
      {
        japanese: '人は死ぬまで勉強である。',
        reading: 'ひとはしぬまでべんきょうである。',
        korean: '사람은 죽을 때까지 배우는 존재다.',
      },
    ],
    tags: ['학습', 'JLPT N5'],
  },
  {
    id: 'd038',
    word: '習慣',
    reading: 'しゅうかん',
    romaji: 'shuukan',
    pos: '명사',
    meanings: ['습관', '버릇'],
    examples: [
      {
        japanese: '習慣が変われば人格が変わる。',
        reading: 'しゅうかんがかわればじんかくがかわる。',
        korean: '습관이 바뀌면 인격이 바뀐다.',
      },
    ],
    tags: ['마음', 'JLPT N3'],
  },
  {
    id: 'd039',
    word: '行動',
    reading: 'こうどう',
    romaji: 'koudou',
    pos: '명사',
    meanings: ['행동', '행위', '행보'],
    examples: [
      {
        japanese: '心が変われば行動が変わる。',
        reading: 'こころがかわればこうどうがかわる。',
        korean: '마음이 바뀌면 행동이 바뀐다.',
      },
    ],
    tags: ['마음', 'JLPT N3'],
  },
  {
    id: 'd040',
    word: '運命',
    reading: 'うんめい',
    romaji: 'unmei',
    pos: '명사',
    meanings: ['운명', '숙명'],
    examples: [
      {
        japanese: '人格が変われば運命が変わる。',
        reading: 'じんかくがかわればうんめいがかわる。',
        korean: '인격이 바뀌면 운명이 바뀐다.',
      },
    ],
    tags: ['인생', 'JLPT N3'],
  },
  {
    id: 'd041',
    word: '忍耐',
    reading: 'にんたい',
    romaji: 'nintai',
    pos: '명사',
    meanings: ['인내', '참을성', '끈기'],
    examples: [
      {
        japanese: '忍耐は美徳だ。',
        reading: 'にんたいはびとくだ。',
        korean: '인내는 미덕이다.',
      },
    ],
    tags: ['노력', 'JLPT N2'],
  },
  {
    id: 'd042',
    word: '才能',
    reading: 'さいのう',
    romaji: 'sainou',
    pos: '명사',
    meanings: ['재능', '재주', '천부적인 능력'],
    examples: [
      {
        japanese: '才能とは、燃え上がる知的好奇心である。',
        reading: 'さいのうとは、もえあがるちてきこうきしんである。',
        korean: '재능이란 불타오르는 지적 호기심이다.',
      },
    ],
    tags: ['학습', 'JLPT N3'],
  },
  {
    id: 'd043',
    word: '笑う',
    reading: 'わらう',
    romaji: 'warau',
    pos: '동사',
    meanings: ['웃다', '비웃다'],
    examples: [
      {
        japanese: '笑う門には福来る。',
        reading: 'わらうかどにはふくきたる。',
        korean: '웃는 집에 복이 온다.',
      },
    ],
    tags: ['인생', 'JLPT N4'],
  },
  {
    id: 'd044',
    word: '福',
    reading: 'ふく',
    romaji: 'fuku',
    pos: '명사',
    meanings: ['복', '행운', '행복'],
    examples: [
      {
        japanese: '笑う門には福来る。',
        reading: 'わらうかどにはふくきたる。',
        korean: '웃는 집에 복이 온다.',
      },
    ],
    tags: ['인생', 'JLPT N3'],
  },
  {
    id: 'd045',
    word: '沈黙',
    reading: 'ちんもく',
    romaji: 'chinmoku',
    pos: '명사',
    meanings: ['침묵', '고요함', '말 없음'],
    examples: [
      {
        japanese: '沈黙は金、雄弁は銀。',
        reading: 'ちんもくはきん、ゆうべんはぎん。',
        korean: '침묵은 금, 웅변은 은.',
      },
    ],
    tags: ['마음', 'JLPT N2'],
  },
];

export default dictionary;

export function searchDictionary(query: string): DictEntry[] {
  if (!query.trim()) return [];
  const q = query.trim().toLowerCase();
  return dictionary.filter(
    entry =>
      entry.word.includes(q) ||
      entry.reading.includes(q) ||
      entry.romaji.toLowerCase().includes(q) ||
      entry.meanings.some(m => m.includes(q)),
  );
}
