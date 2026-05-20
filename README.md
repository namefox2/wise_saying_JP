# 言葉の宝箱 — 일본어 명언 앱

일본어 명언을 카테고리별로 학습하고, JLPT 단어 카드와 사전 기능을 제공하는 React Native 앱입니다.

---

## Android Studio에서 실행하기

### 1단계 — 저장소 클론

```bash
git clone <repo-url>
cd wise_saying_JP
```

### 2단계 — 세팅 스크립트 실행 (자동 설정)

**macOS / Linux:**
```bash
cd KotobaNoTakarabako
bash setup-android.sh
```

**Windows:**
```
KotobaNoTakarabako\setup-android.bat 더블클릭
```

스크립트가 자동으로:
- `npm install` 실행 (Gradle이 React Native 플러그인을 찾는 데 필요)
- `android/local.properties` 생성 (Android SDK 경로 자동 감지)

### 3단계 — Android Studio에서 열기

> ⚠️ **반드시 `KotobaNoTakarabako/android` 폴더를 열어야 합니다**
> (`wise_saying_JP/` 나 `KotobaNoTakarabako/` 를 열면 `app` 모듈이 보이지 않습니다)

```
Android Studio → File → Open
→ KotobaNoTakarabako/android  ← 이 폴더 선택
```

Gradle Sync가 자동으로 시작됩니다. 완료 후 `app` 모듈이 나타납니다.

### 4단계 — Metro 번들러 실행

Android Studio와 **별도 터미널**에서:

```bash
cd KotobaNoTakarabako
npm start
```

### 5단계 — 앱 실행

Android Studio에서 에뮬레이터 또는 연결된 기기 선택 후 ▶ **Run** 클릭

---

## 터미널에서만 실행하기 (Android Studio 불필요)

```bash
cd KotobaNoTakarabako
npm install
npm run android   # Metro + 빌드 + 설치까지 자동
```

---

## local.properties 수동 설정

`setup-android.sh` 대신 수동 설정이 필요한 경우:

```bash
cp android/local.properties.example android/local.properties
# 파일을 열어서 sdk.dir 경로를 수정
```

| OS | 기본 Android SDK 경로 |
|---|---|
| macOS | `/Users/사용자/Library/Android/sdk` |
| Linux | `~/Android/Sdk` |
| Windows | `C:\Users\사용자\AppData\Local\Android\Sdk` |

---

## 브랜치 구성

| 브랜치 | 설명 |
|---|---|
| `claude/japanese-quotes-app-dC3eg` | 메인 기능 개발 |
| `claude/android-build` | Android 전용 빌드 설정 |

## 기술 스택

- React Native 0.85.3 (TypeScript)
- Zustand (상태 관리) + AsyncStorage (영속화)
- React Navigation (네비게이션)