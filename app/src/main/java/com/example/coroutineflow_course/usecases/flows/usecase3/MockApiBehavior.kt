package com.example.coroutineflow_course.usecases.flows.usecase3

import android.content.Context
import com.example.coroutineflow_course.mock.MockNetworkInterceptor
import com.example.coroutineflow_course.usecases.flows.mock.createFlowMockApi
import com.example.coroutineflow_course.usecases.flows.mock.fakeCurrentStockPrices
import com.google.gson.Gson

fun mockApi(context: Context) =
    createFlowMockApi(
        MockNetworkInterceptor()
            .mock(
                path = "/current-stock-prices",
                body = { Gson().toJson(fakeCurrentStockPrices(context)) },
                status = 200,
                delayInMs = 1500,
            )
    )