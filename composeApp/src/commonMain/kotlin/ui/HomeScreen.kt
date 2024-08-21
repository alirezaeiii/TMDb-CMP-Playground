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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import component.Divider
import component.ErrorScreen
import component.ShimmerLoading
import component.TMDbTopBar
import domain.model.Movie
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import utils.Dimens.disney_150_dp
import utils.Dimens.disney_16_dp
import viewmodel.DisneyViewModel

@OptIn(ExperimentalSharedTransitionApi::class, KoinExperimentalAPI::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    viewModel: DisneyViewModel = koinViewModel<DisneyViewModel>(),
    onClick: (Movie) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state by viewModel.uiState.collectAsState()
    if (state.isLoading) {
        ShimmerLoading()
    }
    if (state.error.isNotEmpty()) {
        ErrorScreen(state.error, viewModel::refresh)
    }
    if (state.movies.isNotEmpty()) {
        HomeScreen(viewModel, state.movies, onClick, animatedVisibilityScope)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.HomeScreen(
    viewModel: DisneyViewModel,
    posters: List<Movie>,
    onClick: (Movie) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    TMDbTopBar {
        PullToRefreshBox(
            modifier = Modifier.padding(it),
            isRefreshing = viewModel.isRefreshing,
            onRefresh = { viewModel.refresh(isLoading = false) }
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
    posters: List<Movie>,
    onClick: (Movie) -> Unit,
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
    item: Movie,
    onClick: (Movie) -> Unit,
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
            model = item.backdropPath,
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
        item.releaseDate?.let {
            Text(
                text = it,
                style = typography.body2,
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}