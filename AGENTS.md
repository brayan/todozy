# Repository Guidelines

## Project Structure & Module Organization
- Kotlin DSL multi-module Gradle. Entry point in `app/` with DI wiring. Core business rules in `domain/`; shared helpers live in `utility/kotlin-util` and Android extensions in `utility/android-util`.
- UI kit split into `ui-component/public` (contracts) and `ui-component/impl` (views/resources). Features live under `feature/<name>/{public,impl}` (task-list, task-form, task-history, task-details, alarm, settings, about, splash, navigation) with data/domain/presentation layers.
- Platform integrations (DB, logging, Crashlytics) sit in `platform/impl`. Resources stay per module; non-transitive R is on, so import the correct module `R`.
- Build constants and dependency aliases live in `buildSrc` (`BuildVersion`, `Dependency`, `Module`). Prefer those over hardcoded values.

## Build, Test, and Development Commands
- `./gradlew assembleDebug` — builds with AGP 8.10.1, Kotlin 1.9.22, Java 17, compileSdk 36/targetSdk 35/minSdk 24.
- `./gradlew test` — JVM unit tests across modules.
- `./gradlew ktlintCheck` (or `ktlintFormat`) — Kotlin style enforcement.
- `./gradlew checkCrossModuleRImports` — fails on cross-module `R` imports; run before PRs.
- `./gradlew clean` — clear build outputs if caches misbehave.

## Coding Style & Naming Conventions
- ktlint defaults (4-space indent, ordered imports, trailing commas off). Keep Android types out of `domain`.
- Naming: `PascalCase` for classes, `camelCase` for members, `UPPER_SNAKE_CASE` for constants, lowercase packages. Suffix patterns: `*UseCaseImpl`, `*RepositoryImpl`, `*ViewModel`, `*Mapper`.
- Favor view binding for XML, Koin for DI modules under `di/`, and `java.time` (desugared) over legacy date APIs.

## Testing Guidelines
- Stack: JUnit4, MockK, coroutine-test, AndroidX test libs. Write fast JVM tests; mock navigation/DB/analytics boundaries.
- Mirror package paths in `src/test/java` (e.g., `TaskListViewModelTest`, `CompleteTaskUseCaseImplTest`). Cover mappers, repositories, and business rules before UI polish.
- CI runs `assembleDebug`, `test`, and `checkCrossModuleRImports`; keep it green.

## Commit & Pull Request Guidelines
- Commits are small, imperative, and single-purpose (e.g., “Migrate time picker theme to Material3 overlay”).
- PRs include: summary, linked issue, screenshots for UI changes, and results for `assembleDebug`, `test`, `ktlintCheck`, `checkCrossModuleRImports`. Call out SDK/AGP bumps, resource moves, or permission changes and update docs when they apply.

## Security & Configuration Tips
- Keep secrets out of VCS; only the checked-in `google-services.json` remains. Store local keys outside the repo and configure paths in `local.properties`.
- Avoid verbose logging in release builds; confirm Crashlytics toggles when adjusting build types. Keep FAB tint/icon contrast consistent with the Material theme and brand colors.
