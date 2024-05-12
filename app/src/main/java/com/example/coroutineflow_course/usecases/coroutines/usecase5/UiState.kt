package com.example.coroutineflow_course.usecases.coroutines.usecase5

import com.example.coroutineflow_course.mock.AndroidVersion

open class UiState {
    data object Loading : UiState()
    data class Success(val androidVersions: List<AndroidVersion>) : UiState()
    data class Error(val message: String) : UiState()
}