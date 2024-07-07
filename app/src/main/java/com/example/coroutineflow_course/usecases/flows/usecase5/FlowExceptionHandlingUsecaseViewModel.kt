package com.example.coroutineflow_course.usecases.flows.usecase5

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutineflow_course.usecases.flows.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn

class FlowExceptionHandlingUsecaseViewModel(
    stockPriceDataSource: StockPriceDataSource
) : ViewModel() {

    /*

       Exception Handling Goals:
       - for HttpExceptions in the datasource
           - re-collect from the flow
           - delay for 5 seconds before re-collecting the flow
       - for all other Exceptions within the whole flow pipeline
           - show toast with error message by emitting UiState.Error

    */

    val currentStockAsStateFlow: Flow<UiState> = stockPriceDataSource
        .latestStockList
        .map {  UiState.Success(it) as UiState }
        .onCompletion { Log.d("Flow", "onCompletion") }
        .catch { throwable ->
            Log.d("Flow", "Enter catch operator with $throwable")
            emit(UiState.Error("something went wrong"))
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = UiState.Loading,
            started = SharingStarted.WhileSubscribed(5000),
        )

}

