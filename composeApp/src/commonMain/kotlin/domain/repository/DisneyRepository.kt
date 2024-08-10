package domain.repository

import domain.model.Poster
import kotlinx.coroutines.flow.Flow

interface DisneyRepository {
    suspend fun getPosters(): Flow<Result<List<Poster>>>
}