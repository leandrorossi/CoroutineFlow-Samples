package com.example.coroutineflow_course.usecases.coroutines.usecase7

import com.example.coroutineflow_course.mock.AndroidVersion

open class UiState {
    open class Loading : UiState() {
        object LoadFromDb : Loading()
        object LoadFromNetwork : Loading()
    }

    data class Success(val dataSource: DataSource, val androidVersions: List<AndroidVersion>) :
        UiState()

    data class Error(val dataSource: DataSource, val message: String) : UiState()
}