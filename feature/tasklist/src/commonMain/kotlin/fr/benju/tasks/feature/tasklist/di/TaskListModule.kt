package fr.benju.tasks.feature.tasklist.di

import fr.benju.tasks.feature.tasklist.TaskListViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val taskListModule = module {
    viewModelOf(::TaskListViewModel)
}
