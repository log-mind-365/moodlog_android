# MoodLog Android - 기능 명세서

## 📱 완료된 기능

### 🏠 홈 화면 (HomeScreen)
**구현 상태**: ✅ 완료 (100%)

#### 주요 기능
- **월간 캘린더 뷰**
  - 현재 월의 날짜들을 가로 스크롤로 표시
  - 일기가 있는 날짜에 점(dot) 표시
  - 선택된 날짜 하이라이트 표시
  - 날짜 선택 시 해당 날짜의 일기 목록 로드

- **일기 목록 표시**
  - 선택된 날짜의 모든 일기를 시간 순으로 표시
  - 각 일기 카드에 감정 이모지, 작성 시간, 내용 미리보기
  - 일기가 없을 경우 "첫 번째 무드 기록하기" 안내 메시지

- **빠른 액션**
  - FAB(Floating Action Button)으로 일기 작성 화면 이동
  - 각 일기 카드에서 삭제 버튼으로 즉시 삭제

#### 기술 구현
- **HomeViewModel**: StateFlow 기반 상태 관리
- **UI State 관리**: `HomeUiState` 데이터 클래스로 로딩/에러/데이터 상태
- **날짜 처리**: `LocalDateTime` 기반 날짜 선택 및 필터링
- **리스트 최적화**: `LazyColumn`, `LazyRow` 사용으로 성능 최적화

### 🗂️ 데이터 계층 (Data Layer)  
**구현 상태**: ✅ 완료 (95%)

#### Room Database
- **MoodLogDatabase**: 메인 데이터베이스 클래스
  - 버전 1, 스키마 내보내기 활성화
  - 3개 엔티티 테이블 관리

- **엔티티 테이블**
  ```sql
  -- journals 테이블
  CREATE TABLE journals (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    content TEXT,
    mood_type TEXT NOT NULL,
    image_uris TEXT,  -- JSON 형태 저장
    created_at TEXT NOT NULL,
    ai_response_enabled INTEGER NOT NULL,
    ai_response TEXT,
    latitude REAL,
    longitude REAL,
    address TEXT,
    temperature REAL,
    weather_icon TEXT,
    weather_description TEXT
  );
  
  -- tags 테이블  
  CREATE TABLE tags (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    color TEXT,
    created_at TEXT NOT NULL
  );
  
  -- journal_tag_cross_ref 테이블 (다대다 관계)
  CREATE TABLE journal_tag_cross_ref (
    journal_id INTEGER,
    tag_id INTEGER,
    PRIMARY KEY (journal_id, tag_id),
    FOREIGN KEY (journal_id) REFERENCES journals(id),
    FOREIGN KEY (tag_id) REFERENCES tags(id)
  );
  ```

#### DAO 인터페이스
- **JournalDao**: 일기 관련 모든 데이터베이스 작업
  - `getAllJournalsFlow()`: 실시간 데이터 스트림
  - `getJournalsByDate()`, `getJournalsByMonth()`: 날짜별 조회
  - `insertJournal()`, `updateJournal()`, `deleteJournalById()`
  - `updateJournalTags()`: 일기-태그 관계 업데이트

- **TagDao**: 태그 관련 데이터베이스 작업
  - `getAllTags()`, `getTagById()`, `getTagsByJournalId()`
  - `insertTag()`, `updateTag()`, `deleteTag()`

#### Repository 구현체
- **JournalRepositoryImpl**: 일기 비즈니스 로직 구현
  - Room DAO를 사용한 CRUD 작업
  - Entity ↔ Domain 모델 자동 변환
  - Flow 기반 실시간 데이터 스트림 제공
  - Result 패턴으로 성공/실패 처리

- **TagRepositoryImpl**: 태그 관리 로직 구현
  - 태그 CRUD 및 일기-태그 관계 관리

### 🏗️ Domain Layer (도메인 계층)
**구현 상태**: ✅ 완료 (100%)

#### 핵심 엔티티
- **MoodType**: 감정 타입 enum
  ```kotlin
  enum class MoodType(val score: Int, val emoji: String, val colorValue: Long) {
    VERY_HAPPY(5, "😄", 0xFF4CAF50),
    HAPPY(4, "😊", 0xFF8BC34A), 
    NEUTRAL(3, "😐", 0xFFFFEB3B),
    SAD(2, "😢", 0xFFFF9800),
    VERY_SAD(1, "😭", 0xFFF44336)
  }
  ```

