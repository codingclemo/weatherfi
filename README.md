# Weather Finder

A modern Android weather application built with Jetpack Compose that provides real-time weather information and forecasts for selected locations.

## Features

- Real-time weather information with current conditions
- Detailed weather forecasts including hourly and daily predictions
- Pull-to-refresh functionality for updating weather data
- Animated UI transitions and weather icons
- Material 3 design implementation
- Dark mode support

## Technical Stack

### Architecture & Dependencies

- **Jetpack Compose**: Modern declarative UI toolkit for building native Android UI
- **Hilt**: Dependency injection library that reduces boilerplate code and provides a standard way to incorporate Dagger dependency injection into an Android application
- **Retrofit**: Type-safe HTTP client for Android and Java, used for making API calls to OpenWeather
- **Room**: Persistence library that provides an abstraction layer over SQLite
- **Turbine**: Testing library for Kotlin Flow, making it easier to test reactive streams
- **Coil**: Image loading library for Android backed by Kotlin Coroutines
- **Material 3**: Latest version of Material Design components and theming

## Architecture

The app follows a clean architecture approach with the following layers:

- **UI Layer**: Jetpack Compose screens and components
- **Domain Layer**: Business logic and use cases
- **Data Layer**: Repository implementations and data sources
- **Remote Layer**: API service implementations

## Setup

### Requirements

- Android Studio Hedgehog (2023.1.1) or newer
- Android SDK 28 or higher
- Gradle 8.0 or higher
- JDK 11

### Configuration

1. Clone the repository
2. Open the project in Android Studio
3. Add your OpenWeather API key to `gradle.properties`:
   ```properties
   openweather.api.key=your_api_key_here
   ```
   Note: In a production environment, the API key should be fetched from a secure server rather than being bundled with the app.

4. Build and run the application

## Known Limitations

- No internationalization support (English only)
- Fixed list of locations (no location search or custom locations)
- UI optimized for phones only (no tablet or foldable support)
- API key is bundled with the app (should be fetched from a secure server in production)

## Future Improvements

- Add location search functionality
- Implement internationalization
- Support for different form factors (tablets, foldables)
- Offline support with Room database
- Weather notifications and widgets
- More detailed weather information and maps

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the terms of the license included in the repository.