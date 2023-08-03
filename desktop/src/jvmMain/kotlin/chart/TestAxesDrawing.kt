package chart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.*

@Composable
fun TestAxesDrawing() {

    val lineParameters = LineParameters(
        dataName = "revenue",
        data = listOf(
            80000.0,
            50000.0,
            20000.0,
            30000.0,
            40000.0,
            10000.0,
            70000.0,
            90000.0,
        ),
        lineColor = Color.Blue,
        lineType = LineType.QUADRATIC_LINE,
        lineShadow = LineShadow.SHADOW
    )

    val xAxisListMonth = listOf(
        "Jan",
        "Feb",
        "march",
        "april",
    )
    val xAxisListYear = listOf(
        "2012",
        "2013",
        "2014",
        "2015",
    )
    val xAxisListWeek = listOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
    )

    val lineParameters2 = LineParameters(
        dataName = "revenue",
        data = listOf(
            80000.0,
            20000.0,
            30000.0,
            80000.0,
            90000.0,
            50000.0,
            40000.0,
            90000.0,
        ),
        lineColor = Color.Red,
        lineType = LineType.QUADRATIC_LINE,
        lineShadow = LineShadow.BLANK
    )
    val lineParameters3 = LineParameters(
        dataName = "revenue",
        data = listOf(
            90000.0,
            70000.0,
            30000.0,
            50000.0,
            40000.0,
            90000.0,
            50000.0,
            90000.0,
        ),
        lineColor = Color.Red,
        lineType = LineType.QUADRATIC_LINE,
        lineShadow = LineShadow.BLANK
    )
    val lineParameters4 = LineParameters(
        dataName = "revenue",
        data = listOf(
            60000.0,
            40000.0,
            30000.0,
            70000.0,
            20000.0,
            50000.0,
            20000.0,
            90000.0,
        ),
        lineColor = Color.Red,
        lineType = LineType.QUADRATIC_LINE,
        lineShadow = LineShadow.BLANK
    )
    val lineParameters5 = LineParameters(
        dataName = "revenue",
        data = listOf(
            50000.0,
            20000.0,
            30000.0,
            30000.0,
            20000.0,
            40000.0,
            10000.0,
            90000.0,
        ),
        lineColor = Color.Red,
        lineType = LineType.QUADRATIC_LINE,
        lineShadow = LineShadow.BLANK
    )
    val lineParameters6 = LineParameters(
        dataName = "revenue",
        data = listOf(
            20000.0,
            40000.0,
            70000.0,
            50000.0,
            20000.0,
            10000.0,
            80000.0,
            90000.0,
        ),
        lineColor = Color.Red,
        lineType = LineType.QUADRATIC_LINE,
        lineShadow = LineShadow.BLANK
    )

    val linesMonth = Lines(
        linesParameters = listOf(lineParameters, lineParameters2),
        xAxisLabel = "month",
        yAxisLabel = "money",
        xAxisData = xAxisListMonth,
        timePeriod = TimePeriod.MONTHLY
    )
    val linesWeek = Lines(
        linesParameters = listOf(lineParameters3, lineParameters4),
        xAxisLabel = "month",
        yAxisLabel = "money",
        xAxisData = xAxisListWeek,
        timePeriod = TimePeriod.WEEKLY
    )
    val linesYear = Lines(
        linesParameters = listOf(lineParameters5, lineParameters6),
        xAxisLabel = "month",
        yAxisLabel = "money",
        xAxisData = xAxisListYear,
        timePeriod = TimePeriod.YEARLY
    )
    val combineLines= listOf(linesMonth,linesWeek,linesYear)

    // for x or y this list?????
    val revenueData = listOf(
        Pair("Jan", 50000),
        Pair("Feb", 40000),
        Pair("Mar", 30000),
        Pair("Apr", 50000),
        Pair("May", 40000),
        Pair("Aug", 20000),
        Pair("Sep", 10000),
        Pair("Oct", 50000),
    )

    Column(modifier = Modifier.fillMaxSize()) {
        AxesDrawing(
            modifier = Modifier.size(500.dp)
                .align(Alignment.CenterHorizontally)
                .padding(top = 24.dp),
            lines =combineLines
        )
    }
}