package com.example.coroutineflow_course.usecases.flows.usecase3

import com.example.coroutineflow_course.usecases.flows.mock.Stock

val appleStock = Stock(
    rank = 1,
    name = "Apple",
    symbol = "AAPL",
    marketCap = 1.0f,
    country = "United Stated",
    currentPrice = 1.0f
)

val googleStock = Stock(
    rank = 4,
    name = "Alphabet",
    symbol = "GOOG",
    marketCap = 2.0f,
    country = "United Stated",
    currentPrice = 2.0f
)