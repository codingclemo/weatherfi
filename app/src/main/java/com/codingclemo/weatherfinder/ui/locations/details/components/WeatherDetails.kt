package com.codingclemo.weatherfinder.ui.locations.details.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.Waves
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codingclemo.weatherfinder.R
import com.codingclemo.weatherfinder.ui.theme.WeatherFinderTheme
import com.codingclemo.weatherfinder.util.Fixtures
import com.codingclemo.weatherfinder.util.Formatter


@Composable
fun WeatherDetails(
    windSpeed: Double,
    windDirection: Int,
    humidity: Int,
    precipitation: Double,
) {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .height(150.dp)
            .padding(horizontal = 16.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(size = 20.dp)
            )
    ) {
        WeatherDetailsItem(
            metricName = stringResource(R.string.weather_metric_wind),
            metricValue = Formatter.formatWindSpeed(windSpeed),
            imageVector = Icons.Outlined.Navigation,
            rotateImage = true,
            iconRotation = windDirection.toFloat(),
        )
        WeatherDetailsItem(
            metricName = stringResource(R.string.weather_metric_humidity),
            metricValue = Formatter.formatHumidity(humidity),
            imageVector = Icons.Outlined.Waves,
        )
        WeatherDetailsItem(
            metricName = stringResource(R.string.weather_metric_precipitation),
            metricValue = Formatter.formatProbabilityOfPrecipitation(precipitation),
            imageVector = Icons.Outlined.WaterDrop,
        )
    }
}

@Composable
private fun WeatherDetailsItem(
    metricName: String,
    metricValue: String,
    imageVector: ImageVector,
    rotateImage: Boolean = false,
    iconRotation: Float = 0f,
) {
    val rotationAnimated = animateFloatAsState(
        targetValue = iconRotation - 180f,
        animationSpec = tween(durationMillis = 1200),
        label = "imageRotation",
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxHeight()
            .width(100.dp)
    ) {
        Image(
            modifier = Modifier
                .size(64.dp)
                .rotate(if (rotateImage) rotationAnimated.value else 0f),
            imageVector = imageVector,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.onPrimary
            ),
            contentDescription = null,
        )

        Text(
            text = metricValue,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = metricName,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun WeatherDetailsPreview() {
    WeatherFinderTheme {
        WeatherDetails(
            windSpeed = Fixtures.createWeather().windSpeed,
            windDirection = Fixtures.createWeather().windDirection,
            humidity = Fixtures.createWeather().humidity,
            precipitation = Fixtures.createDailyForecasts()[0].avgProbabilityOfPrecipitation,
        )
    }
}