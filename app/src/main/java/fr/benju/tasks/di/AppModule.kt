package fr.benju.tasks.di

import fr.benju.tasks.data.repository.TaskRepositoryImpl
import fr.benju.tasks.data.repository.UserPreferencesRepositoryImpl
import fr.benju.tasks.domain.repository.TaskRepository
import fr.benju.tasks.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        impl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository
}
