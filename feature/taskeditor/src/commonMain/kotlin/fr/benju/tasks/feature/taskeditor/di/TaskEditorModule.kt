package fr.benju.tasks.feature.taskeditor.di

import fr.benju.tasks.feature.taskeditor.TaskEditorViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val taskEditorModule = module {
    viewModelOf(::TaskEditorViewModel)
}
