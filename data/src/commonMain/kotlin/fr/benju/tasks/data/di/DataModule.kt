package fr.benju.tasks.data.di

import fr.benju.tasks.core.dispatchers.CoroutineDispatchers
import fr.benju.tasks.core.dispatchers.ICoroutineDispatchers
import fr.benju.tasks.data.database.DatabaseDriverFactory
import fr.benju.tasks.data.database.TaskDatabase
import fr.benju.tasks.data.mapper.TaskMapper
import fr.benju.tasks.data.preferences.SettingsFactory
import fr.benju.tasks.data.repository.TaskRepositoryImpl
import fr.benju.tasks.data.repository.UserPreferencesRepositoryImpl
import fr.benju.tasks.data.usecase.AddTaskUseCaseImpl
import fr.benju.tasks.data.usecase.DeleteTaskUseCaseImpl
import fr.benju.tasks.data.usecase.GetDarkModeUseCaseImpl
import fr.benju.tasks.data.usecase.GetTaskByIdUseCaseImpl
import fr.benju.tasks.data.usecase.GetTasksUseCaseImpl
import fr.benju.tasks.data.usecase.SetDarkModeUseCaseImpl
import fr.benju.tasks.data.usecase.ToggleTaskStatusUseCaseImpl
import fr.benju.tasks.data.usecase.UpdateTaskUseCaseImpl
import fr.benju.tasks.domain.repository.TaskRepository
import fr.benju.tasks.domain.repository.UserPreferencesRepository
import fr.benju.tasks.domain.usecase.AddTaskUseCase
import fr.benju.tasks.domain.usecase.DeleteTaskUseCase
import fr.benju.tasks.domain.usecase.GetDarkModeUseCase
import fr.benju.tasks.domain.usecase.GetTaskByIdUseCase
import fr.benju.tasks.domain.usecase.GetTasksUseCase
import fr.benju.tasks.domain.usecase.SetDarkModeUseCase
import fr.benju.tasks.domain.usecase.ToggleTaskStatusUseCase
import fr.benju.tasks.domain.usecase.UpdateTaskUseCase
import org.koin.dsl.module

val dataModule = module {
    // Database
    single { get<DatabaseDriverFactory>().createDriver() }
    single { TaskDatabase(get()) }

    // Settings
    single { get<SettingsFactory>().createSettings() }

    // Mapper
    single { TaskMapper() }

    // Repositories
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    single<UserPreferencesRepository> { UserPreferencesRepositoryImpl(get()) }

    // Dispatchers
    single<ICoroutineDispatchers> { CoroutineDispatchers() }

    // Use cases
    factory<GetTasksUseCase> { GetTasksUseCaseImpl(get(), get()) }
    factory<GetTaskByIdUseCase> { GetTaskByIdUseCaseImpl(get()) }
    factory<AddTaskUseCase> { AddTaskUseCaseImpl(get(), get()) }
    factory<UpdateTaskUseCase> { UpdateTaskUseCaseImpl(get(), get()) }
    factory<DeleteTaskUseCase> { DeleteTaskUseCaseImpl(get(), get()) }
    factory<ToggleTaskStatusUseCase> { ToggleTaskStatusUseCaseImpl(get(), get()) }
    factory<GetDarkModeUseCase> { GetDarkModeUseCaseImpl(get()) }
    factory<SetDarkModeUseCase> { SetDarkModeUseCaseImpl(get()) }
}
