package di

import base.jsonModule
import base.ktorModule
import domain.repository.DisneyRepository
import data.repository.DisneyRepositoryImpl
import viewmodel.DisneyViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(jsonModule, ktorModule, sharedModule)
    }
}

val sharedModule = module {
    singleOf(::DisneyRepositoryImpl).bind<DisneyRepository>()
    viewModelOf(::DisneyViewModel)
}