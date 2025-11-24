# Todozy – Tasks & Habits

[![CI](https://github.com/brayan/todozy/actions/workflows/ci.yml/badge.svg)](https://github.com/brayan/todozy/actions/workflows/ci.yml)

Minimalist task manager built with a modular Android stack (Java 17, Kotlin 1.9, AGP 8.x, compile/targetSdk 36, minSdk 24) and MaterialComponents.

## Architecture & Modules
- Clean Architecture + MVVM, DI via Koin.
- Domain rules in `domain/`; shared helpers in `utility/kotlin-util` and `utility/android-util`.
- UI kit split into `ui-component/public` (contracts) and `ui-component/impl` (views/resources).
- Features under `feature/<name>/{public,impl}` (task-list, task-form, task-history, task-details, alarm, settings, about, splash, navigation) with data/domain/presentation layers.
- Platform integrations (DB, logging, Crashlytics) in `platform/impl`. Non-transitive R enforced.

## Build & Test
- `./gradlew assembleDebug` – build the app.
- `./gradlew test` – run JVM unit tests across modules.
- `./gradlew ktlintCheck` – style check; `ktlintFormat` to fix.
- `./gradlew checkCrossModuleRImports` – guard against importing the app `R` outside `app/`.

## Tech Stack
- Kotlin coroutines, Flow
- AndroidX: AppCompat/Activity/Fragment, Lifecycle 2.7, Material Components
- Koin 3.x for DI
- Testing: JUnit4, MockK, coroutine-test, Arch core test

## UI/UX
- Theme: `Theme.MaterialComponents.DayNight.NoActionBar` with Material date/time pickers.
- FABs tinted with `colorPrimary` and white icons; toolbar uses Material toolbar style.

## Development Notes
- Follow module boundaries; import resources from the correct module R (UI alias `UiR`).
- Avoid `SimpleDateFormat`; `java.time` is enabled via coreLibraryDesugaring.
- Pre-commit hooks run ktlint; ensure `assembleDebug` and `test` pass before PRs.
