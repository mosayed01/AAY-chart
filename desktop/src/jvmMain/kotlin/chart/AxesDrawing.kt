package chart


import ChartDefault
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import composables.CustomDropDownMenu
import model.*

@OptIn(ExperimentalTextApi::class)
@Composable
fun AxesDrawing(
    modifier: Modifier = Modifier,
    linesParameters: List<LineParameters> = ChartDefault.chartUiState.lines[0].linesParameters,
    backGroundGrid: BackGroundGrid = ChartDefault.chartUiState.backGroundGrid,
    backGroundColor: Color = ChartDefault.chartUiState.backGroundColor,
    xAxisData: List<String> = ChartDefault.chartUiState.lines[0].xAxisData,
) {
    val spacing = 130f
    val upperValue = remember {
        linesParameters.flatMap { it.data }.maxOrNull()?.plus(1.0) ?: 0.0
    }
    val lowerValue = remember {
        linesParameters.flatMap { it.data }.minOrNull() ?: 0.0
    }

    val yAxis = mutableListOf<Float>()
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 16f), 0f)

    val textMeasure = rememberTextMeasurer()
    Box {
        Column {
            Canvas(modifier = modifier) {
                val spaceBetweenXes = (size.width - spacing) / (linesParameters[0].data.size - 1)
                val barWidthPx = 0.2.dp.toPx()


                xAxisData.forEachIndexed { index, dataPoint ->
                    val xLength = spacing + index * spaceBetweenXes
                    // for x coordinate
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            textMeasurer = textMeasure, text = dataPoint,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Gray
                            ),
                            topLeft = Offset(xLength, size.height / 1.07f)
                        )
                    }

                    // for y coordinate
                    val priceRange = upperValue - lowerValue
                    val priceStep = priceRange / 5f

                    (0..4).forEach { i ->
                        drawContext.canvas.nativeCanvas.apply {
                            val yValue = lowerValue + priceStep * i

                            drawText(
                                textMeasurer = textMeasure, text = yValue.toInt().toString(),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                ),
                                topLeft = Offset(0f, size.height - spacing - i * size.height / 8f)
                            )
                        }
                    }


                    //background line
                    (0..4).forEach { i ->
                        yAxis.add(size.height - spacing - i * size.height / 8f)
                        drawLine(
                            backGroundColor,
                            start = Offset(spacing - 10, yAxis[i] + 12f),
                            end = Offset(xLength + 55, yAxis[i] + 12f),
                            strokeWidth = barWidthPx,
                            pathEffect = pathEffect
                        )
                    }


                    // lines drawing
                    linesParameters.forEach { line ->
                        if (line.lineType == LineType.DEFAULT_LINE) {
                            val strokePathDefault = Path().apply {
                                val height = size.height
                                line.data.indices.forEach { i ->
                                    val info = line.data[i]
                                    val ratio = (info.toFloat() - lowerValue) / (upperValue - lowerValue)

                                    val x1 = spacing + i * spaceBetweenXes
                                    val y1 = height - spacing - (ratio * height).toFloat()

                                    if (i == 0) {
                                        moveTo(x1, y1)
                                    }
                                    lineTo(x1, y1)
                                }
                            }

                            drawPath(
                                path = strokePathDefault,
                                color = line.lineColor,
                                style = Stroke(
                                    width = 3.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            )
                            if (line.lineShadow == LineShadow.SHADOW) {
                                val fillPath = strokePathDefault.apply {
                                    lineTo(size.width - spaceBetweenXes, size.height - spacing)
                                    lineTo(spacing, size.height - spacing)
                                    close()
                                }

                                drawPath(
                                    path = fillPath,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            line.lineColor.copy(alpha = .3f),
                                            Color.Transparent
                                        ),
                                        endY = size.height - spacing
                                    )
                                )
                            }
                        } else {
                            var medX: Float
                            var medY: Float
                            val strokePath = Path().apply {
                                val height = size.height
                                line.data.indices.forEach { i ->
                                    val nextInfo = line.data.getOrNull(i + 1) ?: line.data.last()
                                    val firstRatio = (line.data[i].toFloat() - lowerValue) / (upperValue - lowerValue)
                                    val secondRatio = (nextInfo.toFloat() - lowerValue) / (upperValue - lowerValue)

                                    val x1 = spacing + i * spaceBetweenXes
                                    val y1 = height - spacing - (firstRatio * height).toFloat()
                                    val x2 = spacing + (i + 1) * spaceBetweenXes
                                    val y2 = height - spacing - (secondRatio * height).toFloat()
                                    if (i == 0) {
                                        moveTo(x1, y1)
                                    } else {
                                        medX = (x1 + x2) / 2f
                                        medY = (y1 + y2) / 2f
                                        quadraticBezierTo(x1 = x1, y1 = y1, x2 = medX, y2 = medY)
                                    }
                                }
                            }
                            drawPath(
                                path = strokePath,
                                color = line.lineColor,
                                style = Stroke(
                                    width = 3.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            )

                            if (line.lineShadow == LineShadow.SHADOW) {
                                val fillPath = strokePath.apply {
                                    lineTo(size.width - spaceBetweenXes, size.height - spacing)
                                    lineTo(spacing, size.height - spacing)
                                    close()
                                }

                                drawPath(
                                    path = fillPath,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            line.lineColor.copy(alpha = .3f),
                                            Color.Transparent
                                        ),
                                        endY = size.height - spacing
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalTextApi::class)
@Composable
fun AxesDrawing(
    modifier: Modifier = Modifier,
    lines: List<Lines> = ChartDefault.chartUiState.lines,
    backGroundGrid: BackGroundGrid = ChartDefault.chartUiState.backGroundGrid,
    backGroundColor: Color = ChartDefault.chartUiState.backGroundColor,
    showDropDownItems:Boolean=true,
){
    val spacing = 130f
    Box {
            var expanded by remember { mutableStateOf(false) }
            val suggestions = listOf("Week", "Month", "Year")
            var selectedText by remember { mutableStateOf("Month") }
            var textFieldSize by remember { mutableStateOf(Size.Zero)}
            val icon = if (expanded)
                Icons.Filled.KeyboardArrowUp
            else
                Icons.Filled.KeyboardArrowDown
            Column(Modifier.padding(20.dp).width(120.dp)) {
                OutlinedTextField(
                    value = selectedText,
                    onValueChange = { selectedText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            //This value is used to assign to the DropDown the same width
                            textFieldSize = coordinates.size.toSize()
                        },
                    trailingIcon = {
                        Icon(icon,"contentDescription",
                            Modifier.clickable { expanded = !expanded })
                    },
                    shape = RoundedCornerShape(300f)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current){textFieldSize.width.toDp()})
                ) {
                    suggestions.forEach { label ->
                        DropdownMenuItem(onClick = {
                            selectedText = label
                            expanded = false
                        }) {
                            Text(text = label)
                        }
                    }
                }
            }

       val linesWithTimePeriod= when(selectedText){
            "Month"->lines.filter {it.timePeriod==TimePeriod.MONTHLY }
            "Week"->lines.filter {it.timePeriod==TimePeriod.WEEKLY }
            "Year"->lines.filter {it.timePeriod==TimePeriod.YEARLY }
           else -> {
               lines.filter {it.timePeriod==TimePeriod.YEARLY }
           }
       }

        val upperValue = remember {
            linesWithTimePeriod[0].linesParameters.flatMap { it.data }.maxOrNull()?.plus(1.0) ?: 0.0
        }
        val lowerValue = remember {
            linesWithTimePeriod[0].linesParameters.flatMap { it.data }.minOrNull() ?: 0.0
        }
        val yAxis = mutableListOf<Float>()
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 16f), 0f)

        val textMeasure = rememberTextMeasurer()
        Column {
            Canvas(modifier = modifier) {
                val spaceBetweenXes = (size.width - spacing) / (linesWithTimePeriod[0].linesParameters[0].data.size - 1)
                val barWidthPx = 0.2.dp.toPx()


                linesWithTimePeriod[0].xAxisData.forEachIndexed { index, dataPoint ->
                    val xLength = spacing + index * spaceBetweenXes
                    // for x coordinate
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            textMeasurer = textMeasure, text = dataPoint,
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = Color.Gray
                            ),
                            topLeft = Offset(xLength, size.height / 1.07f)
                        )
                    }

                    // for y coordinate
                    val priceRange = upperValue - lowerValue
                    val priceStep = priceRange / 5f

                    (0..4).forEach { i ->
                        drawContext.canvas.nativeCanvas.apply {
                            val yValue = lowerValue + priceStep * i

                            drawText(
                                textMeasurer = textMeasure, text = yValue.toInt().toString(),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                ),
                                topLeft = Offset(0f, size.height - spacing - i * size.height / 8f)
                            )
                        }
                    }


                    //background line
                    (0..4).forEach { i ->
                        yAxis.add(size.height - spacing - i * size.height / 8f)
                        drawLine(
                            backGroundColor,
                            start = Offset(spacing - 10, yAxis[i] + 12f),
                            end = Offset(xLength + 55, yAxis[i] + 12f),
                            strokeWidth = barWidthPx,
                            pathEffect = pathEffect
                        )
                    }


                    // lines drawing
                    linesWithTimePeriod[0].linesParameters.forEach { line ->
                        if (line.lineType == LineType.DEFAULT_LINE) {
                            val strokePathDefault = Path().apply {
                                val height = size.height
                                line.data.indices.forEach { i ->
                                    val info = line.data[i]
                                    val ratio = (info.toFloat() - lowerValue) / (upperValue - lowerValue)

                                    val x1 = spacing + i * spaceBetweenXes
                                    val y1 = height - spacing - (ratio * height).toFloat()

                                    if (i == 0) {
                                        moveTo(x1, y1)
                                    }
                                    lineTo(x1, y1)
                                }
                            }

                            drawPath(
                                path = strokePathDefault,
                                color = line.lineColor,
                                style = Stroke(
                                    width = 3.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            )
                            if (line.lineShadow == LineShadow.SHADOW) {
                                val fillPath = strokePathDefault.apply {
                                    lineTo(size.width - spaceBetweenXes, size.height - spacing)
                                    lineTo(spacing, size.height - spacing)
                                    close()
                                }

                                drawPath(
                                    path = fillPath,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            line.lineColor.copy(alpha = .3f),
                                            Color.Transparent
                                        ),
                                        endY = size.height - spacing
                                    )
                                )
                            }
                        } else {
                            var medX: Float
                            var medY: Float
                            val strokePath = Path().apply {
                                val height = size.height
                                line.data.indices.forEach { i ->
                                    val nextInfo = line.data.getOrNull(i + 1) ?: line.data.last()
                                    val firstRatio = (line.data[i].toFloat() - lowerValue) / (upperValue - lowerValue)
                                    val secondRatio = (nextInfo.toFloat() - lowerValue) / (upperValue - lowerValue)

                                    val x1 = spacing + i * spaceBetweenXes
                                    val y1 = height - spacing - (firstRatio * height).toFloat()
                                    val x2 = spacing + (i + 1) * spaceBetweenXes
                                    val y2 = height - spacing - (secondRatio * height).toFloat()
                                    if (i == 0) {
                                        moveTo(x1, y1)
                                    } else {
                                        medX = (x1 + x2) / 2f
                                        medY = (y1 + y2) / 2f
                                        quadraticBezierTo(x1 = x1, y1 = y1, x2 = medX, y2 = medY)
                                    }
                                }
                            }
                            drawPath(
                                path = strokePath,
                                color = line.lineColor,
                                style = Stroke(
                                    width = 3.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            )

                            if (line.lineShadow == LineShadow.SHADOW) {
                                val fillPath = strokePath.apply {
                                    lineTo(size.width - spaceBetweenXes, size.height - spacing)
                                    lineTo(spacing, size.height - spacing)
                                    close()
                                }

                                drawPath(
                                    path = fillPath,
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            line.lineColor.copy(alpha = .3f),
                                            Color.Transparent
                                        ),
                                        endY = size.height - spacing
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

    }

}