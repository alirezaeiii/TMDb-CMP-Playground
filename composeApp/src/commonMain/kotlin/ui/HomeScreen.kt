package ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import component.ErrorScreen
import component.ShimmerLoading
import component.TMDbCard
import component.TMDbTopBar
import component.TMDbVerticalLazyGrid
import domain.model.Movie
import org.koin.compose.viewmodel.koinViewModel
import utils.Dimens.TMDb_8_dp
import viewmodel.TMDbViewModel

@Composable
fun HomeScreen(
    viewModel: TMDbViewModel = koinViewModel<TMDbViewModel>(),
    onClick: (Movie) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    if (state.isLoading) {
        ShimmerLoading()
    }
    if (state.error.isNotEmpty()) {
        ErrorScreen(state.error, viewModel::refresh)
    }
    if (state.movies.isNotEmpty()) {
        HomeScreen(viewModel, state.movies, onClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    viewModel: TMDbViewModel,
    movies: List<Movie>,
    onClick: (Movie) -> Unit
) {
    TMDbTopBar {
        PullToRefreshBox(
            modifier = Modifier.padding(it),
            isRefreshing = viewModel.isRefreshing,
            onRefresh = { viewModel.refresh() }
        ) {
            VerticalCollection(
                movies,
                onClick
            )
        }
    }
}

@Composable
private fun VerticalCollection(
    movies: List<Movie>,
    onClick: (Movie) -> Unit
) {
    TMDbVerticalLazyGrid(content = {
        items(
            items = movies,
            itemContent = { movie ->
                TMDbCard(
                    movie,
                    onClick,
                    Modifier.padding(vertical = TMDb_8_dp)
                )
            }
        )
    })
}