package viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import domain.model.Poster
import domain.repository.DisneyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DisneyViewModel(private val repository: DisneyRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    var isRefreshing by mutableStateOf(false)
        private set

    init {
        refresh()
    }

    fun refresh(isLoading: Boolean = true) {
        if (isLoading) {
            _uiState.update {
                UiState(isLoading = isLoading)
            }
        }
        viewModelScope.launch {
            try {
                isRefreshing = true
                repository.getPosters().collect {
                    it.fold(
                        onSuccess = { posters ->
                            _uiState.update {
                                UiState(posters = posters)
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
    var posters: List<Poster> = emptyList(),
    var error: String = ""
)