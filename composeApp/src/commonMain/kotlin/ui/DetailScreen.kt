package ui

import LocalNavAnimatedVisibilityScope
import LocalSharedTransitionScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import domain.model.Movie
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import tmdb_compose_multiplatform.composeapp.generated.resources.Res
import tmdb_compose_multiplatform.composeapp.generated.resources.release_date
import tmdb_compose_multiplatform.composeapp.generated.resources.vote_average
import tmdb_compose_multiplatform.composeapp.generated.resources.vote_count
import utils.Dimens.TMDb_4_dp
import utils.Dimens.TMDb_8_dp
import utils.TMDbSharedElementKey
import utils.TMDbSharedElementType

fun <T> spatialExpressiveSpring() = spring<T>(
    dampingRatio = 0.8f,
    stiffness = 380f
)

fun <T> nonSpatialExpressiveSpring() = spring<T>(
    dampingRatio = 1f,
    stiffness = 1600f
)

@OptIn(ExperimentalSharedTransitionApi::class)
val TMDbDetailBoundsTransform = BoundsTransform { _, _ ->
    spatialExpressiveSpring()
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailScreen(
    movie: Movie,
    pressOnBack: () -> Unit
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No Scope found")
    val roundedCornerAnim by animatedVisibilityScope.transition
        .animateDp(label = "rounded corner") { enterExit: EnterExitState ->
            when (enterExit) {
                EnterExitState.PreEnter -> TMDb_8_dp
                EnterExitState.Visible -> 0.dp
                EnterExitState.PostExit -> TMDb_8_dp
            }
        }

    with(sharedTransitionScope) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .clip(RoundedCornerShape(roundedCornerAnim))
                .background(MaterialTheme.colors.background)
                .sharedBounds(
                    rememberSharedContentState(
                        key = TMDbSharedElementKey(
                            movieId = movie.id,
                            type = TMDbSharedElementType.Bounds
                        )
                    ),
                    animatedVisibilityScope,
                    clipInOverlayDuringTransition = OverlayClip(
                        RoundedCornerShape(roundedCornerAnim)
                    ),
                    boundsTransform = TMDbDetailBoundsTransform,
                    exit = fadeOut(nonSpatialExpressiveSpring()),
                    enter = fadeIn(nonSpatialExpressiveSpring()),
                )

        ) {
            Box {
                AsyncImage(
                    model = movie.backdropPath,
                    contentScale = ContentScale.FillHeight,
                    contentDescription = null,
                    modifier = Modifier.height(360.dp)
                        .fillMaxWidth()
                        .sharedBounds(
                            rememberSharedContentState(
                                key = TMDbSharedElementKey(
                                    movieId = movie.id,
                                    type = TMDbSharedElementType.Image
                                )
                            ),
                            animatedVisibilityScope = animatedVisibilityScope,
                            exit = fadeOut(),
                            enter = fadeIn(),
                            boundsTransform = TMDbDetailBoundsTransform
                        ),
                )
                Up(pressOnBack)
            }
            Column(
                modifier = Modifier.padding(TMDb_4_dp).fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(TMDb_8_dp).sharedBounds(
                        rememberSharedContentState(
                            key = TMDbSharedElementKey(
                                movieId = movie.id,
                                type = TMDbSharedElementType.Title
                            )
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = TMDbDetailBoundsTransform
                    ),
                    text = movie.name,
                    style = typography.h4,
                    color = MaterialTheme.colors.onSurface
                )
                Row {
                    movie.releaseDate?.let {
                        DetailItem(
                            Res.string.release_date,
                            it,
                            movie.id,
                            TMDbSharedElementType.ReleaseDate
                        )
                    }
                    DetailItem(
                        Res.string.vote_average,
                        movie.voteAverage.toString(),
                    )
                    DetailItem(
                        Res.string.vote_count,
                        movie.voteCount.toString(),
                        movie.id,
                        TMDbSharedElementType.Vote
                    )
                }
                Text(
                    modifier = Modifier.padding(TMDb_8_dp),
                    text = movie.overview,
                    style = typography.body1,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DetailItem(
    titleRes: StringResource,
    value: String,
    movieId: Int? = null,
    type: TMDbSharedElementType? = null,
    modifier: Modifier = Modifier
) {
    val sharedTransitionScope = LocalSharedTransitionScope.current
        ?: throw IllegalStateException("No Scope found")
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalStateException("No Scope found")

    Column(
        modifier = modifier.padding(TMDb_4_dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(TMDb_4_dp),
            text = stringResource(titleRes),
            style = typography.subtitle1,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSurface
        )
        with(sharedTransitionScope) {
            Text(
                modifier = Modifier.padding(TMDb_4_dp)
                    .then(
                        if (movieId != null && type != null) Modifier.sharedBounds(
                            rememberSharedContentState(
                                key = TMDbSharedElementKey(
                                    movieId = movieId,
                                    type = type
                                )
                            ),
                            animatedVisibilityScope = animatedVisibilityScope,
                            boundsTransform = TMDbDetailBoundsTransform
                        ) else Modifier
                    ),
                text = value,
                style = typography.subtitle2,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.Up(upPress: () -> Unit) {
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
        ?: throw IllegalArgumentException("No Scope found")
    with(animatedVisibilityScope) {
        IconButton(
            onClick = upPress,
            modifier = Modifier
                .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 3f)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .size(36.dp)
                .animateEnterExit(
                    enter = scaleIn(tween(300, delayMillis = 300)),
                    exit = scaleOut(tween(20))
                )
                .background(
                    color = MaterialTheme.colors.background,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                tint = MaterialTheme.colors.onSurface,
                contentDescription = "Back",
            )
        }
    }
}