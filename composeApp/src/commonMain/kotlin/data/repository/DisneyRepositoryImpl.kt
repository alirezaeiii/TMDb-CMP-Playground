package data.repository

import base.Network
import data.response.PosterDto
import data.response.asDomainModel
import domain.model.Poster
import domain.repository.DisneyRepository
import io.github.aakira.napier.Napier
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

class DisneyRepositoryImpl: DisneyRepository {

    private val httpClient = Network.httpClient

    override suspend fun getPosters(): Result<List<Poster>> {
        return try {
            val response: String = httpClient.get("DisneyPosters2.json").body()
            val json = Json { ignoreUnknownKeys = true }
            Result.success(json.decodeFromString<List<PosterDto>>(response).asDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}