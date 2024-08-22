package data.repository

import data.database.AppDatabase
import data.database.asDomainModel
import data.response.TMDbWrapper
import data.response.asDatabaseModel
import domain.model.Movie
import domain.repository.TMDbRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TMDbRepositoryImpl(
    private val httpClient: HttpClient,
    private val database: AppDatabase,
    private val dispatcher: CoroutineDispatcher
) : TMDbRepository {

    override suspend fun getPosters(): Flow<Result<List<Movie>>> = flow {
        val cache = database.movieDao().getMovies()
        if (cache.isEmpty()) {
            try {
                // ****** STEP 1: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
                refresh()
                // ****** STEP 3: VIEW CACHE ******
                emit(Result.success(database.movieDao().getMovies().asDomainModel()))
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
                emit(Result.success(database.movieDao().getMovies().asDomainModel()))
            } catch (_: Throwable) {
            }
        }
    }.flowOn(dispatcher)

    private suspend fun refresh() {
        val response = httpClient.get("discover/movie").body<TMDbWrapper>()
        database.movieDao().insertAll(*response.items.asDatabaseModel())
    }
}