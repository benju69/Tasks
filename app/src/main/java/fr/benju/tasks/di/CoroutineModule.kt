package fr.benju.tasks.di

import fr.benju.tasks.core.dispatchers.CoroutineDispatchers
import fr.benju.tasks.core.dispatchers.ICoroutineDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @Provides
    @Singleton
    fun provideCoroutineDispatchers(): ICoroutineDispatchers {
        return CoroutineDispatchers()
    }
}
