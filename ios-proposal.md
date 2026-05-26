# 言葉の宝箱 iOS 개발 제안서

**작성일:** 2026-05-26  
**대상:** Android 앱(com.kotoba.takarabako) 동등 기능 iOS 포팅

---

## 1. 프로젝트 개요

### 앱 소개
일본어 학습자(주 타깃: 한국인)를 위한 JLPT 단어 + 일본어 명언 학습 앱.  
Android와 완전히 동일한 기능·UI를 iOS(iPhone/iPad)에서 제공한다.

### 포팅 범위
| 항목 | Android | iOS |
|------|---------|-----|
| 화면 수 | 5개 | 5개 (동일) |
| 데이터 | JSON (assets) | JSON (Bundle) |
| JLPT 단어 | N1~N5 각 250~376개 | 동일 파일 재사용 |
| 명언 | 237개 / 7카테고리 | 동일 |
| 광고 | AdMob 배너 | Google Mobile Ads SDK |
| 알림 | AlarmManager | UNUserNotificationCenter |
| 저장소 | Room + DataStore | SwiftData + UserDefaults |

---

## 2. iOS 기술 스택

### 언어 / 프레임워크
| 역할 | 선택 | Android 대응 |
|------|------|-------------|
| 언어 | **Swift 6** | Kotlin |
| UI | **SwiftUI** | Jetpack Compose |
| 상태 관리 | `@Observable` + `@State` | StateFlow + ViewModel |
| 내비게이션 | SwiftUI `NavigationStack` + `TabView` | Jetpack Navigation Compose |
| DB (즐겨찾기) | **SwiftData** | Room |
| 설정 저장 | **UserDefaults** (AppStorage) | DataStore Preferences |
| JSON 파싱 | `Codable` | Gson |
| 알림 | `UNUserNotificationCenter` | AlarmManager + BroadcastReceiver |
| 광고 | **Google Mobile Ads SDK (iOS)** | Google Mobile Ads SDK (Android) |
| 네트워크 | `URLSession` | Java URLConnection |
| 폰트 | Noto Serif JP (.ttf → 동일 파일 재사용) | Noto Serif JP |
| 비동기 | Swift Concurrency (`async/await`) | Coroutines |

### 최소 사양
- **iOS 17+** (SwiftData, `@Observable` 사용 조건)
- **Xcode 16+**

---

## 3. 화면별 상세 명세

### 3-1. HomeScreen
- 앱 타이틀 헤더 + 상태 표시 점(초록)
- **오늘의 명언 카드**: 날짜 기반으로 deterministic 선택 (dayOfYear % total)
  - 한자 표시 → 후리가나 토글 → 한국어 번역 토글 (StepBlock)
- AdMob 배너 광고 (하단 50px 고정)
- **탭 스위처**: 명언 카테고리 / JLPT 레벨
- 카테고리 그리드 (2열):
  - 명언: 전체 / 노력 / 성공 / 사랑 / 인생 / 학습 / 마음 / 기타
  - JLPT: 전체 / N1 / N2 / N3 / N4 / N5 (색상 뱃지)

### 3-2. QuoteCardScreen
- 카드 슬라이드 학습 화면
- 진행도 표시 (X / Y)
- 카드 내용: 카테고리 뱃지 → 한자 명언 → 후리가나 → 한국어 번역 → 출처
- **좌우 탭 네비게이션** (좌측 탭=이전, 우측 탭=다음)
- 즐겨찾기(하트) 버튼
- 텍스트 선택 → "사전 검색" (한국어 사전 앱 연동)
- **자동 재생**: 5초마다 자동 이동
- **자동 블러 해제**: 3초 후 후리가나 → 3초 후 번역 순차 공개

### 3-3. JlptScreen
- JLPT 단어 카드 학습
- 상단 레벨 필터 탭 (전체 / N5~N1, 색상 구분)
- 카드: 레벨 뱃지 + 품사 뱃지 + 하트 / 한자어 / 후리가나 / 한국어 의미
- 예문 박스: 한자+후리가나 / 한국어 번역
- QuoteCardScreen과 동일한 좌우 탭 네비게이션

### 3-4. FavoritesScreen
- 탭: "명언 (X)" / "단어 (X)"
- 명언 즐겨찾기 리스트: 카드형, 카테고리 뱃지, 하트로 삭제
- 단어 즐겨찾기 리스트: 컴팩트 카드, 한자/읽기/뜻

