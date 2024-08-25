package component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import utils.Dimens.TMDb_118_dp
import utils.Dimens.TMDb_12_dp
import utils.Dimens.TMDb_140_dp
import utils.Dimens.TMDb_4_dp
import utils.Dimens.TMDb_6_dp
import utils.Dimens.TMDb_8_dp

@Composable
fun ShimmerLoading() {
    TMDbTopBar {
        LazyVerticalGrid(
            modifier = Modifier.background(MaterialTheme.colors.background).padding(it),
            columns = GridCells.Adaptive(minSize = TMDb_140_dp),
            content = {
                items(count = 20) {
                    ShimmerItem()
                }
            })
    }
}


@Composable
private fun ShimmerItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(TMDb_4_dp)
    ) {
        Box(
            modifier = Modifier
                .height(TMDb_118_dp)
                .fillMaxWidth()
                .background(color = Color.LightGray)
                .clip(shape = MaterialTheme.shapes.medium)
                .shimmerLoadingAnimation(),
        )
        Spacer(Modifier.height(TMDb_8_dp))
        ComponentRectangleLineLong(.7f)
        Spacer(modifier = Modifier.height(TMDb_6_dp))
        ComponentRectangleLineLong(.4f)
        Spacer(modifier = Modifier.height(TMDb_6_dp))
    }
}

@Composable
private fun ComponentRectangleLineLong(
    widthFraction: Float
) {
    Box(
        modifier = Modifier.fillMaxWidth(widthFraction)
            .clip(shape = RoundedCornerShape(TMDb_8_dp))
            .background(color = Color.LightGray)
            .height(TMDb_12_dp)
            .shimmerLoadingAnimation()
    )
}

private fun Modifier.shimmerLoadingAnimation(
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
): Modifier = composed {
    val shimmerColors = ShimmerAnimationData(isLightMode = !isSystemInDarkTheme()).getColours()

    val transition = rememberInfiniteTransition(label = "")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )

    this.background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
            end = Offset(x = translateAnimation.value, y = angleOfAxisY),
        ),
    )
}

data class ShimmerAnimationData(
    private val isLightMode: Boolean
) {
    fun getColours(): List<Color> {
        return if (isLightMode) {
            val color = Color.White

            listOf(
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 1.0f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 0.3f),
            )
        } else {
            val color = Color.Black

            listOf(
                color.copy(alpha = 0.0f),
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.0f),
            )
        }
    }
}