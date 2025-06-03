package com.codingclemo.weatherfinder.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val BoldLightColorScheme = lightColorScheme(
    primary = WeatherBlack,
    onPrimary = WeatherYellow,
    primaryContainer = WeatherYellow,
    onPrimaryContainer = WeatherBlack,
    secondary = WeatherBlack,
    onSecondary = WeatherYellow,
    secondaryContainer = WeatherBlack,
    onSecondaryContainer = WeatherYellow,
    tertiary = WeatherSubtleYellow,
    onTertiary = WeatherGray,
    tertiaryContainer = WeatherYellow,
    onTertiaryContainer = WeatherBlack,
    background = WeatherYellow,
    onBackground = WeatherBlack,
    surface = WeatherYellow,
    onSurface = WeatherBlack,
    surfaceVariant = WeatherYellow,
    onSurfaceVariant = WeatherBlack,
    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFFCDAE1),
    onErrorContainer = Color(0xFF410E0B),
    outline = WeatherBlack,
    inverseSurface = WeatherBlack,
    inverseOnSurface = WeatherYellow,
    inversePrimary = WeatherYellow,
    surfaceTint = WeatherBlack
)

private val BoldDarkColorScheme = darkColorScheme(
    primary = WeatherYellow,
    onPrimary = WeatherBlack,
    primaryContainer = WeatherBlack,
    onPrimaryContainer = WeatherYellow,
    secondary = WeatherYellow,
    onSecondary = WeatherBlack,
    secondaryContainer = WeatherYellow,
    onSecondaryContainer = WeatherBlack,
    tertiary = WeatherSubtleYellow,
    onTertiary = WeatherGray,
    tertiaryContainer = WeatherBlack,
    onTertiaryContainer = WeatherYellow,
    background = WeatherBlack,
    onBackground = WeatherYellow,
    surface = WeatherBlack,
    onSurface = WeatherYellow,
    surfaceVariant = WeatherBlack,
    onSurfaceVariant = WeatherYellow,
    error = Color(0xFFCF6679),
    onError = Color.Black,
    errorContainer = Color(0xFF5C1A1A),
    onErrorContainer = Color(0xFFFCDAE1),
    outline = WeatherYellow,
    inverseSurface = WeatherYellow,
    inverseOnSurface = WeatherBlack,
    inversePrimary = WeatherBlack,
    surfaceTint = WeatherYellow
)

enum class ThemeType {
    DEFAULT,
    BOLD
}

@Composable
fun WeatherFinderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    themeType: ThemeType = ThemeType.BOLD,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeType) {
        ThemeType.BOLD -> if (darkTheme) BoldDarkColorScheme else BoldLightColorScheme
        ThemeType.DEFAULT -> when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
