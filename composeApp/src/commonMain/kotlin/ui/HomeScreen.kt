package ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import component.ErrorScreen
import component.ShimmerLoading
import component.TMDbCard
import component.TMDbTopBar
import domain.model.Movie
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import utils.Dimens.TMDb_140_dp
import utils.Dimens.TMDb_8_dp
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
    movies: List<Movie>,
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
                movies,
                onClick,
                animatedVisibilityScope,
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.VerticalCollection(
    movies: List<Movie>,
    onClick: (Movie) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = TMDb_140_dp),
        contentPadding = PaddingValues(
            start = TMDb_8_dp,
            end = TMDb_8_dp,
            bottom = TMDb_8_dp
        ),
        horizontalArrangement = Arrangement.spacedBy(
            TMDb_8_dp, Alignment.CenterHorizontally
        ),
        content = {
            items(
                items = movies,
                itemContent = { movie ->
                    TMDbCard(
                        movie,
                        onClick,
                        animatedVisibilityScope,
                        Modifier.padding(vertical = TMDb_8_dp)
                    )
                }
            )
        })
}