package fr.benju.tasks.core.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface ICoroutineDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}
