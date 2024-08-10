package di

import base.jsonModule
import base.ktorModule
import data.repository.DisneyRepositoryImpl
import domain.repository.DisneyRepository
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module
import viewmodel.DisneyViewModel

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(jsonModule, ktorModule, sharedModule, platformModule())
    }
}

expect fun platformModule(): Module

val sharedModule = module {
    singleOf(::DisneyRepositoryImpl).bind<DisneyRepository>()
    viewModelOf(::DisneyViewModel)
}