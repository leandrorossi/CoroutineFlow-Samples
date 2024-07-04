package com.example.coroutineflow_course.usecases.flows.usecase1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.coroutineflow_course.usecases.flows.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn

class FlowAsLivedataUsecaseViewModel(
    stockPriceDataSource: StockPriceDataSource
) : ViewModel() {

    val currentStockAsLivedata: LiveData<UiState> = stockPriceDataSource
        .latestStockList
        .map { UiState.Success(it) as UiState }
        .onStart { emit(UiState.Loading) }
        .onCompletion { Log.d("Flow", "onCompletion") }
        .asLiveData()

}

