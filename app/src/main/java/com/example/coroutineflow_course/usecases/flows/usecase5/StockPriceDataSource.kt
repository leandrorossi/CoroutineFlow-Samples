package com.example.coroutineflow_course.usecases.flows.usecase5

import android.util.Log
import com.example.coroutineflow_course.usecases.flows.mock.FlowMockApi
import com.example.coroutineflow_course.usecases.flows.mock.Stock
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import retrofit2.HttpException

interface StockPriceDataSource {
    val latestStockList: Flow<List<Stock>>
}

class NetworkStockPriceDataSource(mockApi: FlowMockApi) : StockPriceDataSource {

    override val latestStockList: Flow<List<Stock>> = flow {
        while (true) {
            Log.e("Flow", "flow collected")
            val currentStockList = mockApi.getCurrentStockPrices()
            emit(currentStockList)
            delay(5_000)
        }
    }.retry { cause ->
        Log.e("Flow", "Enter retry operator with $cause")

        val shouldRetry = cause is HttpException

        if (shouldRetry) {
            delay(5_000)
        }

        shouldRetry
    }
}