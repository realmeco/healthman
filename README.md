# 헬스맨 · Healthman

Android 8.0(API 26) 이상에서 동작하는 오프라인 헬스 보조 앱의 첫 버전입니다. Java와 Android 기본 UI를 사용합니다. 계정이나 서버 연결 없이 기기에 데이터를 저장합니다.

## 기능

- 홈: 선택한 날짜의 운동 완료 수와 기록한 식단 열량
- 운동 루틴: 요일별 운동 이름, 세트와 횟수 추가·삭제, 날짜별 완료 체크
- 식단: 날짜별 아침·점심·저녁·간식과 음식·양·열량 입력 및 삭제
- 기구 가이드: 레그 프레스, 랫 풀다운, 체스트 프레스, 시티드 로우, 러닝머신 사용 순서·주의사항과 검색

식단 열량은 수동 입력입니다. 자동 추천, 영양 DB, 동영상, 로그인, 클라우드 동기화는 아직 포함하지 않습니다. 앱 데이터 삭제 또는 제거 시 기록도 삭제됩니다.

## 개발 및 빌드

Android Studio에서 이 폴더를 열고 SDK 35와 Build Tools 35.0.0을 설치합니다. JDK 17 이상(클라우드 검증: JDK 21)을 사용합니다.

```sh
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

SDK 위치는 추적하지 않는 `local.properties`의 `sdk.dir` 또는 `ANDROID_HOME`으로 지정합니다. APK는 `app/build/outputs/apk/debug/app-debug.apk`에 생성됩니다.

연결된 Android 기기나 에뮬레이터에서:

```sh
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb shell am start -n com.healthman.app/.MainActivity
```

## 기기 검증 항목

1. 운동을 추가하고 완료 체크 후 다른 날짜에는 체크가 분리되는지 확인합니다.
2. 같은 요일의 다음 주에는 계획이 유지되는지 확인합니다.
3. 날짜별 식단을 추가해 열량 합계와 날짜 분리를 확인합니다.
4. 앱을 종료·재실행해 저장된 기록이 유지되는지 확인합니다.
5. 기구 검색·상세 안내, 빈 입력·범위 밖 숫자 거절, 삭제 취소를 확인합니다.

기구 안내는 일반 참고 정보이며 실제 기구의 안내문과 전문가의 지도를 우선합니다.

이 클라우드 환경에서는 준비된 도구를 다음과 같이 활성화합니다.

```sh
source /workspace/tooling/healthman-env.sh
cd /workspace/healthman
./gradlew --no-daemon :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

실제 기기나 에뮬레이터가 없는 클라우드에서는 화면 조작 검증을 수행할 수 없습니다. 위의 기기 검증 항목을 별도로 실행해야 합니다.

현재 클라우드의 `adb`는 읽기 전용 `/home/agent/.android`에 접근하다 중단됩니다. 기기 설치·실행 명령은 Android Studio가 설정된 로컬 환경에서 실행하세요. 클라우드에서는 APK 빌드, 입력 검증 단위 테스트, lint까지 검증했습니다.
