package theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val Neutral4 = Color(0x1f000000)
val Neutral3 = Color(0x1fffffff)

@Composable
fun getBorderColor(): Color = if (isSystemInDarkTheme()) Neutral3 else Neutral4