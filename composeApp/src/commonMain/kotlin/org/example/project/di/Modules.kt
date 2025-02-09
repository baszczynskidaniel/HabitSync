package org.example.project.di

import org.example.project.app.AppViewModel
import org.example.project.authentication.data.network.KtorRemoteUserDataSource
import org.example.project.authentication.data.network.RemoteUserDataSource
import org.example.project.authentication.data.repository.DefaultUserRepository
import org.example.project.authentication.domain.UserRepository
import org.example.project.authentication.presentation.log_in.LoginViewModel
import org.example.project.authentication.presentation.register.SignUpViewModel
import org.example.project.core.data.UserDataRepositoryImpl
import org.example.project.core.data.datastore.MIUPreferencesDataSource
import org.example.project.core.data.network.HttpClientFactory
import org.example.project.core.domain.UserDataRepository
import org.example.project.habit.data.network.KtorRemoteHabitDataSource
import org.example.project.habit.data.network.RemoteHabitDataSource
import org.example.project.habit.data.repository.DefaultHabitRepository
import org.example.project.habit.domain.HabitRepository
import org.example.project.habit.presentation.SelectedHabitViewModel
import org.example.project.habit.presentation.add_category.AddCategoryAction
import org.example.project.habit.presentation.add_category.AddCategoryViewModel
import org.example.project.habit.presentation.add_habit.AddEditHabitViewModel
import org.example.project.habit.presentation.categories_list.CategoriesListViewModel
import org.example.project.habit.presentation.habit_detail.HabitDetailViewModel
import org.example.project.habit.presentation.habit_list.HabitListViewModel
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
    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteHabitDataSource).bind<RemoteHabitDataSource>()
    singleOf(::DefaultHabitRepository).bind<HabitRepository>()

    singleOf(::KtorRemoteUserDataSource).bind<RemoteUserDataSource>()
    singleOf(::DefaultUserRepository).bind<UserRepository>()


    singleOf(::MIUPreferencesDataSource)
    single {
        get<DataStoreFactory>().create()
    }
    singleOf(::UserDataRepositoryImpl).bind<UserDataRepository>()


    viewModelOf(::AppViewModel)


    viewModelOf(::AddCategoryViewModel)
    viewModelOf(::CategoriesListViewModel)

    viewModelOf(::AddEditHabitViewModel)
    viewModelOf(::HabitDetailViewModel)
    viewModelOf(::SelectedHabitViewModel)

    viewModelOf(::SignUpViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HabitListViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::LanguageViewModel)
}