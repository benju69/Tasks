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
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    @ViewModelScoped
    abstract fun bindGetTasksUseCase(
        impl: GetTasksUseCaseImpl
    ): GetTasksUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetTaskByIdUseCase(
        impl: GetTaskByIdUseCaseImpl
    ): GetTaskByIdUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindUpdateTaskUseCase(
        impl: UpdateTaskUseCaseImpl
    ): UpdateTaskUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindDeleteTaskUseCase(
        impl: DeleteTaskUseCaseImpl
    ): DeleteTaskUseCase

    @Binds
    abstract fun bindToggleTaskStatusUseCase(
        impl: ToggleTaskStatusUseCaseImpl
    ): ToggleTaskStatusUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetDarkModeUseCase(
        impl: GetDarkModeUseCaseImpl
    ): GetDarkModeUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindSetDarkModeUseCase(
        impl: SetDarkModeUseCaseImpl
    ): SetDarkModeUseCase
}
