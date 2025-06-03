package com.codingclemo.weatherfinder.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.codingclemo.weatherfinder.R
import com.codingclemo.weatherfinder.ui.theme.WeatherFinderTheme
import com.codingclemo.weatherfinder.util.Utils.getIconUrl

@Composable
fun WeatherImage(
    iconCode: String,
    contentDescription: String? = null,
    modifier: Modifier = Modifier.size(48.dp)
) {

    val isInPreview = LocalInspectionMode.current

    if (isInPreview) {
        // Code to display a static image or placeholder for preview
        // Example: using a drawable resource
        Image(
            painter = painterResource(id = R.drawable.img_weather_placeholder),
            contentDescription = null,
            modifier = modifier
        )
    } else {
        // Code to display the AsyncImage for the actual app
        AsyncImage(
            model = getIconUrl(iconCode),
            contentDescription = null,
            modifier = modifier
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WeatherImagePreview() {
    WeatherFinderTheme {
        WeatherImage(iconCode = "10d")
    }
}