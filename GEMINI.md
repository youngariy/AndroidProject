# GEMINI.md

## Project Overview

This is the project for PopCorn, an Android application for browsing movies and TV shows. It is written in Java and built with Gradle.

The app uses the [The Movie Database (TMDb)](https://www.themoviedb.org/) API to fetch data. It features a Material Design UI and allows users to view details about movies and TV shows, watch trailers, and manage a list of their favorite titles.

### Key Technologies:

*   **Language:** Java
*   **Build:** Gradle
*   **Networking:** [Retrofit](https://square.github.io/retrofit/)
*   **Image Loading:** [Glide](https://github.com/bumptech/glide)
*   **Database:** SQLite (for favorites)
*   **UI:** Android Material Components

## Building and Running

### Prerequisites

You will need a TMDb API key to build and run the app.

1.  Get an API key from [The Movie Database](https://www.themoviedb.org/documentation/api).
2.  Open the file `app/src/main/res/values/strings.xml`.
3.  Add the following line, replacing `YOUR_API_KEY_HERE` with your actual key:
    ```xml
    <string name="MOVIE_DB_API_KEY">YOUR_API_KEY_HERE</string>
    ```

### Gradle Commands

You can use the Gradle wrapper (`gradlew`) from the root directory to build and run the application.

*   **Build the project:**
    ```shell
    ./gradlew build
    ```

*   **Assemble a debug APK:**
    ```shell
    ./gradlew assembleDebug
    ```
    The output APK will be located in `app/build/outputs/apk/debug/`.

*   **Install the debug APK on a connected device/emulator:**
    ```shell
    ./gradlew installDebug
    ```

*   **Run unit tests:**
    ```shell
    ./gradlew test
    ```

## Development Conventions

*   The code follows standard Android development patterns.
*   The application ID is `com.hitanshudhawan.popcorn`.
*   The minimum Android SDK version is 21.
*   The target Android SDK version is 26.
*   Dependencies are managed in `app/build.gradle`.
*   The app's activities, services, and permissions are declared in `app/src/main/AndroidManifest.xml`.
