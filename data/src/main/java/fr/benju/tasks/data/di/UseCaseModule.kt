package fr.benju.tasks.data.di

import fr.benju.tasks.data.usecase.DeleteTaskUseCaseImpl
import fr.benju.tasks.data.usecase.GetDarkModeUseCaseImpl
import fr.benju.tasks.data.usecase.GetTaskByIdUseCaseImpl
import fr.benju.tasks.data.usecase.GetTasksUseCaseImpl
import fr.benju.tasks.data.usecase.SetDarkModeUseCaseImpl
import fr.benju.tasks.data.usecase.ToggleTaskStatusUseCaseImpl
import fr.benju.tasks.data.usecase.UpdateTaskUseCaseImpl
import fr.benju.tasks.domain.usecase.DeleteTaskUseCase
import fr.benju.tasks.domain.usecase.GetDarkModeUseCase
import fr.benju.tasks.domain.usecase.GetTaskByIdUseCase
import fr.benju.tasks.domain.usecase.GetTasksUseCase
import fr.benju.tasks.domain.usecase.SetDarkModeUseCase
import fr.benju.tasks.domain.usecase.ToggleTaskStatusUseCase
import fr.benju.tasks.domain.usecase.UpdateTaskUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindGetTasksUseCase(
        impl: GetTasksUseCaseImpl
    ): GetTasksUseCase

    @Binds
    @Singleton
    abstract fun bindGetTaskByIdUseCase(
        impl: GetTaskByIdUseCaseImpl
    ): GetTaskByIdUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateTaskUseCase(
        impl: UpdateTaskUseCaseImpl
    ): UpdateTaskUseCase

    @Binds
    @Singleton
    abstract fun bindDeleteTaskUseCase(
        impl: DeleteTaskUseCaseImpl
    ): DeleteTaskUseCase

    @Binds
    @Singleton
    abstract fun bindToggleTaskStatusUseCase(
        impl: ToggleTaskStatusUseCaseImpl
    ): ToggleTaskStatusUseCase

    @Binds
    @Singleton
    abstract fun bindGetDarkModeUseCase(
        impl: GetDarkModeUseCaseImpl
    ): GetDarkModeUseCase

    @Binds
    @Singleton
    abstract fun bindSetDarkModeUseCase(
        impl: SetDarkModeUseCaseImpl
    ): SetDarkModeUseCase
}