### 3-5. SettingsScreen
- **테마 선택** (4종): 골드 다크 / 벚꽃 다크 / 오션 다크 / 화지 라이트
- **폰트 크기** (4단계): 작게 0.85× / 보통 1.0× / 크게 1.2× / 매우 크게 1.4×
- **데이터 새로고침**: GitHub CDN에서 최신 JSON 다운로드
- **자동 재생 토글**
- **즐겨찾기 내보내기**: iOS Share Sheet 연동
- **학습기록**: N일 연속 접속 표시
- **알림 토글 + 시간 설정** (DatePicker)
- **블러 자동 해제 토글**

---

## 4. 아키텍처 설계

```
┌────────────────────────────────────────────────┐
│                  SwiftUI Views                  │
│  HomeView  QuoteCardView  JlptView  ...        │
└───────────────────┬────────────────────────────┘
                    │ @Observable
┌───────────────────▼────────────────────────────┐
│                 ViewModels                      │
│  HomeVM  QuoteVM  JlptVM  FavVM  SettingsVM    │
└───────────────────┬────────────────────────────┘
                    │
┌───────────────────▼────────────────────────────┐
│               Repositories                     │
│  QuoteRepository      WordRepository           │
│  (in-memory cache + remote fetch)              │
└──────┬─────────────────────────────┬───────────┘
       │                             │
┌──────▼───────┐            ┌────────▼──────────┐
│  SwiftData   │            │  Bundle JSON /     │
│  (Favorites) │            │  FileManager cache │
└──────────────┘            └───────────────────┘
```

### Android → iOS 아키텍처 매핑
| Android | iOS |
|---------|-----|
| `@Composable` | `View` (SwiftUI) |
| `StateFlow` | `@Published` / `@Observable` |
| `AndroidViewModel` | `@Observable class ViewModel` |
| `Room` | `SwiftData` |
| `DataStore` | `UserDefaults` + `@AppStorage` |
| `CompositionLocal` | `SwiftUI Environment` |
| `LaunchedEffect` | `.task {}` modifier |
| `coroutineScope` | `Task {}` / `async/await` |

---

## 5. 핵심 구현 포인트

### 5-1. 후리가나 렌더링
Android에서 `FuriganaText` 컴포넌트로 세그먼트 기반 렌더링.  
iOS는 동일 구조를 SwiftUI로 구현:
```swift
// 각 세그먼트를 (한자, 읽기) 쌍으로 HStack 배치
// ruby text 대신 VStack(루비 위, 한자 아래) 방식 사용
struct FuriganaText: View {
    let segments: [Segment]
    // ...
}
```

### 5-2. 테마 시스템
`EnvironmentObject` 또는 `@Environment` 커스텀 키로  
Android `LocalAppColors`와 동일하게 전역 색상 주입.

```swift
struct AppColors {
    var background: Color
    var accent: Color
    var surface: Color
    // ... 15가지 색상 속성
}
```

### 5-3. 사전 검색 (텍스트 선택)
iOS에서는 `UITextView`를 SwiftUI `UIViewRepresentable`로 래핑하거나  
커스텀 `TextEditor` + context menu로 "사전 검색" 액션 구현.  
검색 실행: `UIApplication.shared.open(URL(string: "dict://\(query)")!)`  
또는 네이버 사전 딥링크.

### 5-4. 알림 스케줄링
```swift
// Android AlarmManager 대응
UNUserNotificationCenter.current().add(request)
// 매일 특정 시간 반복 trigger
UNCalendarNotificationTrigger(dateMatching: components, repeats: true)
```

### 5-5. 데이터 새로고침
Android와 동일한 GitHub CDN URL 사용.  
`URLSession.shared.data(from: url)` → `FileManager` 로컬 저장.

### 5-6. AdMob 배너
`GADBannerView`를 `UIViewRepresentable`로 래핑하여 SwiftUI에서 사용.  
기존 AdMob 계정의 iOS용 광고 단위 ID 추가 필요.

### 5-7. 자동 재생 / 자동 블러
`Timer.publish(every: 5, on: .main)` + `.onReceive` 조합으로  
Android `LaunchedEffect + delay()` 로직과 동일하게 구현.

---

## 6. 데이터 파일 전략

기존 Android 프로젝트의 JSON 파일을 **그대로 재사용**.

```
iOS Xcode Project
└── Resources/
    └── data/
        ├── quotes.json        (237개)
        ├── words_n5.json      (376개)
        ├── words_n4.json      (281개)
        ├── words_n3.json      (280개)
        ├── words_n2.json      (280개)
        └── words_n1.json      (249개)
```