- **Journal**: 일기 도메인 모델
  ```kotlin
  data class Journal(
    val id: Int,
    val content: String?,
    val moodType: MoodType,
    val imageUris: List<String>?,
    val createdAt: LocalDateTime,
    val aiResponseEnabled: Boolean,
    val aiResponse: String?,
    // 위치/날씨 정보
    val latitude: Double?,
    val longitude: Double?,
    val address: String?,
    val temperature: Double?,
    val weatherIcon: String?,
    val weatherDescription: String?,
    val tags: List<Tag>?
  )
  ```

#### 설정 관련 엔티티
- **설정 Enum 클래스들**
  - `LanguageCode`: 9개 언어 지원 (한/영/일/중/서/이/프/베/태)
  - `ColorTheme`: 19가지 컬러 테마
  - `ThemeMode`: 라이트/다크/시스템 테마
  - `FontFamily`: 6가지 폰트 패밀리
  - `AiPersonality`: AI 성격 (이성적/균형/공감)

#### Use Cases
- **JournalUseCase**: 일기 비즈니스 로직
- **TagUseCase**: 태그 관리 로직  
- **AuthUseCase**: 인증 처리 로직

#### 공통 클래스
- **Result<T>**: 성공/실패 래퍼 클래스
  ```kotlin
  sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
  }
  ```

### ⚙️ 의존성 주입 (Hilt)
**구현 상태**: ✅ 완료 (100%)

#### 모듈 구성
- **DatabaseModule**: Room Database 및 DAO 제공
- **RepositoryModule**: Repository 인터페이스 바인딩
- **@HiltAndroidApp**: Application 클래스 어노테이션
- **@AndroidEntryPoint**: Activity/Fragment 어노테이션

---

## 🚧 구현 예정 기능

### ✏️ 일기 작성 화면 (WriteScreen)
**구현 상태**: ❌ 미구현 (0%)

#### 계획된 UI 구성
- **감정 선택 섹션**
  - 5단계 감정 슬라이더 (VERY_SAD ~ VERY_HAPPY)
  - 선택된 감정의 이모지와 색상 실시간 표시
  - 슬라이더 값에 따른 배경색 그라데이션 효과

- **텍스트 입력 섹션**  
  - 멀티라인 텍스트 입력 필드
  - 글자 수 카운터 표시
  - 텍스트 정렬 옵션 (좌/중/우)

- **이미지 첨부 섹션**
  - 갤러리에서 선택 / 카메라 촬영 옵션
  - 최대 5장까지 첨부 가능
  - 썸네일 미리보기 및 삭제 기능
  - 이미지 압축 및 리사이징

- **태그 선택 섹션**
  - 기존 태그 목록에서 선택
  - 새 태그 생성 기능
  - 선택된 태그 Chip 형태로 표시
  - 태그별 색상 커스터마이징

- **부가 정보 섹션**
  - 현재 위치 자동 수집 (옵션)
  - 현재 날씨 정보 표시
  - AI 응답 생성 활성화 토글

- **액션 바**
  - 저장 버튼 (유효성 검사 후 활성화)
  - 취소 버튼 (변경 사항 확인 다이얼로그)
  - 임시 저장 기능

#### 계획된 기술 구현
- **WriteViewModel**: 복잡한 폼 상태 관리
  ```kotlin
  data class WriteUiState(
    val moodType: MoodType = MoodType.NEUTRAL,
    val content: String = "",
    val selectedImages: List<Uri> = emptyList(),
    val selectedTags: List<Tag> = emptyList(),
    val locationInfo: LocationInfo? = null,
    val weatherInfo: WeatherInfo? = null,
    val aiResponseEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
  )
  ```

- **이미지 처리 파이프라인**
  - ActivityResultContracts 사용한 갤러리/카메라 접근
  - Coil 라이브러리로 이미지 로딩 및 캐싱
  - 자동 압축 및 리사이징 (최대 1MB)

- **실시간 유효성 검사**
  - 감정 선택 필수 체크
  - 텍스트 또는 이미지 중 하나 필수 입력
  - 저장 버튼 활성화/비활성화

### 📊 통계 화면 (StatisticsScreen) 
**구현 상태**: ❌ 미구현 (0%)

#### 계획된 UI 구성
- **대시보드 섹션**
  - 이번 달 평균 감정 점수 (큰 숫자 + 이모지)
  - 연속 기록 일수 (Streak Count)
  - 총 일기 작성 수
  - 가장 많이 사용한 감정

