package composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize



@Composable
fun CustomDropDownHeader(
    selectedText: String,
    onSelectedTextChanged: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("Week", "Month", "Year")
    var textFieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column(Modifier.padding(20.dp).width(110.dp)) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { newText ->
                 onSelectedTextChanged(newText)
                },
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
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults
                .outlinedTextFieldColors(unfocusedBorderColor = androidx.compose.ui.graphics.Color.LightGray)

        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {
                    onSelectedTextChanged(label)
                    expanded = false
                }) {
                    Text(text = label)
                }
            }
        }
    }
}