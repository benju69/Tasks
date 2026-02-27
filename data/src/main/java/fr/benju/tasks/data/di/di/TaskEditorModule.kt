package fr.benju.tasks.feature.taskeditor.di

import fr.benju.tasks.data.usecase.AddTaskUseCaseImpl
import fr.benju.tasks.domain.usecase.AddTaskUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TaskEditorModule {

    @Binds
    @Singleton
    abstract fun bindAddTaskUseCase(
        impl: AddTaskUseCaseImpl
    ): AddTaskUseCase
}
