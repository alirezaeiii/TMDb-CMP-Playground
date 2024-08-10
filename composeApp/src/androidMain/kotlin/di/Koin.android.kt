package di

import data.database.AppDatabase
import data.database.getDatabaseBuilder
import org.koin.dsl.module

actual fun platformModule() = module {
    single<AppDatabase> { getDatabaseBuilder(get()) }
}