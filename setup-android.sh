#!/usr/bin/env bash
# ────────────────────────────────────────────────
#  言葉の宝箱 — Android 개발 환경 세팅 스크립트
#  사용법: bash setup-android.sh
# ────────────────────────────────────────────────
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ANDROID_DIR="$SCRIPT_DIR/android"

echo ""
echo "🏯  言葉の宝箱 Android 세팅 시작..."
echo ""

# ── 1. Node.js 확인 ─────────────────────────────
if ! command -v node &>/dev/null; then
  echo "❌  Node.js가 설치되어 있지 않습니다."
  echo "    https://nodejs.org/ 에서 설치 후 다시 실행하세요."
  exit 1
fi
echo "✅  Node.js $(node -v)"

# ── 2. npm install ───────────────────────────────
if [ ! -d "$SCRIPT_DIR/node_modules" ]; then
  echo ""
  echo "📦  npm install 실행 중..."
  cd "$SCRIPT_DIR" && npm install
  echo "✅  패키지 설치 완료"
else
  echo "✅  node_modules 이미 존재"
fi

# ── 3. local.properties 생성 ─────────────────────
LOCAL_PROPS="$ANDROID_DIR/local.properties"
if [ ! -f "$LOCAL_PROPS" ]; then
  echo ""
  echo "🔧  android/local.properties 생성 중..."

  # Android SDK 경로 자동 감지
  if [ -n "$ANDROID_HOME" ]; then
    SDK_DIR="$ANDROID_HOME"
  elif [ -d "$HOME/Library/Android/sdk" ]; then
    SDK_DIR="$HOME/Library/Android/sdk"         # macOS
  elif [ -d "$HOME/Android/Sdk" ]; then
    SDK_DIR="$HOME/Android/Sdk"                  # Linux
  elif [ -d "%LOCALAPPDATA%\\Android\\Sdk" ]; then
    SDK_DIR="%LOCALAPPDATA%\\Android\\Sdk"       # Windows (fallback)
  else
    SDK_DIR="YOUR_ANDROID_SDK_PATH"
    echo "⚠️   SDK 경로를 자동으로 찾지 못했습니다."
    echo "    android/local.properties 파일에서 sdk.dir을 직접 수정하세요."
  fi

  echo "sdk.dir=$SDK_DIR" > "$LOCAL_PROPS"
  echo "✅  local.properties 생성: sdk.dir=$SDK_DIR"
else
  echo "✅  android/local.properties 이미 존재"
fi

# ── 4. gradlew 실행 권한 ─────────────────────────
chmod +x "$ANDROID_DIR/gradlew"
echo "✅  gradlew 실행 권한 설정 완료"

# ── 완료 안내 ────────────────────────────────────
echo ""
echo "═══════════════════════════════════════════"
echo "  ✨  세팅 완료!"
echo ""
echo "  Android Studio에서 실행하는 방법:"
echo "  1. Android Studio → File → Open"
echo "     → $(basename "$SCRIPT_DIR")/android 폴더 선택"
echo "  2. Gradle Sync 완료 대기"
echo "  3. 터미널에서 Metro 번들러 실행:"
echo "     cd $(basename "$SCRIPT_DIR") && npm start"
echo "  4. Android Studio에서 ▶ Run 클릭"
echo ""
echo "  또는 터미널에서 바로 실행:"
echo "     cd $(basename "$SCRIPT_DIR") && npm run android"
echo "═══════════════════════════════════════════"
echo ""
