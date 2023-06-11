package com.example.moneymanagement.ui.chart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.axisLabelComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf

@Composable
fun CustomChart(
    groupTransactions: Map<String, Float>,
) {
    //list properties get from map
    //MONTHS
    val weekList = groupTransactions.keys.toList().reversed()  //older months to the left
    val weekSumList = groupTransactions.values.toList().reversed()

    //from list to monthEntries
    val weekEntries = weekSumList.mapIndexed { index, value -> entryOf(index.toFloat(), value) }

    //skip stateflow init part
    if (weekList.isEmpty()) return

    //data for compose chart
    //weeks
    val chartEntryModelProducer = ChartEntryModelProducer(weekEntries)
    val bottomAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> weekList[x.toInt() % weekList.size] }

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

    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Thống kê tổng cộng ${weekList.size} tuần",
            Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption
        )
        Chart(
            chart = columnChart,
            chartModelProducer = chartEntryModelProducer,
            startAxis = startAxis(),
            bottomAxis = bottomAxis(
                valueFormatter = bottomAxisValueFormatter,
                labelRotationDegrees = -30f,
                label = axisLabelComponent(textSize = 6.sp)
            ),
            marker = rememberMarker()
        )
    }

}
