package domain.repository

import domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface TMDbRepository {
    suspend fun getPosters(): Flow<Result<List<Movie>>>
}