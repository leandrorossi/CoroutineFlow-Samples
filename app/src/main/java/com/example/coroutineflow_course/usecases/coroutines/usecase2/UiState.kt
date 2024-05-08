package com.example.coroutineflow_course.usecases.coroutines.usecase2

import com.example.coroutineflow_course.mock.VersionFeatures

open class UiState {
    data object Loading : UiState()
    data class Success(val versionFeatures: List<VersionFeatures>) : UiState()
    data class Error(val message: String) : UiState()
}