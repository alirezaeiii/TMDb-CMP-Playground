package domain.repository

import domain.model.Poster

interface DisneyRepository {
    suspend fun getPosters(): Result<List<Poster>>
}