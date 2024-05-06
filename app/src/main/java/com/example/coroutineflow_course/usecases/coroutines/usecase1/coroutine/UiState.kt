package com.example.coroutineflow_course.usecases.coroutines.usecase1.coroutine

import com.example.coroutineflow_course.mock.VersionFeatures

open class UiState {
    data object Loading : UiState()
    data class Success(val versionFeatures: VersionFeatures) : UiState()
    data class Error(val message: String) : UiState()
}