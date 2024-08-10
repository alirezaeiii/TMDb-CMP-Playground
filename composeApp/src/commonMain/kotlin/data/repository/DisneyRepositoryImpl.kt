package data.repository

import data.database.AppDatabase
import data.database.asDomainModel
import data.response.PosterDto
import data.response.asDatabaseModel
import domain.model.Poster
import domain.repository.DisneyRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class DisneyRepositoryImpl(
    private val httpClient: HttpClient,
    private val json: Json,
    private val database: AppDatabase
) : DisneyRepository {

    override suspend fun getPosters(): Flow<Result<List<Poster>>> = flow {
        val cache = database.posterDao().getPosters()
        if (cache.isEmpty()) {
            try {
                // ****** STEP 1: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
                refresh()
                // ****** STEP 3: VIEW CACHE ******
                emit(Result.success(database.posterDao().getPosters().asDomainModel()))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        } else {
            // ****** STEP 1: VIEW CACHE ******
            emit(Result.success(cache.asDomainModel()))
            try {
                // ****** STEP 2: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
                refresh()
                // ****** STEP 3: VIEW CACHE ******
                emit(Result.success(database.posterDao().getPosters().asDomainModel()))
            } catch (_: Throwable) {
            }
        }
    }

    private suspend fun refresh() {
        val response: String = httpClient.get("DisneyPosters2.json").body()
        val items = json.decodeFromString<List<PosterDto>>(response)
        database.posterDao().insertAll(*items.asDatabaseModel())
    }
}