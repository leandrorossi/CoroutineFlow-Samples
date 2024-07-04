package com.example.coroutineflow_course.usecases.flows.utils

import com.example.coroutineflow_course.usecases.flows.mock.Stock

open class UiState {
    object Loading : UiState()
    data class Success(val stockList: List<Stock>) : UiState()
    data class Error(val message: String) : UiState()
}