package di

import base.jsonModule
import base.ktorModule
import data.repository.DisneyRepositoryImpl
import domain.repository.DisneyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import viewmodel.DisneyViewModel

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(jsonModule, ktorModule, dispatcherModule, sharedModule, platformModule())
    }
}

expect fun platformModule(): Module

val sharedModule = module {
    single<DisneyRepository> { DisneyRepositoryImpl(get(), get(), get(), get(named("io"))) }
    viewModelOf(::DisneyViewModel)
}

val dispatcherModule = module {
    single(named("io")) { Dispatchers.IO }
}