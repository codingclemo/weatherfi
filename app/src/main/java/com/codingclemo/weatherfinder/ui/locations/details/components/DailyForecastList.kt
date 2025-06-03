package com.codingclemo.weatherfinder.ui.locations.details.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import com.codingclemo.weatherfinder.domain.model.DailyForecast
import com.codingclemo.weatherfinder.ui.components.WeatherImage
import com.codingclemo.weatherfinder.ui.theme.WeatherFinderTheme
import com.codingclemo.weatherfinder.util.Fixtures
import com.codingclemo.weatherfinder.util.Formatter

@Composable
fun DailyForecastList(
    dailyForecasts: List<DailyForecast>,
    selectedIndex: Int,
    onForecastSelected: (Int) -> Unit
) {

    LazyRow(
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp
        )
    ) {
        itemsIndexed(dailyForecasts) { index, dailyForecast ->
            DailyForecastItem(
                timestamp = dailyForecast.timestamp,
                iconId = dailyForecast.iconCode,
                minTemperature = dailyForecast.minTemperature,
                maxTemperature = dailyForecast.maxTemperature,
                isSelected = index == selectedIndex,
                onClick = { onForecastSelected(index) }
            )
        }
    }
}

@Composable
private fun DailyForecastItem(
    timestamp: Long,
    iconId: String,
    minTemperature: Double,
    maxTemperature: Double,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(20.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
        ),

        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.onPrimaryContainer) else null,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                )
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text(
                    text = Formatter.formatTemperature(maxTemperature),
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Text(
                    text = "/${Formatter.formatTemperature(minTemperature)}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
            WeatherImage(iconCode = iconId, contentDescription = null)
            Text(
                text = Formatter.formatTimestamp(timestamp, pattern = "MMM dd"),
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
    }
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DailyForecastListPreview() {
    WeatherFinderTheme {
        DailyForecastList(
            dailyForecasts = Fixtures.createDailyForecasts(),
            selectedIndex = 0,
            onForecastSelected = {}
        )
    }
}