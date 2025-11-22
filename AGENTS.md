# Repository Guidelines

## Project Structure & Module Organization
- Android app module lives in `app/`; sources in `app/src/main/java/com/hitanshudhawan/popcorn/` with feature folders like `activities`, `fragments`, `network`, and `viewmodels`.
- UI resources sit in `app/src/main/res/` (layouts, drawables, values, menus); update strings and themes there.
- Build outputs land in `app/build/`; checked-in demo assets live under `images/`.

## Build, Test, and Development Commands
- `./gradlew assembleDebug` - compile the app for local installs using the debug config.
- `./gradlew clean build` - full compile plus unit tests; run before pushing changes.
- `./gradlew lint` - Android lint scan; fix or justify warnings before PRs.
- `./gradlew connectedAndroidTest` - instrumentation tests on a running emulator or device.

## Coding Style & Naming Conventions
- Java 8 with AndroidX; prefer 4-space indentation. Keep opening braces on the same line for statements, new line for types.
- Classes and enums: PascalCase (`MovieDetailActivity`); methods/fields/locals: camelCase; constants: ALL_CAPS with underscores.
- Resource naming: layouts `activity_*`/`fragment_*`, drawables `ic_*` or `bg_*`, strings `label_*`/`msg_*`.
- Keep networking in `network/`, UI logic in `activities`/`fragments`, shared helpers in `utils`, and screen state in `viewmodels`.

## Testing Guidelines
- Unit tests (if added) belong under `app/src/test/java`; instrumentation under `app/src/androidTest/java`.
- Name tests with the subject and expectation (`MovieRepositoryTest`, `returnsCachedResults_whenOffline`).
- Target high-value logic: API client wrappers, database caching, and ViewModel transformations; add regression tests with each bug fix.

## Config & Security
- TMDb API key is required; add `<string name="MOVIE_DB_API_KEY">...</string>` to `app/src/main/res/values/strings.xml` and avoid committing real keys.
- Do not commit `local.properties`, keystore files, or emulator data; rely on `.gitignore` defaults.

## Commit & Pull Request Guidelines
- Commits: short, imperative summaries (e.g., `Add search pagination`), scoped to a single concern.
- PRs: include purpose, screenshots for UI changes, reproduction steps for fixes, and note any lint/test gaps; link related issues.
- Keep branches rebased on `main` before review; prefer small, focused PRs for quicker reviews.
