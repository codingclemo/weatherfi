package com.codingclemo.weatherfinder.ui.locations.details.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.codingclemo.weatherfinder.domain.model.HourlyForecast
import com.codingclemo.weatherfinder.ui.components.WeatherImage
import com.codingclemo.weatherfinder.ui.theme.WeatherFinderTheme
import com.codingclemo.weatherfinder.util.Fixtures
import com.codingclemo.weatherfinder.util.Formatter
import com.codingclemo.weatherfinder.util.Formatter.formatTimestamp


@Composable
fun HourlyForecastList(
    hourlyForecasts: List<HourlyForecast>,
) {
    val roundedCorner = 20.dp

    LazyRow(
        modifier = Modifier
            .padding(horizontal = 18.dp)
            .clip(RoundedCornerShape(roundedCorner))
            .border(2.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(roundedCorner)),
        contentPadding = PaddingValues(horizontal = 0.dp),
    ) {
        itemsIndexed(hourlyForecasts.sortedBy { it.timestamp }) { index, forecast ->
            HourlyForecastItem(
                temperature = forecast.temperature,
                probabilityOfPrecipitation = forecast.probabilityOfprecipitation,
                iconCode = forecast.iconCode,
                timestamp = forecast.timestamp
            )
        }
    }
}

@Composable
fun HourlyForecastItem(
    temperature: Double,
    probabilityOfPrecipitation: Double,
    iconCode: String,
    timestamp: Long,
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(0.3f)
            .defaultMinSize(minWidth = 60.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                text = "${temperature.toInt()}°",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            WeatherImage(
                iconCode = iconCode,
                modifier = Modifier.size(32.dp)
            )

            Text(
                text = Formatter.formatProbabilityOfPrecipitation(probabilityOfPrecipitation),
                style = MaterialTheme.typography.bodyMedium,
                color = if (probabilityOfPrecipitation > 0.01) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.background,
            )

            Text(
                text = formatTimestamp(timestamp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun WeatherDetailsListPreview() {
    WeatherFinderTheme {
        HourlyForecastList(
            hourlyForecasts = Fixtures.createHourlyForecasts()
        )
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HourlyForecastViewPreview() {
    WeatherFinderTheme {
        HourlyForecastItem(
            temperature = 22.0,
            probabilityOfPrecipitation = 0.32,
            iconCode = "10d",
            timestamp = 1633024000,
        )
    }
}
