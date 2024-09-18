package viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.Movie
import domain.repository.TMDbRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TMDbViewModel(private val repository: TMDbRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(isLoading = true))
    val uiState = _uiState.onStart {
        refresh()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState(isLoading = true)
    )

    var isRefreshing by mutableStateOf(false)
        private set

    fun refresh() {
        viewModelScope.launch {
            try {
                isRefreshing = true
                repository.getPosters().collect {
                    it.fold(
                        onSuccess = { posters ->
                            _uiState.update {
                                UiState(movies = posters)
                            }
                        },
                        onFailure = { error ->
                            _uiState.update {
                                UiState(error = error.message.orEmpty())
                            }
                        }
                    )
                }
            } finally {
                isRefreshing = false
            }
        }
    }
}

data class UiState(
    var isLoading: Boolean = false,
    var movies: List<Movie> = emptyList(),
    var error: String = ""
)