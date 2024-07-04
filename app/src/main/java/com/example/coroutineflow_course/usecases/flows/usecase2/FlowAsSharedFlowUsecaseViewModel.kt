package com.example.coroutineflow_course.usecases.flows.usecase2

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

class FlowAsSharedFlowUsecaseViewModel(
    stockPriceDataSource: StockPriceDataSource
) : ViewModel() {

    val currentStockAsSharedFlow: Flow<UiState> = stockPriceDataSource
        .latestStockList
        .map { UiState.Success(it) as UiState }
        .onStart { emit(UiState.Loading) }
        .onCompletion { Log.d("Flow", "onCompletion") }
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

}

