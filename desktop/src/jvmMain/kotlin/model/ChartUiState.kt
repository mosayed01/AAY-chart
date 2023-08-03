package model

import androidx.compose.ui.graphics.Color

data class ChartUiState(
    val lines: List<Lines>,
    val backGroundGrid: BackGroundGrid,
    val backGroundColor: Color,
)
enum class BackGroundGrid {
    SHOW,
    BLANK,
}

