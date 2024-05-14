package com.example.coroutineflow_course.usecases.coroutines.usecase9

open class UiState {
    object Loading : UiState()
    data class Success(
        val result: String,
        val computationDuration: Long,
        val stringConversionDuration: Long
    ) : UiState()

    data class Error(val message: String) : UiState()
}