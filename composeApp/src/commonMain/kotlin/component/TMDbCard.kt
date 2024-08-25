package component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import domain.model.Movie
import theme.rateColors
import utils.Dimens.TMDb_12_dp
import utils.Dimens.TMDb_150_dp
import utils.Dimens.TMDb_2_dp
import utils.Dimens.TMDb_4_dp
import utils.Dimens.TMDb_6_dp
import utils.Dimens.TMDb_8_dp

@OptIn(ExperimentalMaterialApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TMDbCard(
    movie: Movie,
    onClick: (Movie) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        TMDbItemRate(
            movie.voteAverage,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(2f)
        )
        Card(
            modifier = Modifier
                .offset(y = TMDb_12_dp),
            shape = RoundedCornerShape(size = TMDb_8_dp),
            elevation = TMDb_8_dp,
            onClick = { onClick.invoke(movie) }
        ) {
            Box {
                AsyncImage(
                    model = movie.backdropPath,
                    contentDescription = null,
                    modifier = Modifier.sharedElement(
                        state = rememberSharedContentState(key = movie.id),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(400)
                        }
                    ).height(TMDb_150_dp)
                        .fillMaxWidth()
                        .clip(shape = MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
                TMDbItemInfo(
                    movie,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(Color(0x97000000))
                )
            }
        }
    }
}

@Composable
private fun TMDbItemRate(rate: Double, modifier: Modifier) {
    val shape = RoundedCornerShape(percent = 50)
    Surface(
        shape = shape,
        elevation = TMDb_12_dp,
        modifier = modifier
    ) {
        Text(
            text = rate.toString(),
            style = MaterialTheme.typography.body1.copy(color = Color.White),
            modifier = Modifier
                .background(Brush.horizontalGradient(Color.rateColors(movieRate = rate)))
                .padding(horizontal = 10.dp)
        )
    }
}

@Composable
private fun TMDbItemInfo(movie: Movie, modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(TMDb_4_dp),
        modifier = modifier.padding(
            horizontal = TMDb_6_dp,
            vertical = TMDb_4_dp
        )
    ) {
        TMDbItemName(name = movie.name)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            movie.releaseDate?.let { TMDbItemFeature(Icons.Default.DateRange, it) }
            TMDbItemFeature(Icons.Default.ThumbUp, movie.voteCount.toString())
        }
    }
}

@Composable
private fun TMDbItemName(name: String) = Text(
    text = name,
    style = MaterialTheme.typography.subtitle1.copy(
        color = Color.White,
        letterSpacing = 1.5.sp,
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.W500
    ),
    maxLines = 1,
    overflow = TextOverflow.Ellipsis
)

@Composable
private fun TMDbItemFeature(icon: ImageVector, field: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(13.dp)
        )
        Text(
            text = field,
            style = MaterialTheme.typography.subtitle2.copy(
                color = Color.White,
                letterSpacing = 1.5.sp,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.W400
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = TMDb_2_dp)
        )
    }
}