- **감정 추이 차트**
  - 최근 30일 감정 점수 라인 차트
  - 주간/월간 뷰 전환 옵션
  - 터치 시 해당 날짜 상세 정보 표시
  - 감정별 색상으로 구분된 포인트

- **감정 분포 차트**
  - 도넛 차트로 감정 비율 표시
  - 각 감정의 퍼센티지 및 개수
  - 탭하면 해당 감정의 일기 목록으로 이동

- **태그 분석**
  - 가장 많이 사용된 태그 TOP 10
  - 태그별 감정 평균 점수
  - 태그 클라우드 형태 시각화

- **시간대별 분석**
  - 일기 작성 시간대 분포
  - 시간대별 감정 패턴 분석

#### 계획된 기술 구현
- **커스텀 Compose 차트**
  - Canvas API를 사용한 차트 구현
  - 애니메이션 효과 (값 변경 시 부드러운 전환)
  - 터치 제스처 처리 (탭, 드래그)

- **통계 데이터 계산 로직**
  ```kotlin
  data class StatisticsData(
    val averageMood: Double,
    val streakDays: Int,
    val totalEntries: Int,
    val moodDistribution: Map<MoodType, Int>,
    val moodTrend: List<DailyMood>,
    val topTags: List<TagStats>,
    val timePatterns: Map<Int, Int> // Hour -> Count
  )
  ```

### ⚙️ 설정 화면 (SettingsScreen)
**구현 상태**: ❌ 미구현 (0%)

#### 계획된 UI 구성
- **외관 설정 섹션**
  - 테마 선택: 라이트/다크/시스템 (RadioButton)
  - 컬러 테마: 19가지 색상 그리드 선택기
  - 폰트 패밀리: 6가지 폰트 드롭다운

- **언어 설정 섹션**  
  - 언어 선택: 9개 언어 지원 드롭다운
  - 변경 시 즉시 적용 (앱 재시작 안내)

- **알림 설정 섹션**
  - 일기 작성 리마인더 활성화/비활성화
  - 리마인더 시간 설정 (TimePicker)
  - 리마인더 요일 선택

- **동기화 설정 섹션**
  - 자동 백업 활성화/비활성화
  - 백업 주기 설정 (일간/주간)
  - 마지막 백업 시간 표시

- **AI 설정 섹션**
  - AI 응답 기능 활성화/비활성화
  - AI 페르소나 선택 (이성적/균형/공감)
  - AI 응답 언어 설정

- **계정 관리 섹션**
  - 로그인 상태 표시
  - 프로필 이미지 및 닉네임 변경
  - 로그아웃 / 계정 연결

- **앱 정보 섹션**
  - 버전 정보
  - 개인정보 처리방침
  - 오픈소스 라이선스
  - 앱 평가하기

#### 계획된 기술 구현
- **DataStore Preferences 연동**
  ```kotlin
  class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
  ) : SettingsRepository {
    
    private val THEME_MODE = stringPreferencesKey("theme_mode")
    private val LANGUAGE_CODE = stringPreferencesKey("language_code")
    private val COLOR_THEME = stringPreferencesKey("color_theme")
    // ... 기타 설정 키들
  }
  ```

- **실시간 설정 적용**
  - 테마 변경 시 즉시 UI 반영
  - 언어 변경 시 문자열 리소스 업데이트
  - 컬러 테마 변경 시 Material3 색상 적용

### 🔐 인증 화면 (AuthScreen)
**구현 상태**: ❌ 미구현 (0%)

#### 계획된 UI 구성
- **로그인 화면**
  - Google 로그인 버튼 (Material 디자인)
  - 익명 로그인 옵션
  - 로그인 중 로딩 인디케이터
  - 로그인 실패 시 에러 메시지

- **프로필 관리 화면**
  - 현재 로그인된 계정 정보
  - 프로필 이미지 변경 (갤러리/카메라)
  - 닉네임 변경 (텍스트 입력)
  - 계정 연결 (익명 → Google)

#### 계획된 기술 구현
- **Firebase Auth 연동**
  ```kotlin
  class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
  ) : AuthRepository {
    
    override val userChanges: Flow<FirebaseUser?> = 
      firebaseAuth.authStateChanges().asFlow()
    
    override suspend fun signInWithGoogle(): Result<FirebaseUser?> {
      // Google Sign-In 구현
    }
  }
  ```

### 🌍 위치 및 날씨 서비스
**구현 상태**: ❌ 미구현 (0%)

