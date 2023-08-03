import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import chart.TestAxesDrawing
import composables.CustomDropDownMenu


fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        TestAxesDrawing()
    }
}
