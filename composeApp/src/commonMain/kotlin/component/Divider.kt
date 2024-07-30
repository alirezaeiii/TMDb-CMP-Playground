package component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import utils.Dimens.disney_12_dp

@Composable
fun Divider() {
    Divider(
        modifier = Modifier.padding(horizontal = disney_12_dp, vertical = disney_12_dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.08f)
    )
}