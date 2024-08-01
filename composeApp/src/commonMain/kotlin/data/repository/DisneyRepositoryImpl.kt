package data.repository

import data.response.PosterDto
import data.response.asDomainModel
import domain.model.Poster
import domain.repository.DisneyRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

class DisneyRepositoryImpl(
    private val httpClient: HttpClient,
    private val json: Json
) : DisneyRepository {

    override suspend fun getPosters(): Result<List<Poster>> {
        return try {
            val response: String = httpClient.get("DisneyPosters2.json").body()
            Result.success(json.decodeFromString<List<PosterDto>>(response).asDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}