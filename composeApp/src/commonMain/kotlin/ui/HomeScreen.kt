package ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import component.Divider
import component.ErrorScreen
import component.ShimmerLoading
import disney_compose_multiplatform.composeapp.generated.resources.Res
import disney_compose_multiplatform.composeapp.generated.resources.app_name
import domain.model.Poster
import org.jetbrains.compose.resources.stringResource
import utils.Dimens.disney_150_dp
import utils.Dimens.disney_16_dp
import viewmodel.DisneyViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    viewModel: DisneyViewModel = viewModel { DisneyViewModel() },
    onClick: (Poster) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state = viewModel.uiState.collectAsState().value
    if (state.isLoading) {
        ShimmerLoading()
    }
    if (state.error.isNotEmpty()) {
        ErrorScreen(state.error, viewModel::refresh)
    }
    if (state.posters.isNotEmpty()) {
        MediumTopAppBar(viewModel, state.posters, onClick, animatedVisibilityScope)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MediumTopAppBar(
    viewModel: DisneyViewModel,
    posters: List<Poster>,
    onClick: (Poster) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colors.background,
                    scrolledContainerColor = MaterialTheme.colors.background
                ),
                title = {
                    Text(
                        stringResource(Res.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSurface
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) {
        PullToRefreshBox(
            modifier = Modifier.padding(it),
            isRefreshing = viewModel.isRefreshing,
            onRefresh = { viewModel.refresh(showShimmer = false) }
        ) {
            VerticalCollection(
                posters,
                onClick,
                animatedVisibilityScope,
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.VerticalCollection(
    posters: List<Poster>,
    onClick: (Poster) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    LazyColumn(modifier = Modifier.background(MaterialTheme.colors.background)) {
        items(
            items = posters,
            itemContent = { item ->
                VerticalListItem(item = item, onClick, animatedVisibilityScope)
                Divider()
            }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.VerticalListItem(
    item: Poster,
    onClick: (Poster) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val typography = MaterialTheme.typography
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(disney_16_dp)
            .clickable { onClick.invoke(item) }
    ) {
        AsyncImage(
            model = item.poster,
            contentDescription = null,
            modifier = Modifier.sharedElement(
                state = rememberSharedContentState(key = item.id),
                animatedVisibilityScope = animatedVisibilityScope,
                boundsTransform = { _, _ ->
                    tween(400)
                }
            )
                .height(disney_150_dp)
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.height(disney_16_dp))
        Text(
            text = item.name,
            style = typography.h6,
            color = MaterialTheme.colors.onSurface
        )
        Text(
            text = item.release,
            style = typography.body2,
            color = MaterialTheme.colors.onSurface
        )
        Text(
            text = item.playtime,
            style = typography.subtitle2,
            color = MaterialTheme.colors.onSurface
        )
    }
}