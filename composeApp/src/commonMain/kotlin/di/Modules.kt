package di

import domain.repository.DisneyRepository
import data.repository.DisneyRepositoryImpl
import viewmodel.DisneyViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sharedModule = module {
    singleOf(::DisneyRepositoryImpl).bind<DisneyRepository>()
    viewModelOf(::DisneyViewModel)
}