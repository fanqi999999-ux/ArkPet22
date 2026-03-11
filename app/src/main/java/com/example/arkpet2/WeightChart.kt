package com.example.arkpet2

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun WeightChartPreview() {
    WeightChart(
        records = listOf(
            WeightRecord(value = 3.2, unit = "kg", timeMillis = 0L)
        )
    )
}
