# MoodLog Android (Jetpack Compose)

Flutter 프로젝트에서 Android Jetpack Compose로 마이그레이션된 무드 트래킹 앱입니다.

## 📱 앱 개요

MoodLog는 사용자의 일상 감정을 기록하고 분석할 수 있는 무드 트래킹 앱입니다. 감정 상태, 일기, 위치 정보, 날씨 등을 종합하여 개인의 감정 패턴을 분석합니다.

## 🏗️ 아키텍처

### Clean Architecture + MVVM 패턴
```
app/
├── domain/           # 비즈니스 로직
│   ├── entities/     # 도메인 엔티티
│   ├── repositories/ # Repository 인터페이스  
│   ├── usecases/     # Use Cases
│   └── common/       # 공통 클래스 (Result 등)
├── data/            # 데이터 계층
│   ├── database/    # Room Database
│   │   ├── entities/    # Room 엔티티
│   │   ├── dao/         # DAO 인터페이스
│   │   └── converters/  # Type Converters
│   ├── repositories/    # Repository 구현체
│   └── mappers/         # Entity ↔ Domain 매퍼
├── presentation/    # UI 계층
│   ├── home/        # 홈 화면
│   ├── navigation/  # 네비게이션
│   └── ui/theme/    # UI 테마
└── di/              # 의존성 주입 모듈
```

## 🛠️ 기술 스택

### 핵심 기술
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose + Material3
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Database**: Room
- **Navigation**: Navigation Compose
- **Async**: Coroutines + Flow/StateFlow
- **Serialization**: Kotlinx Serialization

### 주요 라이브러리
```toml
# UI
androidx-compose-bom = "2024.08.00"
androidx-material3 = { ... }
androidx-activity-compose = "1.9.1"

# Architecture
hilt-android = "2.51.1"
androidx-lifecycle-viewmodel-compose = "2.8.4"
androidx-navigation-compose = "2.7.7"

# Database
room-runtime = "2.6.1" 
room-ktx = "2.6.1"

# Network (향후 사용)
retrofit = "2.11.0"
okhttp = "4.12.0"

# Image Loading
coil-compose = "2.7.0"

# Firebase (향후 사용)
firebase-bom = "33.1.2"
```

## ✅ 구현 완료 기능

### 1. 프로젝트 기초 설정
- [x] Gradle 설정 (Version Catalog 사용)
- [x] Hilt 의존성 주입 설정
- [x] Room Database 설정
- [x] Navigation Compose 설정
- [x] Material3 테마 설정

### 2. Domain Layer (도메인 계층)
- [x] **엔티티 클래스**
  - `MoodType` - 감정 타입 (매우좋음~매우나쁨, 이모지, 점수, 색상)
  - `Journal` - 일기 엔티티 (내용, 감정, 이미지, 위치, 날씨, 태그)
  - `Tag` - 태그 엔티티
  - `Settings` - 설정 엔티티 (테마, 언어, 폰트 등)
  - `LocationInfo`, `WeatherInfo` - 위치/날씨 정보
  - Enum 클래스들 (`LanguageCode`, `ColorTheme`, `FontFamily` 등)

- [x] **Repository 인터페이스**
  - `JournalRepository` - 일기 CRUD 및 스트림
  - `TagRepository` - 태그 관리
  - `AuthRepository` - 인증 (Firebase)
  - `SettingsRepository` - 설정 관리
  - `LocationRepository`, `WeatherRepository` - 위치/날씨

- [x] **Use Cases**
  - `JournalUseCase` - 일기 비즈니스 로직
  - `TagUseCase` - 태그 관리 로직
  - `AuthUseCase` - 인증 로직

- [x] **공통 클래스**
  - `Result<T>` - 성공/실패 래퍼 클래스

### 3. Data Layer (데이터 계층)
- [x] **Room Database**
  - `MoodLogDatabase` - 메인 데이터베이스 클래스
  - `JournalEntity` - 일기 테이블 엔티티
  - `TagEntity` - 태그 테이블 엔티티
  - `JournalTagCrossRef` - 일기-태그 다대다 관계
  - Type Converters (LocalDateTime, MoodType, List<String>)

- [x] **DAO (Data Access Object)**
  - `JournalDao` - 일기 CRUD, 날짜별/월별 조회
  - `TagDao` - 태그 CRUD, 일기별 태그 조회

- [x] **Repository 구현체**
  - `JournalRepositoryImpl` - Room 기반 일기 repository
  - `TagRepositoryImpl` - Room 기반 태그 repository

- [x] **매퍼 클래스**
  - Entity ↔ Domain 모델 변환

### 4. Presentation Layer (UI 계층)
- [x] **홈 화면 (HomeScreen)**
  - 월간 캘린더 뷰 (일기 있는 날짜 표시)
  - 선택된 날짜의 일기 목록
  - 감정 이모지 및 시간 표시
  - 일기 삭제 기능
  - FAB로 일기 작성 화면 이동

- [x] **HomeViewModel**
  - StateFlow 기반 상태 관리
  - 날짜별 일기 로딩
  - 월간 일기 데이터 관리
  - 일기 삭제 처리

- [x] **네비게이션**
  - Navigation Compose 설정
  - 화면 간 이동 관리
  - 기본 라우팅 구조

