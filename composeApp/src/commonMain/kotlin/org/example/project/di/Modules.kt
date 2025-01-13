package org.example.project.di

import org.example.project.app.AppViewModel
import org.example.project.core.data.UserDataRepositoryImpl
import org.example.project.core.data.datastore.MIUPreferencesDataSource
import org.example.project.core.domain.UserDataRepository
import org.example.project.settings.data.DataStoreFactory
import org.example.project.settings.presentation.language.LanguageViewModel
import org.example.project.settings.presentation.settings_list.SettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module


expect val platformModule: Module

val sharedModule = module {

    singleOf(::MIUPreferencesDataSource)
    single {
        get<DataStoreFactory>().create()
    }
    singleOf(::UserDataRepositoryImpl).bind<UserDataRepository>()

    viewModelOf(::AppViewModel)

    viewModelOf(::SettingsViewModel)
    viewModelOf(::LanguageViewModel)
}