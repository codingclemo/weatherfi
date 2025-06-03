package com.codingclemo.weatherfinder.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun CenteredComposableWithSuffix(
    prefix: @Composable (() -> Unit)? = null,
    center: @Composable () -> Unit,
    suffix: @Composable (() -> Unit)? = null,
    showSuffix: Boolean = true,
    showPrefix: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Layout(
        content = {
            if (showPrefix && prefix != null) {
                prefix()
            }
            center()
            if (showSuffix && suffix != null) {
                suffix()
            }
        },
        modifier = modifier
    ) { measurables, constraints ->
        val leftSymbolMeasurable = if (showPrefix && prefix != null) measurables.getOrNull(0) else null
        val centeredTextMeasurable = if (showPrefix && prefix != null) measurables.getOrNull(1) else measurables.getOrNull(0)
        val suffixTextMeasurable = if (showSuffix && suffix != null) {
            if (showPrefix && prefix != null) measurables.getOrNull(2) else measurables.getOrNull(1)
        } else null

        requireNotNull(centeredTextMeasurable) { "centeredText must be provided" }

        // Measure the children with the incoming constraints
        val leftSymbolPlaceable = leftSymbolMeasurable?.measure(constraints)
        val centeredTextPlaceable = centeredTextMeasurable.measure(constraints)
        val suffixTextPlaceable = suffixTextMeasurable?.measure(constraints)

        // Calculate the total width needed
        val layoutWidth = constraints.maxWidth
        val layoutHeight = maxOf(
            leftSymbolPlaceable?.height ?: 0,
            centeredTextPlaceable.height,
            suffixTextPlaceable?.height ?: 0
        )

        // Calculate placement positions
        val leftSymbolWidth = leftSymbolPlaceable?.width ?: 0
        val centeredTextX = (layoutWidth - centeredTextPlaceable.width) / 2
        val centeredTextY = 0 // Align tops for simplicity, adjust if needed

        // X for suffix text: starts right after the centered text
        val suffixTextX = centeredTextX + centeredTextPlaceable.width
        val suffixTextY = (layoutHeight - (suffixTextPlaceable?.height ?: 0)) / 2

        // Place the children
        layout(layoutWidth, layoutHeight) {
            leftSymbolPlaceable?.let {
                it.place(
                    x = centeredTextX - it.width - 4, // 4.dp spacing between symbol and centered text
                    y = (layoutHeight - it.height) / 2
                )
            }
            centeredTextPlaceable.place(x = centeredTextX, y = centeredTextY)
            suffixTextPlaceable?.let {
                it.place(x = suffixTextX, y = suffixTextY)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CenteredComposableWithSuffixPreview() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        CenteredComposableWithSuffix(
            center = { Text("21") },
            suffix = { Text("°") },
            showPrefix = true,
            prefix = { Text("Ø") },
            modifier = Modifier.wrapContentSize()
        )
        CenteredComposableWithSuffix(
            center = { Text("1000") },
            suffix = { Text("°") },
            showPrefix = true,
            prefix = { Text("Ø") },
            modifier = Modifier.wrapContentSize()
        )
        // Preview without suffix
        CenteredComposableWithSuffix(
            center = { Text("21") },
            showSuffix = false,
            modifier = Modifier.wrapContentSize()
        )
        // Preview without suffix but with prefix
        CenteredComposableWithSuffix(
            prefix = { Text("Ø") },
            center = { Text("21") },
            showPrefix = true,
            showSuffix = false,
            modifier = Modifier.wrapContentSize()
        )
    }
}