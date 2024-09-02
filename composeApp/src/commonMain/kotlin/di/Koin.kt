package di

import base.jsonModule
import base.ktorModule
import data.database.AppDatabase
import data.repository.TMDbRepositoryImpl
import domain.repository.TMDbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import viewmodel.TMDbViewModel

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            jsonModule,
            ktorModule,
            dispatcherModule,
            sharedModule,
            platformModule(),
            persistenceModule
        )
    }
}

expect fun platformModule(): Module

val sharedModule = module {
    single<TMDbRepository> { TMDbRepositoryImpl(get(), get(), get(named("io"))) }
    viewModelOf(::TMDbViewModel)
}

val dispatcherModule = module {
    single(named("io")) { Dispatchers.IO }
}

val persistenceModule = module {
    single { get<AppDatabase>().movieDao() }
}