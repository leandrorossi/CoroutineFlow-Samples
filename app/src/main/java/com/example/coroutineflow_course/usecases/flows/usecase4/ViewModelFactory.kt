package com.example.coroutineflow_course.usecases.flows.usecase4

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val stockPriceDataSource: StockPriceDataSource) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(StockPriceDataSource::class.java)
            .newInstance(stockPriceDataSource)
    }
}   