package ui

import LocalNavAnimatedVisibilityScope
import LocalSharedTransitionScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import component.DetailsScroller
import component.TMDbDetailTopBar
import component.ToolbarState
import component.isShown
import domain.model.Movie
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import tmdb_compose_multiplatform.composeapp.generated.resources.Res
import tmdb_compose_multiplatform.composeapp.generated.resources.release_date
import tmdb_compose_multiplatform.composeapp.generated.resources.vote_average
import tmdb_compose_multiplatform.composeapp.generated.resources.vote_count
import utils.Dimens.TMDb_12_dp
import utils.Dimens.TMDb_16_dp
import utils.Dimens.TMDb_32_dp
import utils.Dimens.TMDb_448_dp
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

@Composable
fun DetailScreen(
    movie: Movie,
    pressOnBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    var detailScroller by remember {
        mutableStateOf(DetailsScroller(scrollState, Float.MIN_VALUE))
    }

    val transitionState =
        remember(detailScroller) { detailScroller.toolbarTransitionState }
    val toolbarState = detailScroller.getToolbarState(LocalDensity.current)

    // Transition that fades in/out the header with the image and the Toolbar
    val transition = updateTransition(transitionState, label = "")
    val contentAlpha = transition.animateFloat(
        transitionSpec = { spring(stiffness = Spring.StiffnessLow) }, label = ""
    ) { toolbarTransitionState ->
        if (toolbarTransitionState == ToolbarState.HIDDEN) 1f else 0f
    }

    Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
        DetailsContent(
            scrollState = scrollState,
            onNamePosition = { newNamePosition ->
                // Comparing to Float.MIN_VALUE as we are just interested on the original
                // position of name on the screen
                if (detailScroller.namePosition == Float.MIN_VALUE) {
                    detailScroller =
                        detailScroller.copy(namePosition = newNamePosition)
                }
            },
            movie = movie,
            contentAlpha = { contentAlpha.value }
        )
        DetailsToolbar(
            toolbarState, movie.name, pressOnBack,
            contentAlpha = { contentAlpha.value }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DetailsContent(
    scrollState: ScrollState,
    onNamePosition: (Float) -> Unit,
    movie: Movie,
    contentAlpha: () -> Float
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
            modifier = Modifier.verticalScroll(scrollState).fillMaxSize().sharedBounds(
                rememberSharedContentState(
                    key = TMDbSharedElementKey(
                        tmdbId = movie.id,
                        type = TMDbSharedElementType.Bounds
                    )
                ),
                animatedVisibilityScope,
                clipInOverlayDuringTransition =
                OverlayClip(RoundedCornerShape(roundedCornerAnim)),
                boundsTransform = TMDbDetailBoundsTransform,
                exit = fadeOut(nonSpatialExpressiveSpring()),
                enter = fadeIn(nonSpatialExpressiveSpring()),
            ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            AsyncImage(
                model = movie.backdropPath,
                contentDescription = null,
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(
                        key = TMDbSharedElementKey(
                            tmdbId = movie.id,
                            type = TMDbSharedElementType.Image
                        )
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    exit = fadeOut(),
                    enter = fadeIn(),
                    boundsTransform = TMDbDetailBoundsTransform
                ).fillMaxWidth()
                    .height(TMDb_448_dp)
                    .alpha(contentAlpha()),
                contentScale = ContentScale.Crop,
            )
            Spacer(
                Modifier
                    .height(TMDb_16_dp)
                    .onGloballyPositioned { onNamePosition(it.positionInWindow().y) })
            Text(
                modifier = Modifier.padding(TMDb_8_dp).sharedBounds(
                    rememberSharedContentState(
                        key = TMDbSharedElementKey(
                            tmdbId = movie.id,
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
                    movie.id,
                    TMDbSharedElementType.Rate
                )
                DetailItem(
                    Res.string.vote_count,
                    movie.voteCount.toString(),
                    movie.id,
                    TMDbSharedElementType.VOTE
                )
            }
            Text(
                modifier = Modifier.padding(TMDb_8_dp),
                text = movie.overview,
                style = typography.body1,
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface
            )
            Text(
                modifier = Modifier.padding(TMDb_8_dp),
                text = movie.overview,
                style = typography.body1,
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSurface
            )
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun DetailItem(
    titleRes: StringResource,
    value: String,
    movieId: Int,
    type: TMDbSharedElementType,
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
                    .sharedBounds(
                        rememberSharedContentState(
                            key = TMDbSharedElementKey(
                                tmdbId = movieId,
                                type = type
                            )
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = TMDbDetailBoundsTransform
                    ),
                text = value,
                style = typography.subtitle2,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}

@Composable
private fun DetailsToolbar(
    toolbarState: ToolbarState,
    name: String,
    pressOnBack: () -> Unit,
    contentAlpha: () -> Float
) {
    if (toolbarState.isShown) {
        TMDbDetailTopBar(name) {
            pressOnBack.invoke()
        }
    } else {
        HeaderActions(
            onBackClick = pressOnBack,
            modifier = Modifier.alpha(contentAlpha())
        )
    }
}

@Composable
private fun HeaderActions(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(top = TMDb_12_dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val iconModifier = Modifier
            .sizeIn(
                maxWidth = TMDb_32_dp,
                maxHeight = TMDb_32_dp
            )
            .background(
                color = MaterialTheme.colors.surface,
                shape = CircleShape
            )

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(start = TMDb_12_dp)
                .then(iconModifier)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "back",
                tint = MaterialTheme.colors.onSurface
            )
        }
    }
}