package com.codingclemo.weatherfinder.ui.locations.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.codingclemo.weatherfinder.ui.components.CenteredComposableWithSuffix
import com.codingclemo.weatherfinder.ui.theme.WeatherFinderTheme
import com.codingclemo.weatherfinder.util.Fixtures

@Composable
fun MainTemperature(
    temperature: Double,
    showAverage: Boolean = false,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenteredComposableWithSuffix(
            prefix = {
                Text(text = "~",
                    fontSize = 60.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                )
            },
            showPrefix = showAverage,
            center = {
                Text(
                    text = "${temperature.toInt()}",
                    fontSize = 125.sp,
                )
            },
            suffix = {
                Text(
                    text = "°",
                    fontSize = 125.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFEB3B)
@Composable
private fun MainTemperaturePreview() {
    WeatherFinderTheme {
        Column {
            MainTemperature(temperature = Fixtures.createWeather().temperature)
            MainTemperature(temperature = Fixtures.createWeather().temperature, showAverage = true)
        }
    }
}