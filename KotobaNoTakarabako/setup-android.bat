@echo off
REM ────────────────────────────────────────────────
REM  言葉の宝箱 — Android 개발 환경 세팅 스크립트 (Windows)
REM  사용법: setup-android.bat
REM ────────────────────────────────────────────────
setlocal

echo.
echo 🏯  言葉の宝箱 Android 세팅 시작 (Windows)...
echo.

REM ── 1. Node.js 확인 ─────────────────────────────
where node >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Node.js가 설치되어 있지 않습니다.
    echo         https://nodejs.org/ 에서 설치 후 다시 실행하세요.
    pause
    exit /b 1
)
for /f "tokens=*" %%v in ('node -v') do echo [OK] Node.js %%v

REM ── 2. npm install ───────────────────────────────
if not exist "node_modules\" (
    echo.
    echo [INFO] npm install 실행 중...
    npm install
    echo [OK] 패키지 설치 완료
) else (
    echo [OK] node_modules 이미 존재
)

REM ── 3. local.properties 생성 ─────────────────────
if not exist "android\local.properties" (
    echo.
    echo [INFO] android\local.properties 생성 중...
    if defined ANDROID_HOME (
        echo sdk.dir=%ANDROID_HOME%> android\local.properties
        echo [OK] SDK 경로: %ANDROID_HOME%
    ) else if exist "%LOCALAPPDATA%\Android\Sdk" (
        echo sdk.dir=%LOCALAPPDATA%\Android\Sdk> android\local.properties
        echo [OK] SDK 경로: %LOCALAPPDATA%\Android\Sdk
    ) else (
        echo sdk.dir=C:\Users\%USERNAME%\AppData\Local\Android\Sdk> android\local.properties
        echo [WARNING] SDK 경로를 확인하고 android\local.properties를 수정하세요.
    )
) else (
    echo [OK] android\local.properties 이미 존재
)

REM ── 완료 안내 ────────────────────────────────────
echo.
echo ═══════════════════════════════════════════
echo   세팅 완료!
echo.
echo   Android Studio에서 실행하는 방법:
echo   1. Android Studio - File - Open
echo      - KotobaNoTakarabako\android 폴더 선택
echo   2. Gradle Sync 완료 대기
echo   3. 새 터미널에서 Metro 실행:
echo      npm start
echo   4. Android Studio에서 Run 클릭
echo.
echo   또는 터미널에서:
echo      npm run android
echo ═══════════════════════════════════════════
echo.
pause