#### 계획된 기능
- **위치 서비스 (LocationService)**
  - FusedLocationProvider 사용한 정확한 위치 수집
  - 위치 권한 요청 및 처리
  - 주소 역변환 (위경도 → 읽기 쉬운 주소)
  - 위치 캐싱으로 배터리 최적화

- **날씨 서비스 (WeatherService)**
  - OpenWeatherMap API 연동
  - 현재 위치 기반 날씨 정보 수집
  - 온도, 습도, 날씨 상태, 아이콘 정보
  - 날씨 정보 로컬 캐싱 (1시간)

#### 계획된 기술 구현
- **권한 처리 (Accompanist Permissions)**
  ```kotlin
  @Composable
  fun LocationPermissionHandler(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
  ) {
    val locationPermissionState = rememberPermissionState(
      android.Manifest.permission.ACCESS_FINE_LOCATION
    )
    
    LaunchedEffect(locationPermissionState.status) {
      when {
        locationPermissionState.status.isGranted -> onPermissionGranted()
        locationPermissionState.status.shouldShowRationale -> {
          // 권한 설명 다이얼로그 표시
        }
        else -> locationPermissionState.launchPermissionRequest()
      }
    }
  }
  ```

### 🤖 AI 응답 생성 (Firebase AI)
**구현 상태**: ❌ 미구현 (0%)

#### 계획된 기능
- **Gemini API 연동**
  - 일기 내용 기반 AI 응답 생성
  - 3가지 페르소나별 응답 스타일
  - 사용자 언어에 맞는 응답 생성

- **AI 페르소나**
  - **이성적 (Rational)**: 논리적이고 분석적인 조언
  - **균형 (Balanced)**: 중립적이고 균형잡힌 관점
  - **공감 (Compassionate)**: 따뜻하고 공감적인 응답

#### 계획된 기술 구현
```kotlin
class GeminiRepositoryImpl @Inject constructor(
  private val geminiApi: GeminiApi
) : GeminiRepository {
  
  override suspend fun generateResponse(
    content: String,
    moodType: MoodType,
    personality: AiPersonality,
    language: LanguageCode
  ): Result<String> {
    val prompt = buildPrompt(content, moodType, personality, language)
    return geminiApi.generateText(prompt)
  }
}
```

### 🔔 푸시 알림 시스템
**구현 상태**: ❌ 미구현 (0%)

#### 계획된 기능
- **일기 작성 리마인더**
  - 설정한 시간에 매일 알림
  - 요일별 알림 설정 가능
  - 연속 기록 격려 메시지

- **Firebase Cloud Messaging**
  - 앱 업데이트 알림
  - 새 기능 소개 알림
  - 개인화된 인사이트 알림

### 📱 추가 화면 및 기능

#### Bottom Navigation
- **홈 탭**: 현재 HomeScreen
- **통계 탭**: StatisticsScreen  
- **설정 탭**: SettingsScreen
- **프로필 탭**: 사용자 정보 및 인증

#### 상세 화면들
- **일기 상세보기**: 단일 일기의 모든 정보 표시
- **일기 편집**: 기존 일기 수정
- **태그 관리**: 태그 CRUD 전용 화면
- **통계 상세**: 특정 기간/감정의 상세 분석

---

## 📊 기능 구현 진행률

### 전체 진행률: **35%** ✅

| 카테고리 | 진행률 | 상태 |
|---------|--------|------|
| 프로젝트 설정 | 100% | ✅ 완료 |
| Domain Layer | 100% | ✅ 완료 |  
| Data Layer | 95% | ✅ 거의 완료 |
| Home Screen | 100% | ✅ 완료 |
| Write Screen | 0% | ❌ 미구현 |
| Statistics Screen | 0% | ❌ 미구현 |
| Settings Screen | 0% | ❌ 미구현 |
| Auth System | 0% | ❌ 미구현 |
| Location/Weather | 0% | ❌ 미구현 |
| AI Integration | 0% | ❌ 미구현 |
| Push Notifications | 0% | ❌ 미구현 |

### 다음 구현 우선순위
1. **WriteScreen** (일기 작성) - 핵심 기능
2. **SettingsScreen** (설정) - 사용자 경험
3. **StatisticsScreen** (통계) - 부가 가치
4. **AuthScreen** (인증) - 데이터 보안
5. **위치/날씨 서비스** - 컨텍스트 정보
6. **AI 응답 생성** - 차별화 기능