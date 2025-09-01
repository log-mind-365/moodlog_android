# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

MoodLog is an Android application built with Kotlin and Jetpack Compose for mood tracking and journaling. The app uses Firebase for authentication and data storage, with a local Room database for offline functionality.

**Package name:** `com.logmind.moodlog`
**Min SDK:** 26, **Target SDK:** 36

## Development Commands

### Build & Test
```bash
# Build the project
./gradlew build

# Run unit tests
./gradlew test

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Clean build
./gradlew clean
```

### Running Tests
- Unit tests: `./gradlew testDebugUnitTest`
- Instrumented tests: `./gradlew connectedDebugAndroidTest`
- Test a single class: `./gradlew test --tests "com.logmind.moodlog.ExampleUnitTest"`

## Architecture

### Clean Architecture Structure
```
app/src/main/java/com/logmind/moodlog/
├── data/
│   ├── database/         # Room database, entities, DAOs
│   ├── mappers/          # Entity-domain mappers
│   └── repositories/     # Repository implementations
├── domain/
│   ├── entities/         # Domain models
│   ├── repositories/     # Repository interfaces
│   └── usecases/         # Business logic
├── presentation/
│   ├── navigation/       # Navigation components
│   └── [screens]/        # UI screens with ViewModels
├── di/                   # Dependency injection modules
└── ui/theme/             # Compose theming
```

### Key Technologies
- **UI:** Jetpack Compose with Material 3
- **Architecture:** MVVM with Clean Architecture
- **DI:** Hilt (Dagger)
- **Database:** Room with KSP
- **Navigation:** Navigation Compose
- **Network:** Retrofit + OkHttp
- **Image Loading:** Coil
- **Firebase:** Auth, Firestore, Analytics
- **Data Storage:** DataStore Preferences

### Core Domain Entities
- **Journal:** Main mood log entry with mood type, content, tags, weather, location
- **Tag:** Categorization system for journal entries
- **Settings:** User preferences, theme, language, AI personality
- **MoodType:** Enum for different mood states
- **WeatherInfo & LocationInfo:** Context data for entries

### Database Schema
- **JournalEntity:** Core journal entries
- **TagEntity:** Available tags
- **JournalTagCrossRef:** Many-to-many relationship between journals and tags
- Uses Room with KSP for code generation

### Navigation
- Navigation destinations defined in `NavigationDestinations.kt`
- Main navigation handled in `MoodLogNavigation.kt`
- Screen-level navigation uses Navigation Compose

### Dependency Injection
- All modules in `di/` package
- `DatabaseModule`: Room database configuration
- `RepositoryModule`: Repository bindings
- Application class: `MoodLogApplication` with `@HiltAndroidApp`

## Development Notes

- Uses version catalogs (`gradle/libs.versions.toml`) for dependency management
- Firebase configuration via `google-services.json`
- Kotlin 2.2.10 with Compose compiler plugin
- Java 11 compatibility
- KSP for annotation processing (Room, Hilt)