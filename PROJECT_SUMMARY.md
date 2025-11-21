# Project Summary: PopCorn Android Application

This document summarizes key aspects of the PopCorn Android application, focusing on its structure and interaction with The Movie Database (TMDb) API.

## 1. Project Directory Structure

```
C:\Users\dudqj\Desktop\PopCorn-master\
├───.gradle\
├───.idea\
├───app\
│   ├───build\
│   └───src\
│       └───main\
│           ├───AndroidManifest.xml
│           ├───java\
│           │   └───com\
│           │       └───hitanshudhawan\
│           │           └───popcorn\
│           │               ├───network\
│           │               │   ├───search\
│           │               │   │   └───SearchAsyncTaskLoader.java
│           │               │   └───ApiClient.java
│           │               └───... (other Java source files)
│           └───res\
│               ├───drawable\
│               ├───layout\
│               ├───menu\
│               ├───mipmap-hdpi\
│               ├───mipmap-mdpi\
│               ├───mipmap-xhdpi\
│               ├───mipmap-xxhdpi\
│               ├───mipmap-xxxhdpi\
│               └───values\
│                   └───strings.xml
├───gradle\
├───images\
└───... (other root level files)
```

## 2. TMDB API Usage

The PopCorn application uses The Movie Database (TMDb) API to fetch movie and TV show data.

### 2.1 API Key Configuration

To use the TMDb API, an API key is required. This key needs to be added to the `app/src/main/res/values/strings.xml` file:

```xml
<string name="MOVIE_DB_API_KEY">YOUR_API_KEY_HERE</string>
```
Replace `YOUR_API_KEY_HERE` with your actual TMDb API key.

### 2.2 Networking Libraries

*   **Retrofit 2:** Used for making HTTP requests to the TMDb API.
*   **Gson:** Used for serializing Java objects to JSON and deserializing JSON responses into Java objects.
*   **Glide:** Used for efficient image loading and caching.

### 2.3 API Client and Base URL

The base URL for the TMDb API is defined in `app/src/main/java/com/hitanshudhawan/popcorn/network/ApiClient.java`:

```java
public class ApiClient {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
```
This `ApiClient` provides a singleton `Retrofit` instance configured with the base URL and a Gson converter.

### 2.4 API Call Method Example (Search)

While Retrofit is set up, some API calls, like the search functionality, appear to be implemented using `HttpURLConnection` and manual JSON parsing.

An example from `app/src/main/java/com/hitanshudhawan/popcorn/network/search/SearchAsyncTaskLoader.java` shows how a search query is constructed:

```java
// Inside SearchAsyncTaskLoader.java's loadInBackground() method
String urlString = "https://api.themoviedb.org/3/" + "search/multi"
        + "?"
        + "api_key=" + mContext.getResources().getString(R.string.MOVIE_DB_API_KEY)
        + "&"
        + "query=" + mQuery
        + "&"
        + "page=" + mPage;
URL url = new URL(urlString);
HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
httpURLConnection.setRequestMethod("GET");

// ... (connection, input stream reading, and manual JSON parsing using JSONObject/JSONArray)
```

This example demonstrates:
*   Direct URL construction for API endpoints (e.g., `search/multi`).
*   Appending query parameters like `api_key`, `query`, and `page`.
*   Using `HttpURLConnection` to make the GET request.
*   Reading the response as an `InputStream` and manually parsing the JSON data using Android's `JSONObject` and `JSONArray`.

**Note:** The codebase includes a comment suggesting the use of custom deserialization with GSON, implying that a full Retrofit-based approach with data models might be intended or partially implemented elsewhere, but the `SearchAsyncTaskLoader` uses a more manual approach for parsing its response.