JSON 구조가 동일하므로 Swift `Codable`로 디코딩:
```swift
struct Quote: Codable, Identifiable {
    let id: String
    let cat: String
    let kanji: String
    let segments: [Segment]
    let korean: String
    let author: String
}
```

---

## 7. 개발 단계

### Phase 1 — 기반 구축 (1~2주)
- Xcode 프로젝트 생성, 폴더 구조 세팅
- JSON 데이터 파일 import + Codable 모델 정의
- SwiftData 스키마 (FavoriteEntity)
- 테마 시스템 (`AppColors`, `@Environment` 연결)
- 폰트 (Noto Serif JP) 임포트
- 공통 컴포넌트: `FuriganaText`, `StepBlock`, `HeartButton`

### Phase 2 — 핵심 화면 (2~3주)
- `HomeView` (오늘의 명언 + 카테고리 그리드)
- `QuoteCardView` (명언 카드 학습 + 탭 네비게이션)
- `JlptView` (단어 카드 학습 + 레벨 필터)
- Bottom `TabView` 내비게이션

### Phase 3 — 부가 기능 (1~2주)
- `FavoritesView` (즐겨찾기 목록)
- `SettingsView` (모든 설정 항목)
- 알림 스케줄링 (UNUserNotificationCenter)
- 데이터 새로고침 (URLSession + FileManager)
- 로그인 연속 기록 (UserDefaults)

### Phase 4 — 광고 / 마무리 (1주)
- AdMob SDK 통합 + 배너 광고
- 사전 검색 텍스트 선택 구현
- 자동 재생 / 자동 블러 구현
- 전체 기기 테스트 (iPhone SE ~ iPhone 16 Pro Max, iPad)
- App Store 심사 준비 (아이콘, 스크린샷, 메타데이터)

**예상 총 기간: 5~8주** (1인 개발 기준)

---

## 8. 주의사항 / 리스크

| 항목 | 내용 |
|------|------|
| **App Store 심사** | 알림 권한, 광고 정책 준수 필수 |
| **후리가나 레이아웃** | iOS에 네이티브 ruby text 없음 → 커스텀 레이아웃 필요 |
| **AdMob iOS 단위 ID** | Android용과 별도 발급 필요 |
| **데이터 새로고침 URL** | branch URL 변경 시 Android/iOS 동시 반영 필요 |
| **iPad 대응** | SwiftUI는 기본 반응형이나 레이아웃 검증 필요 |
| **Dynamic Type** | 시스템 폰트 크기 설정과 앱 내 폰트 스케일 충돌 가능성 |

---

## 9. 파일/폴더 구조 (예상)

```
KotobaNoTakarabako-iOS/
├── App/
│   ├── KotobaApp.swift
│   └── ContentView.swift          # TabView 진입점
├── Views/
│   ├── Home/
│   │   └── HomeView.swift
│   ├── Quote/
│   │   └── QuoteCardView.swift
│   ├── Jlpt/
│   │   └── JlptView.swift
│   ├── Favorites/
│   │   └── FavoritesView.swift
│   └── Settings/
│       └── SettingsView.swift
├── ViewModels/
│   ├── HomeViewModel.swift
│   ├── QuoteViewModel.swift
│   ├── JlptViewModel.swift
│   ├── FavoritesViewModel.swift
│   └── SettingsViewModel.swift
├── Models/
│   ├── Quote.swift
│   └── Word.swift
├── Data/
│   ├── Repository/
│   │   ├── QuoteRepository.swift
│   │   └── WordRepository.swift
│   └── Local/
│       ├── FavoriteItem.swift      # SwiftData 모델
│       └── DataStoreManager.swift  # UserDefaults 래퍼
├── Components/
│   ├── FuriganaText.swift
│   ├── StepBlock.swift
│   ├── HeartButton.swift
│   └── AdBannerView.swift
├── Theme/
│   ├── AppColors.swift
│   └── AppTheme.swift
├── Util/
│   └── NotificationHelper.swift
└── Resources/
    └── data/
        ├── quotes.json
        ├── words_n1.json
        ├── words_n2.json
        ├── words_n3.json
        ├── words_n4.json
        └── words_n5.json
```

---

## 10. Android 대비 변경 없는 항목

- JSON 데이터 파일 (완전 재사용)
- 테마 색상값 (HEX 그대로 이전)
- 데이터 새로고침 URL
- 후리가나 세그먼트 구조
- 카테고리/레벨 분류 체계
- 비즈니스 로직 (streak, today's quote 선택 알고리즘)
