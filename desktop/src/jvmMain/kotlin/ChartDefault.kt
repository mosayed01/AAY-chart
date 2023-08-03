import androidx.compose.ui.graphics.Color
import model.*

object ChartDefault {

    private val lineParameters: LineParameters = LineParameters(
        dataName = "revenue",
        data = emptyList(),
        lineColor = Color.Blue,
        lineType = LineType.QUADRATIC_LINE,
        lineShadow = LineShadow.SHADOW,
    )

    private val lines: Lines = Lines(
        linesParameters = listOf(lineParameters),
        xAxisLabel = "month",
        yAxisLabel = "money",
        xAxisData = emptyList(),
        timePeriod = TimePeriod.MONTHLY,
    )
    val chartUiState:ChartUiState= ChartUiState(
        lines = listOf(lines),
        backGroundColor = Color.Transparent,
        backGroundGrid = BackGroundGrid.SHOW,
    )
}