package model

data class Lines(
    val linesParameters: List<LineParameters>,
    val xAxisData: List<String>,
    val timePeriod: TimePeriod,
    val xAxisLabel: String,
    val yAxisLabel: String,
)

enum class TimePeriod{
    MONTHLY,
    WEEKLY,
    YEARLY,
}