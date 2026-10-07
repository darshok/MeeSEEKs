# 🥒 MeeSEEKs - Rick and Morty Android App

**MeeSEEKs** is a modern, production-ready Android application built with native Kotlin and 100% Jetpack Compose. It serves as an interactive guide to the Rick and Morty universe, leveraging the official Rick and Morty API with an offline-first architectural approach.

---

## 🚀 Key Features

* **Responsive Adaptive Grid**: The character list dynamically scales columns depending on available screen width (single column on portrait phones, 2 columns in landscape, and 3+ columns on tablets and large form factors) using `GridCells.Adaptive`.
* **Material 3 Expandable Search Bar**: A smooth, animated `TopSearchBar` that collapses into a custom app bar with the Rick and Morty logo and expands with auto-focus, keyboard management, cursor placement at the end, and instant character filtering.
* **Shared Element Transitions**: Fluid visual transitions between character cards and their detail views using experimental Compose shared element animations.
* **Offline-First Pagination**: Powered by Android Paging 3 and Room Database with a `RemoteMediator` pattern to seamlessly sync remote data, cache results locally, and gracefully handle edge cases like API 404 responses.
* **Immersive Details & Skeletons**: Rich detail screens for both characters and locations complete with custom shimmer skeleton loaders.

---

## 🛠️ Tech Stack & Architecture

* **UI & Design**:
  * [Jetpack Compose](https://developer.android.com/jetpack/compose) & [Material 3](https://m3.material.io/)
  * Adaptive Layouts & `LazyVerticalGrid`
  * Shared Element Transitions (`SharedTransitionLayout`)
* **Architecture**:
  * **Clean Architecture** (Presentation, Domain, Data layers)
  * **MVVM** (Model-View-ViewModel) pattern
* **Navigation**:
  * [Jetpack Navigation 3](https://developer.android.com/guide/navigation/navigation-3) (`navigation3-runtime`, `navigation3-ui`, `lifecycle-viewmodel-navigation3`)
* **Concurrency & Reactivity**:
  * Kotlin Coroutines & `Flow` / `StateFlow`
* **Dependency Injection**:
  * [Dagger Hilt](https://dagger.dev/hilt/)
* **Networking & Parsing**:
  * [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/) (Logging Interceptor)
  * [Gson](https://github.com/google/gson)
* **Local Storage & Paging**:
  * [Room Database](https://developer.android.com/training/data-storage/room)
  * [Android Paging 3](https://developer.android.com/topic/paging/v3-overview) (with `RemoteMediator`)
* **Testing**:
  * JUnit 4, [MockK](https://mockk.io/), Coroutines Test (`runTest`)

---

## 📂 Project Structure

```tree
com.glootie.meeseeks/
├── core/             # Constants, Network helpers, DataResponse wrappers
├── data/             # Local (Room DAOs, Entities), Remote (ApiService), Repositories & Mediators
├── domain/           # Domain Models and Business Use Cases
└── ui/
    ├── common/       # Common UI states (UiState, Skeletons)
    ├── component/    # Reusable components (TopBar, Cards)
    ├── navigation/   # Navigation 3 Host & Routes
    ├── screen/       # Feature screens & ViewModels (CharacterList, CharacterDetails, LocationDetails)
    └── theme/        # Color, Type, and Material 3 Theme configurations
```

---

## 🧪 Running Tests

The project includes a robust suite of unit tests covering ViewModels, Use Cases, and Repositories. You can run them via Gradle:

```bash
./gradlew :app:testDebugUnitTest
```
