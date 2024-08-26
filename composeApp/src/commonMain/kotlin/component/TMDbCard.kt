package component

import LocalNavAnimatedVisibilityScope
import LocalSharedTransitionScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import domain.model.Movie
import theme.Neutral3
import theme.Neutral4
import ui.TMDbDetailBoundsTransform
import ui.nonSpatialExpressiveSpring
import utils.Dimens.TMDb_150_dp
import utils.Dimens.TMDb_2_dp
import utils.Dimens.TMDb_4_dp
import utils.Dimens.TMDb_6_dp
import utils.Dimens.TMDb_8_dp
import utils.TMDbSharedElementKey
import utils.TMDbSharedElementType

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TMDbCard(
    movie: Movie,
    onClick: (Movie) -> Unit,
    modifier: Modifier = Modifier
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No sharedTransitionScope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No animatedVisibilityScope found")

    val borderColor: Color = if (isSystemInDarkTheme()) Neutral3 else Neutral4

    with(sharedTransitionScope) {
        val roundedCornerAnimation by animatedVisibilityScope.transition
            .animateDp(label = "rounded corner") { enterExit: EnterExitState ->
                when (enterExit) {
                    EnterExitState.PreEnter -> 0.dp
                    EnterExitState.Visible -> TMDb_8_dp
                    EnterExitState.PostExit -> TMDb_8_dp
                }
            }
        TMDbSurface(
            shape = RoundedCornerShape(roundedCornerAnimation),
            modifier = modifier
                .sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        key = TMDbSharedElementKey(
                            movieId = movie.id,
                            type = TMDbSharedElementType.Bounds
                        )
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = TMDbDetailBoundsTransform,
                    clipInOverlayDuringTransition = OverlayClip(
                        RoundedCornerShape(
                            roundedCornerAnimation
                        )
                    ),
                    enter = fadeIn(),
                    exit = fadeOut()
                ).border(
                    1.dp,
                    borderColor.copy(alpha = 0.12f),
                    RoundedCornerShape(roundedCornerAnimation)
                ).clickable {
                    onClick.invoke(movie)
                }
        ) {
            Column {
                AsyncImage(
                    model = movie.backdropPath,
                    contentDescription = null,
                    modifier = Modifier.sharedBounds(
                        rememberSharedContentState(
                            key = TMDbSharedElementKey(
                                movieId = movie.id,
                                type = TMDbSharedElementType.Image
                            )
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = TMDbDetailBoundsTransform
                    ).height(TMDb_150_dp)
                        .fillMaxWidth()
                        .clip(shape = MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
                TMDbItemInfo(
                    movie,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
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
        TMDbItemName(movie = movie)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            movie.releaseDate?.let {
                TMDbItemFeature(
                    Icons.Default.DateRange,
                    it,
                    movie.id,
                    TMDbSharedElementType.ReleaseDate
                )
            }
            TMDbItemFeature(
                Icons.Default.ThumbUp,
                movie.voteCount.toString(),
                movie.id,
                TMDbSharedElementType.VOTE
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun TMDbItemName(movie: Movie) {

    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No sharedTransitionScope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No animatedVisibilityScope found")
    with(sharedTransitionScope) {
        Text(
            text = movie.name,
            style = MaterialTheme.typography.subtitle1.copy(
                color = MaterialTheme.colors.onSurface,
                letterSpacing = 1.5.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.W500
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.sharedBounds(
                rememberSharedContentState(
                    key = TMDbSharedElementKey(
                        movieId = movie.id,
                        type = TMDbSharedElementType.Title
                    )
                ),
                animatedVisibilityScope = animatedVisibilityScope,
                enter = fadeIn(nonSpatialExpressiveSpring()),
                exit = fadeOut(nonSpatialExpressiveSpring()),
                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(),
                boundsTransform = TMDbDetailBoundsTransform
            )
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun TMDbItemFeature(
    icon: ImageVector,
    field: String,
    movieId: Int,
    type: TMDbSharedElementType
) {

    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No sharedTransitionScope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No animatedVisibilityScope found")
    with(sharedTransitionScope) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = field,
                style = MaterialTheme.typography.subtitle2.copy(
                    color = MaterialTheme.colors.onSurface,
                    letterSpacing = 1.5.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.W400
                ),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = TMDb_2_dp)
                    .sharedBounds(
                        rememberSharedContentState(
                            key = TMDbSharedElementKey(
                                movieId = movieId,
                                type = type
                            )
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        enter = fadeIn(nonSpatialExpressiveSpring()),
                        exit = fadeOut(nonSpatialExpressiveSpring()),
                        boundsTransform = TMDbDetailBoundsTransform,
                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                    )
            )
        }
    }
}

@Composable
fun TMDbSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = MaterialTheme.colors.background,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .shadow(elevation = elevation, shape = shape, clip = false)
            .zIndex(elevation.value)
            .background(
                color = color,
                shape = shape
            ).clip(shape)
    ) {
        content.invoke()
    }
}