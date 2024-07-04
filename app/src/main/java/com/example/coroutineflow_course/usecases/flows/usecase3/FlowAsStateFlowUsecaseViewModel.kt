package com.example.coroutineflow_course.usecases.flows.usecase3

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutineflow_course.usecases.flows.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

class FlowAsStateFlowUsecaseViewModel(
    stockPriceDataSource: StockPriceDataSource
) : ViewModel() {

    val currentStockAsStateFlow: Flow<UiState> = stockPriceDataSource
        .latestStockList
        .map { UiState.Success(it) as UiState }
        .onCompletion { Log.d("Flow", "onCompletion") }
        .stateIn(
            scope = viewModelScope,
            initialValue = UiState.Loading,
            started = SharingStarted.WhileSubscribed(5000),
        )

}

