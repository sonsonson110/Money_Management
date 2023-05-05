package com.example.moneymanagement.ui.chart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf

@Composable
fun CustomChart(
    transactionMonthSumMap: Map<String, Float>
) {
    //list properties get from map
    val recentMonths = transactionMonthSumMap.keys.toList().reversed()  //older months to the left
    val sumOfMonths = transactionMonthSumMap.values.toList().reversed()

    //from list to entries
    val entries = sumOfMonths.mapIndexed { index, value -> entryOf(index.toFloat(), value) }

    if (recentMonths.isEmpty()) return
    //data for compose chart
    val chartEntryModelProducer1 = ChartEntryModelProducer(entries)
    val chartEntryModelProducer2 = ChartEntryModelProducer(entries)
    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> recentMonths[x.toInt() % recentMonths.size] }

    //chart type
    //column
    val defaultColumns = currentChartStyle.columnChart.columns
    val columnChart = columnChart(
        columns = remember(defaultColumns) {
            defaultColumns.map { defaultColumn ->
                LineComponent(
                    defaultColumn.color,
                    defaultColumn.thicknessDp,
                    Shapes.rectShape,
                )
            }
        }
    )
    //line
    val pointConnector = DefaultPointConnector(cubicStrength = 0f)
    val defaultLines = currentChartStyle.lineChart.lines
    val lineChart = lineChart(
        remember(defaultLines) {
            defaultLines.map { defaultLine -> defaultLine.copy(pointConnector = pointConnector) }
        },
    )

    Column(Modifier.fillMaxWidth()) {
        Chart(
            chart = columnChart,
            chartModelProducer = chartEntryModelProducer1,
            startAxis = startAxis(
                maxLabelCount = 5
            ),
            bottomAxis = bottomAxis(
                valueFormatter = bottomAxisValueFormatter
            )
        )

        Chart(
            chart = lineChart,
            chartModelProducer = chartEntryModelProducer2,
            startAxis = startAxis(
                maxLabelCount = 5
            ),
            bottomAxis = bottomAxis(
                valueFormatter = bottomAxisValueFormatter
            )
        )
    }
}