- [x] **의존성 주입**
  - `DatabaseModule` - Room Database 제공
  - `RepositoryModule` - Repository 바인딩

## 🚧 구현 예정 기능

### 1. Write Screen (일기 작성 화면)
- [ ] **기본 UI 구성**
  - 감정 선택 슬라이더 (MoodType)
  - 텍스트 입력 필드
  - 이미지 첨부 기능
  - 태그 선택/생성
  - 저장/취소 버튼

- [ ] **고급 기능**
  - 위치 정보 자동 수집
  - 날씨 정보 자동 수집
  - AI 응답 생성 (Firebase AI)
  - 이미지 리사이징 및 압축

- [ ] **WriteViewModel**
  - 폼 상태 관리
  - 유효성 검사
  - 이미지 처리
  - 일기 저장 로직

### 2. Statistics Screen (통계 화면)
- [ ] **차트 구현**
  - 감정 추이 라인 차트 (fl_chart 대신 Compose 차트)
  - 월간/주간 감정 분포
  - 태그별 통계
  - 대표 감정 계산

- [ ] **통계 데이터**
  - 최근 30일 감정 평균
  - 가장 많이 사용한 감정
  - 연속 기록일 카운트
  - 태그 사용 빈도

### 3. Settings Screen (설정 화면)
- [ ] **테마 설정**
  - 라이트/다크/시스템 테마
  - 컬러 테마 선택 (19가지)
  - 폰트 패밀리 선택

- [ ] **언어 설정**
  - 9개 언어 지원 (한국어, 영어, 일본어 등)
  - strings.xml 다국어 리소스

- [ ] **기타 설정**
  - 알림 설정
  - 자동 동기화 설정
  - AI 페르소나 선택 (이성적/균형/공감)
  - 텍스트 정렬 설정

### 4. Auth Screen (인증 화면)
- [ ] **Firebase Auth 연동**
  - Google 로그인
  - 익명 로그인
  - 프로필 관리

- [ ] **사용자 상태 관리**
  - 로그인 상태 감지
  - 프로필 이미지 업데이트
  - 닉네임 관리

### 5. Data Layer 확장
- [ ] **Network Layer**
  - Retrofit + OkHttp 설정
  - 날씨 API 연동 (OpenWeatherMap)
  - Firebase Firestore 연동

- [ ] **Repository 구현체 추가**
  - `AuthRepositoryImpl`
  - `SettingsRepositoryImpl` (DataStore)
  - `LocationRepositoryImpl` (FusedLocationProvider)
  - `WeatherRepositoryImpl`

- [ ] **로컬 설정 저장**
  - DataStore Preferences 연동
  - 앱 설정 로컬 캐싱

### 6. 고급 기능
- [ ] **위치 서비스**
  - FusedLocationProvider 연동
  - 권한 처리 (Accompanist Permissions)
  - 주소 역변환 (Geocoding)

- [ ] **이미지 처리**
  - Coil 이미지 로딩
  - 갤러리/카메라 선택
  - 이미지 압축 및 최적화

- [ ] **푸시 알림**
  - Firebase Cloud Messaging
  - 일기 작성 리마인더
  - 로컬 알림 스케줄링

- [ ] **데이터 동기화**
  - Firebase Firestore 연동
  - 오프라인 지원
  - 데이터 백업/복원

### 7. 성능 최적화
- [ ] **성능 개선**
  - LazyColumn 최적화
  - 이미지 캐싱
  - 데이터베이스 쿼리 최적화
  - Compose 리컴포지션 최적화

- [ ] **테스트 코드**
  - Unit Test (JUnit)
  - UI Test (Compose Test)
  - Repository Test (Room Test)

## 📋 개발 우선순위

### Phase 1 (우선순위 높음)
1. **Write Screen 구현** - 핵심 기능
2. **Settings Screen 기본 UI** - 테마/언어 설정
3. **DataStore 연동** - 설정 저장

### Phase 2 (중간 우선순위)
1. **Firebase Auth 연동** - 사용자 관리
2. **Statistics Screen** - 감정 분석
3. **위치/날씨 서비스** - 부가 정보

### Phase 3 (낮은 우선순위)
1. **AI 응답 기능** - Firebase AI 연동
2. **푸시 알림** - 사용자 경험 개선
3. **고급 통계 기능** - 상세 분석

## 🚀 실행 방법

1. **환경 요구사항**
   - Android Studio Giraffe 이상
   - Kotlin 1.9.24
   - Minimum SDK 26 (Android 8.0)
   - Target SDK 36

2. **빌드 및 실행**
   ```bash
   # 프로젝트 클론 후
   cd moodlog_android
   ./gradlew assembleDebug
   ./gradlew installDebug
   ```

3. **Firebase 설정 (향후)**
   - `google-services.json` 파일 추가 필요
   - Firebase Console에서 프로젝트 생성

## 📚 참고 자료

- [Jetpack Compose 공식 문서](https://developer.android.com/jetpack/compose)
- [Room 데이터베이스 가이드](https://developer.android.com/training/data-storage/room)
- [Hilt 의존성 주입](https://dagger.dev/hilt/)
- [Material3 디자인 시스템](https://m3.material.io/)