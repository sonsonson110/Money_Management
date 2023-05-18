package com.example.moneymanagement.ui.chart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
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
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf

@Composable
fun CustomChart(
    transactionMonthSumMap: Map<String, Float>,
    transactionWeekSumMap: Map<String, Float>
) {
    //list properties get from map
    //MONTHS
    val recentMonths = transactionMonthSumMap.keys.toList().reversed()  //older months to the left
    val sumOfMonths = transactionMonthSumMap.values.toList().reversed()
    //WEEK
    val recentWeeks = transactionWeekSumMap.keys.toList().reversed()  //older weeks to the left
    val sumOfWeeks = transactionWeekSumMap.values.toList().reversed()

    //from list to monthEntries
    val monthEntries = sumOfMonths.mapIndexed { index, value -> entryOf(index.toFloat(), value) }
    val weekEntries = sumOfWeeks.mapIndexed { index, value -> entryOf(index.toFloat(), value) }

    //skip stateflow init part
    if (recentMonths.isEmpty() || recentWeeks.isEmpty()) return

    //data for compose chart
    //MONTHS
    val chartEntryModelProducer1 = ChartEntryModelProducer(monthEntries)
    val bottomAxisValueFormatter1 =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> recentMonths[x.toInt() % recentMonths.size] }
    //WEEKS
    val chartEntryModelProducer2 = ChartEntryModelProducer(weekEntries)
    val bottomAxisValueFormatter2 =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> recentWeeks[x.toInt() % recentWeeks.size] }

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
        Text("Thống kế ${recentMonths.size} tháng gần đây")
        Chart(
            chart = columnChart,
            chartModelProducer = chartEntryModelProducer1,
            startAxis = startAxis(
                maxLabelCount = 5
            ),
            bottomAxis = bottomAxis(
                valueFormatter = bottomAxisValueFormatter1
            )
        )
        Text("Thống kế ${recentWeeks.size} tuần gần đây")
        Chart(
            chart = lineChart,
            chartModelProducer = chartEntryModelProducer2,
            startAxis = startAxis(
                maxLabelCount = 5
            ),
            bottomAxis = bottomAxis(
                valueFormatter = bottomAxisValueFormatter2,
                label = axisLabelComponent(textSize = 9.sp),
                labelRotationDegrees = 15f
            ),
        )
    }
}
