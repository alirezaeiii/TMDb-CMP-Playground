package ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
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
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import component.DetailsScroller
import component.ToolbarState
import component.isShown
import domain.model.Poster
import utils.Dimens.disney_12_dp
import utils.Dimens.disney_16_dp
import utils.Dimens.disney_32_dp
import utils.Dimens.disney_448_dp
import utils.Dimens.disney_8_dp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DetailScreen(
    poster: Poster,
    pressOnBack: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
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
            item = poster,
            contentAlpha = { contentAlpha.value },
            animatedVisibilityScope = animatedVisibilityScope
        )
        DetailsToolbar(
            toolbarState, poster.name, pressOnBack,
            contentAlpha = { contentAlpha.value }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.DetailsContent(
    scrollState: ScrollState,
    onNamePosition: (Float) -> Unit,
    item: Poster,
    contentAlpha: () -> Float,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val typography = MaterialTheme.typography

    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        AsyncImage(
            model = item.poster,
            contentDescription = null,
            modifier = Modifier.sharedElement(
                state = rememberSharedContentState(key = item.id),
                animatedVisibilityScope = animatedVisibilityScope
            )
                .fillMaxWidth()
                .height(disney_448_dp)
                .alpha(contentAlpha()),
            contentScale = ContentScale.Crop,
        )
        Spacer(
            Modifier
                .height(disney_16_dp)
                .onGloballyPositioned { onNamePosition(it.positionInWindow().y) })
        Text(
            modifier = Modifier.padding(disney_8_dp),
            text = item.description,
            style = typography.body1,
            fontSize = 18.sp,
            color = MaterialTheme.colors.onSurface
        )
        Text(
            modifier = Modifier.padding(disney_8_dp),
            text = item.plot,
            style = typography.body1,
            fontSize = 18.sp,
            color = MaterialTheme.colors.onSurface
        )
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
        DetailsToolbar(
            name = name,
            onBackClick = pressOnBack,
        )
    } else {
        HeaderActions(
            onBackClick = pressOnBack,
            modifier = Modifier.alpha(contentAlpha())
        )
    }
}

@Composable
private fun DetailsToolbar(
    name: String,
    onBackClick: () -> Unit
) {
    Surface {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.surface
        ) {
            IconButton(
                onBackClick,
                Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back",
                    tint = MaterialTheme.colors.onSurface
                )
            }
            Text(
                text = name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .wrapContentSize(Alignment.CenterStart)
            )
        }
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
            .padding(top = disney_12_dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val iconModifier = Modifier
            .sizeIn(
                maxWidth = disney_32_dp,
                maxHeight = disney_32_dp
            )
            .background(
                color = MaterialTheme.colors.surface,
                shape = CircleShape
            )

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(start = disney_12_dp)
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